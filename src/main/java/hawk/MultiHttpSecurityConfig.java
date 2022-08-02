package hawk;

import hawk.api.jwt.JwtConfigurer;
import hawk.api.jwt.JwtTokenProvider;
import hawk.api.token.TokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

@EnableWebSecurity
public class MultiHttpSecurityConfig {


    @Configuration
    @Order(1)
    public static class JwtWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        private final JwtTokenProvider jwtTokenProvider;

        @Autowired
        public JwtWebSecurityConfigurationAdapter(JwtTokenProvider jwtTokenProvider) {
            this.jwtTokenProvider = jwtTokenProvider;
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/api/jwt/**")
                        .httpBasic().disable()
                        .csrf().disable()
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                        .authorizeRequests()
                        .antMatchers("/api/jwt/auth/signin").permitAll()
                        .anyRequest().authenticated()
                    .and()
                        .apply(new JwtConfigurer(jwtTokenProvider));
        }
    }

    @Configuration
    @Order(2)
    public static class TokenWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Value("${token.http.auth.name:SH_AUTH_TOKEN}")
        private String authHeaderName;

        @Value("${token.http.auth.value:ITSASECRET}")
        private String authHeaderValue;

        protected void configure(HttpSecurity http) throws Exception {

            TokenFilter filter = new TokenFilter(authHeaderName);

            filter.setAuthenticationManager(new AuthenticationManager() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String principal = (String) authentication.getPrincipal();

                if (!authHeaderValue.equals(principal)) {
                    throw new BadCredentialsException("The API key was not found or not the expected value.");
                }
                authentication.setAuthenticated(true);
                return authentication;
            }
            });

            http
                    .antMatcher("/api/token/**")
                    .httpBasic().disable()
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                        .addFilter(filter)
                        .addFilterBefore(new ExceptionTranslationFilter(
                                    new Http403ForbiddenEntryPoint()),
                            filter.getClass()
                        )
                    .authorizeRequests()
                        .anyRequest()
                        .authenticated();
        }
    }

    @Configuration
    @Order(3)
    public static class BasicAuthWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/api/basic/**")
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests().anyRequest().authenticated()
                .and()
                    .httpBasic();
        }
    }

    @Configuration
    @Order(4)
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                        .antMatchers(
                                "/",
                                "/jwt-auth",
                                "/token-auth",
                                "/basic-auth",
                                "/openapi/**",
                                "/openapi.yaml",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/log4j",
                                "/hidden",
                                "/hidden/*",
                                "/login-code",
                                "/login-form-multi"
                        ).permitAll()
                        .anyRequest().authenticated()
                    .and()
                        .formLogin()
                        .loginPage("/login")
                        .permitAll()
                    .and()
                        .logout()
                        .logoutSuccessUrl("/")
                        .permitAll();
        }
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }
}
