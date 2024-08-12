package com.webflux.security.filter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.webflux.common.APIResponse;
import com.webflux.common.Code;
import com.webflux.security.component.AuthTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class TokenAuthenticationWebFilter extends AuthenticationWebFilter {
    public TokenAuthenticationWebFilter(ReactiveAuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @RequiredArgsConstructor
    public static class TokenAuthenticationConverter implements ServerAuthenticationConverter {
        private final AuthTokenProvider authTokenProvider;

        @Override
        public Mono<Authentication> convert(ServerWebExchange exchange) {
            try {
                String token = authTokenProvider.getAccessToken(exchange.getRequest());
                if(authTokenProvider.validateToken(token)) {
                    return Mono.justOrEmpty(authTokenProvider.getAuthentication(token));
                }
            } catch (Exception e) {
                handleException(exchange.getResponse());
            }


            return Mono.empty();
        }

        private void handleException(ServerHttpResponse response) {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            response.setStatusCode(HttpStatusCode.valueOf(Code.UNAUTHORIZED.getCode()));
            APIResponse<?> apiResponse = new APIResponse(Code.UNAUTHORIZED);

            try {
                byte[] errorByte = new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .writeValueAsBytes(apiResponse);
                DataBuffer dataBuffer = response.bufferFactory().wrap(errorByte);
                response.writeWith(Mono.just(dataBuffer));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }

        }
    }

}
