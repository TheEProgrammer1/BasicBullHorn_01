package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{

    @Autowired
    private SSUserDetailService userDetailService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception
    {
        return new SSUserDetailService(userRepository);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.authorizeRequests().antMatchers("/","/h2-console/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')").antMatchers("/admin").access("hasRole('ROLE_ADMIN')")
                .anyRequest().authenticated().and().formLogin().loginPage("/login").permitAll().and().httpBasic();

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
       // auth.inMemoryAuthentication().withUser("dave").password("begreat").roles("ADMIN").and().withUser("user").password("password").roles("USER");
        auth.userDetailsService(userDetailsServiceBean());
    }
}
