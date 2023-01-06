package com.sample.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    protected void configure(final HttpSecurity http) throws Exception {
        //        http.antMatcher("/**")
        //            .authorizeRequests()
        //            .antMatchers("/").permitAll()
        //            .anyRequest().authenticated()
        //            .and().formLogin();

        http.csrf().disable();
        http.authorizeRequests().anyRequest().permitAll();
    }
}
