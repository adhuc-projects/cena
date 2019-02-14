package org.adhuc.cena.menu.port.adapter.rest;

import static org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest.to;
import static org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest.toAnyEndpoint;

import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import org.adhuc.cena.menu.configuration.MenuGenerationProperties;

@Configuration
class ActuatorSecurity extends WebSecurityConfigurerAdapter {

    private static final String HEALTH_ENDPOINT_ID = "health";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatcher(toAnyEndpoint().excluding(HEALTH_ENDPOINT_ID)).authorizeRequests().anyRequest().hasRole("ACTUATOR")
                .and().requestMatcher(to(HEALTH_ENDPOINT_ID)).authorizeRequests().anyRequest().permitAll()
                .and().authorizeRequests().mvcMatchers("/**").permitAll()
                .and().httpBasic();
    }

    @Configuration
    static class UserDetailsConfiguration {

        @Bean
        UserDetailsService userDetailsService(@NonNull PasswordEncoder passwordEncoder,
                                              @NonNull MenuGenerationProperties menuGenerationProperties) {
            User.UserBuilder users = User.builder().passwordEncoder(passwordEncoder::encode);
            InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
            manager.createUser(users.username(menuGenerationProperties.getManagement().getSecurity().getUsername())
                    .password(menuGenerationProperties.getManagement().getSecurity().getPassword())
                    .roles("ACTUATOR").build());
            return manager;
        }

        @Bean
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

    }

}
