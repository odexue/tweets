package com.odexue.tweets.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static String REGISTER_PATH = "/register";
    private static String REGISTER_ERROR_PATH = "/registerError";
    private static String LOGIN_PATH = "/signIn";
    private static String LOGIN_ERROR_PATH = "/signInError";
    private static String TWEETS_PATH = "/tweets";
    private static String ACTUATOR_PATH = "/actuator/health";
    private static String LOGOUT_PATH = "/logout";


    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(REGISTER_PATH, REGISTER_ERROR_PATH, ACTUATOR_PATH).permitAll()
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage(LOGIN_PATH)
                .failureUrl(LOGIN_ERROR_PATH)
                .defaultSuccessUrl(TWEETS_PATH, true)
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl(LOGIN_PATH)
                .logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_PATH));
    }
}
