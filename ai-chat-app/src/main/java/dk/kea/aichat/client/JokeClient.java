package dk.kea.aichat.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class JokeClient {

    private final WebClient client;

    // Hvis du bruger jokeWebClient i stedet for weatherWebClient
    public JokeClient(@Qualifier("jokeWebClient") WebClient client) {
        this.client = client;
    }

    public record JokeResponse(
            boolean error,
            String type,
            String joke,
            String setup,
            String delivery
    ) {
        public String getFullJoke() {
            if ("single".equals(type)) {
                return joke;
            }
            return setup + "\n" + delivery;
        }
    }

    public Mono<JokeResponse> getRandomJoke() {
        return client.get()
                .uri("/joke/Any?safe-mode")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, r -> r.bodyToMono(String.class)
                        .map(msg -> new RuntimeException("Joke API Error: " + msg)))
                .bodyToMono(JokeResponse.class);
    }
}
