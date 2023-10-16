package net.catena_x.btp.libraries.util.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Value("${security.api.username}") private String username;
    @Value("${security.api.password}") private String password;

    @Value("${security.api.endpoints.noauth.global:false}") private boolean noAuthGlobal;
    @Value("${security.api.endpoints.noauth.callbacks:true}") private boolean noAuthCallbacks;

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser(username)
                .password(passwordEncoder().encode(password)).roles("USER");
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {

        HttpSecurity security = http.csrf().disable();

        if(noAuthGlobal) {
            return security.build();
         }

        if(noAuthCallbacks) {
            security = security
                    .authorizeRequests().requestMatchers(
                            HttpMethod.POST,
                            "/hidatareceiver/notifyresult",
                            "/ruldatacollector/notifycalculation",
                            "/ruldatareceiver/notifyresult",
                            "/api/testinterface/post",
                            "/api/testinterface/postresult",
                            "/api/v1/routine/notification",
                            "/api/testinterface/logpost")
                    .permitAll()
                    .and()
                    .authorizeRequests()
                    .requestMatchers(HttpMethod.GET,
                            "/api/testinterface/get",
                            "/api/testinterface/getresult")
                    .permitAll()
                    .and();
        }

        security.authorizeRequests().requestMatchers(
                        HttpMethod.POST,
                        "/edrcallback")
                .permitAll()
                .and().authorizeRequests()
                .anyRequest()
                .authenticated()
                .and().httpBasic();

        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
