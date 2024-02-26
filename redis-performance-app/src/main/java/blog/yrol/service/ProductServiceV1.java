package blog.yrol.service;

import blog.yrol.entity.Product;
import blog.yrol.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceV1 {

    @Autowired
    private ProductRepository productRepository;

    public Mono<Product> getProduct(int i) {
        return this.productRepository.findById(i);
    }

    public Mono<Product> updateProduct(int id, Mono<Product> productMono) {
        return this.productRepository.findById(id)
                .flatMap(p -> productMono.doOnNext(pr -> pr.setId(id))) // if the product exists set the ID to the one that is provided in th args
                .flatMap(this.productRepository::save);
    }
}
