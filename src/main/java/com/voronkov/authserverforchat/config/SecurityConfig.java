package com.voronkov.authserverforchat.config;

import com.voronkov.authserverforchat.repository.PersonRepository;
import com.voronkov.authserverforchat.repository.RoleRepository;
import com.voronkov.authserverforchat.security.JwtFilter;
import com.voronkov.authserverforchat.security.JwtProvider;
import com.voronkov.authserverforchat.security.JwtProviderImpl;
import com.voronkov.authserverforchat.security.JwtUserDetailsService;
import com.voronkov.authserverforchat.service.PersonService;
import com.voronkov.authserverforchat.service.impl.PersonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableWebSecurity
@Configuration
// Как таковая Security не нужна, фильтр нужно будет перенести на уровень WebSocketServer
public class SecurityConfig {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtProvider(), userDetailsService());
    }

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProviderImpl();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new JwtUserDetailsService(personService());
    }

    @Bean
    public PersonService personService() {
        return new PersonServiceImpl(personRepository, roleRepository, passwordEncoder());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests(auth ->
                        auth
                                .antMatchers("/register", "/auth", "/refresh").permitAll()
                                .antMatchers("/user/all").hasRole("USER")
                                .anyRequest()
                                .authenticated())
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                // Spring Security should completely ignore URLs starting with /resources/
                .antMatchers("/resources/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
