package com.cybertitans.CyberTitans.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.cybertitans.CyberTitans.security.JwtAuthenticationEntryPoint;
import com.cybertitans.CyberTitans.security.JwtAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SecurityConfiguration {

    private UserDetailsService userDetailsService;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    public SecurityConfiguration(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAuthenticationFilter jwtAuthenticationFilter, UserDetailsService userDetailsService) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//        http.addFilterBefore(authCorsFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http
                .csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                      .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/swagger-config").permitAll()
                        .requestMatchers("/swagger-ui/").permitAll()
                        .requestMatchers("/v3/api-docs").permitAll()
                        .requestMatchers("/api/v1/auth/register").permitAll()
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .requestMatchers("/api/v1/admin").hasAnyAuthority("ADMIN", "SUPER_ADMIN")
                        .requestMatchers("/api/v1/user/getAllProducts").permitAll()
                        .requestMatchers("/api/v1/user/getAllProducts/**").permitAll()
                        .requestMatchers("/api/v1/user/getSingleProduct/**").permitAll()
                        .requestMatchers("/api/v1/user/**").permitAll()
                        .requestMatchers("/api/v1/cart/**").hasAuthority("USER")
                        .requestMatchers("/checkout/**").hasAuthority("USER")
                        .requestMatchers("/api/v1/admin/**").hasAnyAuthority("ADMIN", "SUPER_ADMIN")
                        .requestMatchers("/api/v1/reviews/product-review/**").hasAuthority("USER")
                        .requestMatchers("/api/v1/reviews/application-review").hasAuthority("USER")
                        .requestMatchers("/api/v1/reviews/single-product-review/**").permitAll()
                        .requestMatchers("/api/v1/super-admin/**").hasAuthority("SUPER_ADMIN")
                        .requestMatchers("/api/v1/wishlist/**").hasAuthority("USER")
                        .requestMatchers("/audit").hasAnyAuthority("SUPER_ADMIN", "ADMIN")
                        .requestMatchers("/v2/api-docs",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/swagger-ui.html",
                                "/webjars/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**").permitAll()
                        .requestMatchers("/api/v1/auth/oauthsuccess").permitAll()
                        .requestMatchers("/api/v1/auth/oauth2/**").permitAll()
                        .anyRequest()
                        .authenticated()

                ).oauth2Login(oauth2 -> oauth2.loginPage("/api/v1/auth/oauth2").authorizationEndpoint(end->end.baseUri("/api/v1/auth/oauth2")
                        ).defaultSuccessUrl("/api/v1/auth/oauthsuccess").permitAll()
                ).exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .build();
    }

}
