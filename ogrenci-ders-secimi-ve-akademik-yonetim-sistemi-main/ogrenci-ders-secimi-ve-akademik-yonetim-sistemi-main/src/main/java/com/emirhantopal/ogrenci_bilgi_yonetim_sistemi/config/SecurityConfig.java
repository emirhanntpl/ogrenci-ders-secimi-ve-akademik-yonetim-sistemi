package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.config;


import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


    private static final String[] WHITE_LIST_URL = {"/auth/**", "/register", "/registerAdmin", "/authenticate", "/refreshToken"};
    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(request ->
                        request.requestMatchers(WHITE_LIST_URL)
                                .permitAll()

                                .requestMatchers("/faculty/**", "/department/**", "/semester/**", "/classroom/**")
                                .hasRole("ADMIN")

                                .requestMatchers("/teacher/**", "/course/**", "/coursesection/**", "/announcement/**")
                                .hasAnyRole("ADMIN", "TEACHER")


                                .requestMatchers("/grade/**")
                                .hasAnyRole("ADMIN", "TEACHER", "STUDENT")

                                .requestMatchers("/enrollment/**")
                                .hasAnyRole("ADMIN", "STUDENT")
                                .anyRequest()
                                .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}