package com.example.myapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private DataSource securityDataSource;

    @Bean
    public JdbcUserDetailsManager user() {
        return new JdbcUserDetailsManager(securityDataSource);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/home").hasAnyRole(
                                "ADMIN", "MANAGER", "COURIER", "CUSTOMER")
                        .requestMatchers("/api/couriers/**").hasAnyRole(
                                "ADMIN", "MANAGER")
                        .requestMatchers("/api/addresses/**").hasAnyRole(
                                "ADMIN", "MANAGER")
                        .requestMatchers("/manager/**").hasRole("MANAGER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/courier/**").hasRole("COURIER")
                        .requestMatchers("/api/files/**").hasAnyRole("ADMIN")
                        // the registration process for new customers
                        .requestMatchers("/api/customers/**").permitAll()
                        // weather & google interfaces are open to anyone
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login-page")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home")
                        .permitAll())
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll())
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/access-denied"));
        return http.build();
    }
}