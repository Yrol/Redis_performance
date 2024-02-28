package blog.yrol.service.util;

import blog.yrol.entity.Product;
import blog.yrol.repository.ProductRepository;
import org.redisson.api.*;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementation for local cache.
 * @Service is disabled since the ProductCacheTemplate is already enabled (another implementation of CacheTemplate for Redis cache).
 * **/
//@Service
public class ProductLocalCacheTemplate extends CacheTemplate<Integer, Product> {

    @Autowired
    private ProductRepository repository;
    private RLocalCachedMap<Integer, Product> map;

    /**
     * Setting the RedissonClient for local cache map
     * **/
    public ProductLocalCacheTemplate(RedissonClient client) {
        /**
         * Setting the sync strategy to Update (when a value is updated initiate sync)
         * reconnectionStrategy - when a connection is interrupted for some reason then clear the cache
         * **/
        LocalCachedMapOptions<Integer, Product> mapOptions =  LocalCachedMapOptions.<Integer, Product>defaults()
                .syncStrategy(LocalCachedMapOptions.SyncStrategy.UPDATE)
                .reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.CLEAR);
        this.map = client.getLocalCachedMap("product", new TypedJsonJacksonCodec(Integer.class, Product.class), mapOptions);
    }


    @Override
    protected Mono<Product> getFromSource(Integer id) {
        return this.repository.findById(id);
    }

    @Override
    protected Mono<Product> getFromCache(Integer id) {
        return Mono.justOrEmpty(this.map.get(id));
    }

    @Override
    protected Mono<Product> updateSource(Integer id, Product product) {
        return this.repository.findById(id) // check if product exist
                .doOnNext(p -> product.setId(id)) // if the product exist go ahead and update
                .flatMap(p -> this.repository.save(product));
    }

    @Override
    protected Mono<Product> updateCache(Integer id, Product product) {
        /**
         * Updating the map as well as propagating changes to Redis via sink (to be known by the other instances)
         * Using sink for manual / programmatically triggering -  propagating to Redis
         * When the activity is finished, then emit the product (onComplete event)
         * Since this is a Mono, there's no next() and we can only emit one item in Mono unlike Flux.
         * Also capturing the exception
         * **/
        return Mono.create(sink -> this.map.fastPutAsync(id, product)
                .thenAccept(b -> sink.success(product))
                .exceptionally(ex -> {
                    sink.error(ex);
                    return null;
                })
        );
    }

    @Override
    protected Mono<Void> deleteFromSource(Integer id) {
        return this.repository.deleteById(id);
    }

    /**
     * Similar to above updateCache, instead emitting the product after finish, this will emit void via sink.success()
     * **/
    @Override
    protected Mono<Void> deleteFromCache(Integer id) {
        return Mono.create(sink ->
                this.map.fastRemoveAsync(id)
                .thenAccept(b -> sink.success())
                .exceptionally(ex -> {
                    sink.error(ex);
                    return null;
                })
        );
    }
}
