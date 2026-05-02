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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] WHITE_LIST_URL = {
            "/authenticate", 
            "/refreshToken",
            "/auth/forgot-password",
            "/auth/reset-password",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(request ->
                        request.requestMatchers(WHITE_LIST_URL).permitAll()
                                .requestMatchers("/faculty/**", "/department/**", "/semester/**", "/classroom/**").hasAuthority("ADMIN")
                                .requestMatchers("/announcement/all").hasAnyAuthority("ADMIN", "TEACHER", "STUDENT")
                                .requestMatchers("/announcement/**").hasAnyAuthority("ADMIN", "TEACHER") // Öğretmen de duyuru ekleyebilsin
                                .requestMatchers("/teacher/**", "/course/**", "/coursesection/**").hasAnyAuthority("ADMIN", "TEACHER")
                                .requestMatchers("/student/**").hasAnyAuthority("ADMIN", "TEACHER", "STUDENT")
                                .requestMatchers("/grade/**").hasAnyAuthority("ADMIN", "TEACHER", "STUDENT")
                                .requestMatchers("/exam/**").hasAnyAuthority("ADMIN", "TEACHER") // Sınav yönetimi
                                .requestMatchers("/assignment/all").hasAnyAuthority("ADMIN", "TEACHER", "STUDENT") // Ödevleri listeleme izni öğrenciye de verildi
                                .requestMatchers("/assignment/**").hasAnyAuthority("ADMIN", "TEACHER") // Ödev ekleme/güncelleme/silme sadece ADMIN ve TEACHER
                                .requestMatchers("/enrollment/**").hasAnyAuthority("ADMIN", "STUDENT")
                                .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
