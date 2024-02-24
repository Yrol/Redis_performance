package blog.yrol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = "blog.yrol")
@EnableCaching // enable caching
@EnableScheduling // enable schedule cache evict
public class RedisPerformanceApplication implements CommandLineRunner {

    public static final Logger LOG = LoggerFactory.getLogger(RedisPerformanceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RedisPerformanceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("RedisPerformanceApplication started..");
    }
}
