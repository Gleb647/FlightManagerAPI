package com.example.demo.Security;

import com.example.demo.Filter.CustomAuthenticationFilter;
import com.example.demo.Filter.CustomAuthorizationFilter;
import com.example.demo.Filter.SimpleCORSFilter;
import com.example.demo.Jwt.JwtServiceImpl;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private final UserDetailsService userDetailsService;

    @Autowired
    private final PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private final UserService userService;

    @Autowired
    private final JwtServiceImpl jwtService;

    public SecurityConfig(
            UserDetailsService userDetailsService,
            PasswordEncoder bCryptPasswordEncoder,
            UserService userService,
            JwtServiceImpl jwtService) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/users/add", "/token/refresh").permitAll();
        http.authorizeRequests().antMatchers("/role/add").permitAll();
        http.authorizeRequests().antMatchers("/flightinfo/get-flight-info-between/**").permitAll();
        http.authorizeRequests().antMatchers("/role/addtouser").permitAll();
        http.authorizeRequests().antMatchers("/login").permitAll();
        http.authorizeRequests().antMatchers("/signup").permitAll();
        http.authorizeRequests().antMatchers("/flights/get").permitAll();
        http.authorizeRequests().antMatchers("/flightinfo/get/**").permitAll();
        http.authorizeRequests().antMatchers(GET,"/buy-ticket/**").hasAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(POST, "/flights/add/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/flights/delete/**").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/flightinfo/delete/**").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/flights/change/**").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/flightinfo/change/**").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().anyRequest().authenticated();
        http.formLogin().permitAll();
        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean(), userService, jwtService));
        http.addFilterBefore(new CustomAuthorizationFilter(jwtService), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
}
