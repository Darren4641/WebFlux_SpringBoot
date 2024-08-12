package com.webflux.security.config;

import com.webflux.common.propertise.CorsProperties;
import com.webflux.security.component.AuthTokenProvider;
import com.webflux.security.handler.AuthenticationEntryPointHandler;
import com.webflux.security.handler.TokenAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@EnableWebFluxSecurity
@Slf4j
public class SecurityConfig {
    private final CorsProperties corsProperties;
    private final AuthenticationEntryPointHandler authenticationEntryPointHandler;
    private final TokenAccessDeniedHandler tokenAccessDeniedHandler;
    private final AuthTokenProvider authTokenProvider;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .csrf(csrf -> csrf.disable())
            .formLogin(form -> form.disable())
            .httpBasic(httpBasicSpec -> httpBasicSpec.disable())
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .exceptionHandling(ex -> {
                //인증 되지 않은 사용자 접근시 핸들러 -401
                ex.authenticationEntryPoint(authenticationEntryPointHandler);
                //인증은 되었으나, 권한이 없는경우 - 403
                ex.accessDeniedHandler(tokenAccessDeniedHandler);
            })
            .authorizeExchange(auth -> {
                auth.pathMatchers("/actuator/**").permitAll();
                auth.pathMatchers("/v1/**").authenticated();
                auth.anyExchange().permitAll();
            })
                .addFilterBefore()



    }

    @Bean
    public AuthenticationWebFilter authenticationWebFilter() {
        ReactiveAuthenticationManager authenticationManager = new ReactiveAuthenticationManager() {
            @Override
            public Mono<Authentication> authenticate(Authentication authentication) {
                return Mono.just(authentication);
            }
        };// == Mono::just;

        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);

        ServerAuthenticationConverter serverAuthenticationConverter = new ServerAuthenticationConverter() {

            @Override
            public Mono<Authentication> convert(ServerWebExchange exchange) {
                String token =
                return null;
            }
        }

        authenticationWebFilter.setServerAuthenticationConverter(serverAuthenticationConverter());
        return authenticationWebFilter;
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders().split(",")));
        corsConfig.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods().split(",")));
        corsConfig.setAllowedOrigins(Arrays.asList(corsProperties.getAllowedOrigins().split(",")));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(corsConfig.getMaxAge());

        corsConfigSource.registerCorsConfiguration("/**", corsConfig);
        return corsConfigSource;
    }
}
