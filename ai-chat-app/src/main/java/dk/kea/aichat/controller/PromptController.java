package dk.kea.aichat.controller;

import dk.kea.aichat.service.ChatGptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/prompt")
public class PromptController {
    private final ChatGptService chatGptService;

    public PromptController(ChatGptService chatGptService){
        this.chatGptService=chatGptService;
    }
    public record QueryRequest(String prompt){}

    @PostMapping
    public Mono<ResponseEntity<ChatGptService.ResponseDto>> ask(@RequestBody QueryRequest queryRequest){
        var response= chatGptService.getChatGptResponse(queryRequest.prompt);
        return response.map(ResponseEntity::ok);
    }
}
