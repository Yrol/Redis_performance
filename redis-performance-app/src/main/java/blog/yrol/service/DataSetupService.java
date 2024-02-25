package blog.yrol.service;

import blog.yrol.entity.Product;
import blog.yrol.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This service will start the data service automaticall
 * **/

@Service
public class DataSetupService implements CommandLineRunner {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private R2dbcEntityTemplate entityTemplate;

    /**
     * Loading the SQL schema file which consist of table creation if not exist
     * **/
    @Value("classpath:schema.sql")
    private Resource resource;

    @Override
    public void run(String... args) throws Exception {
        String query = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        System.out.println(query);

        /**
         * Populating the table with 1 to 1000 records
         * Keeping the ID field (first colum to null) as adding new data
         * **/
        Mono<Void> insert = Flux.range(1, 1000)
                .map(i -> new Product(null, "product" + i, ThreadLocalRandom.current().nextInt(1, 100)))
                .collectList() // getting as a list which returns a Mono
                .flatMapMany(l -> this.repository.saveAll(l)) // using flatMapMany since saveAll() returns Flux instead of Mono
                .then();

        /**
         * Running the SQL via entityTemplate and insert data
         * **/
        this.entityTemplate.getDatabaseClient()
                .sql(query)
                .then()
                .then(insert)
                .doFinally(s -> System.out.println("data setup done " + s))
                .subscribe();
    }
}
