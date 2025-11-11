package dk.kea.aichat.client;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WeatherClient {

    private final WebClient client;
    private final String apiKey;


    public WeatherClient(
            @Qualifier("weatherWebClient") WebClient client,
            @Value("${weather.api.key}") String apiKey
    ) {
        this.client = client;
        this.apiKey = apiKey;
    }

    // Response structure fra OpenWeatherMap
    public record WeatherResponse(
            Main main,
            Weather[] weather,
            String name
    ) {}

    public record Main(
            double temp,
            double feels_like,
            int humidity
    ) {}

    public record Weather(
            String main,
            String description
    ) {}

    /**
     * Hent vejrdata for en given by
     */
    public Mono<WeatherResponse> getWeather(String city) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather")
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(s -> s.value() == 404, r -> r.bodyToMono(String.class)
                        .map(msg -> new IllegalArgumentException("City not found: " + city)))
                .onStatus(s -> s.value() == 401, r -> r.bodyToMono(String.class)
                        .map(msg -> new IllegalArgumentException("Invalid API key")))
                .onStatus(HttpStatusCode::isError, r -> r.bodyToMono(String.class)
                        .map(msg -> new RuntimeException("Weather API Error: " + msg)))
                .bodyToMono(WeatherResponse.class);
    }
}
