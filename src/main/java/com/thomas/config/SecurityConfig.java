package com.thomas.config;

import com.thomas.modules.security.composer.FilterComposer;
import com.thomas.modules.security.configurer.FiltersConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Order(2)
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final FilterComposer filterComposer;
    public final static String[] permitted = {
            "/storage/**",

            "/api/v**/login",
            "/api/v**/auth",
            "/api/v**/refresh",
            "/api/v**/hello-world",

            "/v3/api-docs/**",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/swagger-ui/**",
            "/swagger-ui/index.html",
            "/swagger-ui/index.html/**",

            "/ws/**",
            "/app/**"
    };
    public final static List<String> permittedList = Arrays.asList(permitted);
    @Autowired
    public SecurityConfig(FilterComposer filterComposer) {
        this.filterComposer = filterComposer;
    }
    @Bean
    @Order(1)
    public SecurityFilterChain apiV2(HttpSecurity http) throws Exception {
        return apiHttpSecurity(http, "/api/v2/**").build();
    }
    private HttpSecurity apiHttpSecurity(HttpSecurity http, String securityMatcher) throws Exception {
        http
                .securityMatcher(securityMatcher)
                .cors(withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(m -> m.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(permitted).permitAll()
                        .anyRequest().authenticated()
                )
                .apply(new FiltersConfigurer(filterComposer));
        return http;
    }

    @Bean
    @Order(2)
    public SecurityFilterChain base(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(m -> m.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(permitted).permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
