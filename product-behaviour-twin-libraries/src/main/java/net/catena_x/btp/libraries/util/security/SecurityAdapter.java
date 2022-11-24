package net.catena_x.btp.libraries.util.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//https://www.baeldung.com/spring-security-basic-authentication
@Configuration
@EnableWebSecurity
public class SecurityAdapter
        extends WebSecurityConfigurerAdapter //FA: For Test only!!!
{
    private static final String ENDPOINT_HIDATARECEIVER_NOTIFYRESULT = "/hidatareceiver/notifyresult";

    @Value("${security.api.username}") private String username;
    @Value("${security.api.password}") private String password;
    @Value("${security.api.role}") private String userrole;

    @Value("${security.api.endpoints.hidatareceiver.notifyresult.noauth:false}")
    private boolean hiDataReceiverNotifyresultNoAuth;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser(username)
            .password(passwordEncoder().encode(password))
            .authorities(userrole);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);

        http.csrf().disable();
        /*
        .authorizeRequests()
                .antMatchers(ENDPOINT_HIDATARECEIVER_NOTIFYRESULT)
                .permitAll().anyRequest()
                .authenticated();
         */
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        if(hiDataReceiverNotifyresultNoAuth) {
            web.ignoring().antMatchers(HttpMethod.POST, ENDPOINT_HIDATARECEIVER_NOTIFYRESULT);
        }
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
