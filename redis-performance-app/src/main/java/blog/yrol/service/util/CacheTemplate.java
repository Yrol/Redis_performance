package blog.yrol.service.util;

import reactor.core.publisher.Mono;

/**
 * Template which externalises the service layer's most generic tasks
 * This template will not cover adding to cache when inserting a new records. Hence, the cache related activities are only for get, update and delete.
 * Using generic KEY and Entity
 * **/
public abstract class CacheTemplate<KEY, ENTITY> {

    /**
     * Attempting to get from cache and if not available fetch from DB / source
     * Also save the fetched results to the cache.
     * **/
    public Mono<ENTITY> get(KEY key){
        return getFromCache(key)
                .switchIfEmpty(getFromSource(key)) // if empty
                .flatMap(e -> updateCache(key, e)); // update cache
    }

    /**
     * Updating the source first and then delete it from the cache
     * Additionally we could also update the cache instead of deleting it.
     * **/
    public Mono<ENTITY> update(KEY key, ENTITY entity) {
        return updateSource(key, entity) // update the source first
                .flatMap(e -> deleteFromCache(key).thenReturn(e));
    }

    /**
     * Delete from source first and then from cache
     * **/
    public Mono<Void> delete(KEY key) {
        return deleteFromSource(key)
                .then(deleteFromCache(key)).then();
    }

    abstract protected Mono<ENTITY> getFromSource(KEY key);
    abstract protected Mono<ENTITY> getFromCache(KEY key);
    abstract protected Mono<ENTITY> updateSource(KEY key, ENTITY entity);
    abstract protected Mono<ENTITY> updateCache(KEY key, ENTITY entity);
    abstract protected Mono<Void> deleteFromSource(KEY key);
    abstract protected Mono<Void> deleteFromCache(KEY key);
}
