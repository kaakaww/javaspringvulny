package hawk;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableJpaRepositories
public class MvcConfig implements WebMvcConfigurer {

    /*public void addViewControllers(ViewControllerRegistry registry) {
        //registry.addViewController("/").setViewName("index");
        //registry.addViewController("/hello").setViewName("hello");
        //registry.addViewController("/login").setViewName("login");
    }*/

}
