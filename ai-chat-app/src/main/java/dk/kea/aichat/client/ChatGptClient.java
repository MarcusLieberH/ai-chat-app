/*package dk.kea.aichat.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class ChatGptClient {
    private final WebClient client;

    public ChatGptClient( WebClient webClient) {
        this.client = webClient;
    }
    public record ChatResponse(
            String id,
            List<Choice> choices
    ){}
    public record Choice(
    Message message,
    int index
    ){}

    public record Message(
            String role,
            String content
    ){}
    public Mono<ChatResponse> getChatResponse(String prompt){
        Map<String,Object> requestBody = Map.of(
                "model","gpt-4o-mini",
                "messages",List.of(
                        Map.of(
                                "role","user",
                                "content",prompt
                        )
                ),
                "max_tokens",500);

        return client.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(s -> s.value() == 400, r -> r.bodyToMono(String.class)
                        .map(msg -> new IllegalArgumentException("OpenAI 400: " + msg)))
                .onStatus(s -> s.value() == 401, r -> r.bodyToMono(String.class)
                        .map(msg -> new IllegalArgumentException("OpenAI 401 Unauthorized: " + msg)))
                .onStatus(HttpStatusCode::isError, r -> r.bodyToMono(String.class)
                        .map(msg -> new RuntimeException("OpenAI Error: " + msg)))
                .bodyToMono(ChatResponse.class);
    }
}



 */