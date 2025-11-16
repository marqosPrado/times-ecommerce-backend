package br.com.marcosprado.timesbackend.controller.chat;

import br.com.marcosprado.timesbackend.dto.chat.ChatMessageRequest;
import br.com.marcosprado.timesbackend.dto.chat.ChatMessageResponse;
import br.com.marcosprado.timesbackend.dto.response.ApiResponse;
import br.com.marcosprado.timesbackend.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/message")
    public ResponseEntity<ApiResponse<ChatMessageResponse>> sendMessage(
            @Valid @RequestBody ChatMessageRequest request
    ) {
        ChatMessageResponse response = chatService.processMessage(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
