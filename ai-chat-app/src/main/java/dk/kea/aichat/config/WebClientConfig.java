package dk.kea.aichat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    WebClient.Builder webClientBuilder(){
        return WebClient.builder()
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * WebClient til JokeAPI
     * Kræver INGEN API key - helt gratis!
     */
    @Bean
    WebClient jokeWebClient(WebClient.Builder b) {
        return b.clone()
                .baseUrl("https://v2.jokeapi.dev")
                .build();
    }
    @Bean
    WebClient weatherWebClient(WebClient.Builder b,
                               @Value("${weather.api.key}") String apiKey,
                               @Value("${weather.api.baseUrl}") String baseUrl
    ) {
        if(apiKey==null || apiKey.isBlank()){
            throw new IllegalArgumentException("Weather API key must be provided in application.properties");
        }
        if(baseUrl==null || baseUrl.isBlank()){
            throw new IllegalArgumentException("Base URL must be provided in application.properties");
        }
        return b.clone()
                .baseUrl(baseUrl)
                .build();
    }
}
