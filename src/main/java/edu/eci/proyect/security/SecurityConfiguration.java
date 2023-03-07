package edu.eci.proyect.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity( securedEnabled = true, jsr250Enabled = true, prePostEnabled = true )
public class SecurityConfiguration {
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfiguration(@Autowired JwtRequestFilter jwtRequestFilter )
    {
        this.jwtRequestFilter = jwtRequestFilter;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
         http.addFilterBefore(jwtRequestFilter, BasicAuthenticationFilter.class).cors().and().csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers( HttpMethod.POST,"/v1/auth" ).permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS );
         return http.build();
    };
}
