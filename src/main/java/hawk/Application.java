package hawk;

import java.util.Arrays;
import java.util.stream.Stream;

import hawk.context.TenantContext;
import hawk.entity.Item;
import hawk.entity.User;
import hawk.repos.ItemRepo;
import hawk.repos.UserRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx, ItemRepo repo, UserRepo userRepo) {


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

            System.out.println(String.format("Users in DB %d", userRepo.count()));

            if (userRepo.count() == 0) {
                userRepo.findAll().forEach(item -> System.out.println(String.format("item: %s", item.getName())));

                TenantContext.setCurrentTenant("1234567");
                Stream.of(1, 2, 3).forEach(i -> {
                    System.out.println(String.format("Adding user%d", i));
                    userRepo.save(new User(String.format("user%d", i), String.format("we have the best users, users%d", i), "1234567"));
                });
                userRepo.save(new User("user", "The auth user", "1234567"));

                TenantContext.setCurrentTenant("12345678");
                Stream.of(4, 5, 6).forEach(i -> {
                    System.out.println(String.format("Adding item%d", i));
                    userRepo.save(new User(String.format("user%d", i), String.format("we have the best users, users%d", i), "12345678"));
                });


                System.out.println(String.format("Items in DB %d", userRepo.count()));
                userRepo.findAll().forEach(item -> System.out.println(String.format("user: %s", item.getName())));
            }

        };
    }


}