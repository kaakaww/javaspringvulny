package hawk;

import java.util.Arrays;
import java.util.stream.Stream;

import hawk.entity.Item;
import hawk.repos.ItemRepo;
import hawk.repos.ItemsRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx, ItemRepo repo) {


        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }


            System.out.println(String.format("Load some fixture data %s", dbUrl));

            System.out.println(String.format("Items in DB %d", repo.count()));

            if (repo.count() == 0) {
                repo.findAll().forEach(item -> System.out.println(String.format("item: %s", item.getName())));

                Stream.of(1, 2, 3).forEach(i -> {
                    System.out.println(String.format("Adding item%d", i));
                    repo.save(new Item(String.format("item%d", i), String.format("we have the best items, item%d", i)));
                });

                System.out.println(String.format("Items in DB %d", repo.count()));
                repo.findAll().forEach(item -> System.out.println(String.format("item: %s", item.getName())));
            }

        };
    }


}