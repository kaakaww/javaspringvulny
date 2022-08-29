package hawk;

import hawk.service.SearchService;
import hawk.service.UserSearchService;
import hawk.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableJpaRepositories
public class Config implements WebMvcConfigurer {
    @Bean
    public SearchService searchService(){
        return new SearchService();
    }
    @Bean
    public UserSearchService userSearchService() { return new UserSearchService(); }
    @Bean
    public UserService userService() { return new UserService(); }

}
