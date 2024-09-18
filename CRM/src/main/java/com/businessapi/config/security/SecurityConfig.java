package com.businessapi.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    //private final JwtTokenFilter jwtTokenFilter;
    //private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll()
                        .requestMatchers("dev/v1/customer/**").permitAll()
                        .requestMatchers("dev/v1/user/**").permitAll()
                        .requestMatchers("/**").permitAll()

                        .anyRequest().authenticated())
//                .addFilterBefore(jwtTokenFilter,
//                        UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable());
				/*.cors(httpSecurityCorsConfigurer ->
						      httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource));
*/		return httpSecurity.build();

    }
}
