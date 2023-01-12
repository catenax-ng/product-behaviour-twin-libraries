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
    @Value("${security.api.role}") private String userrole;

    @Value("${security.api.endpoints.hidatareceiver.notifyresult.noauth:false}")
    private boolean hiDataReceiverNotifyresultNoAuth;

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser(username)
                .password(passwordEncoder().encode(password)).roles("USER");
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        if(hiDataReceiverNotifyresultNoAuth) {
            http.csrf().disable().authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/hidatareceiver/notifyresult")
                    .permitAll()
                    .and()
                    .authorizeRequests()
                    .antMatchers("**")
                    .hasRole(userrole)
                    .anyRequest()
                    .authenticated()
                    .and()
                    .httpBasic();
        } else {
            http.csrf().disable().authorizeRequests()
                    .antMatchers("**")
                    .hasRole(userrole)
                    .anyRequest()
                    .authenticated()
                    .and()
                    .httpBasic();
        }

        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
