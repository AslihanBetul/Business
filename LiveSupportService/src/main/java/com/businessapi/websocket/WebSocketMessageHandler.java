package com.businessapi.websocket;


import com.businessapi.dto.request.ChatMessageDTO;
import com.businessapi.entity.ChatMessage;
import com.businessapi.entity.enums.ERole;
import com.businessapi.repository.LiveSupportRepository;
import com.businessapi.util.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class WebSocketMessageHandler {

    private final SimpMessagingTemplate messagingTemplate;

    private final LiveSupportRepository liveSupportRepository;

    private final JwtTokenManager jwtTokenManager;


    @MessageMapping("/sendMessage")
    public void receiveMessage(@Payload ChatMessageDTO chatMessageDTO) {
        Optional<Long> authId = jwtTokenManager.getIdFromToken(chatMessageDTO.getToken());

        if (!authId.isPresent()) {
            throw new IllegalArgumentException("Invalid token");
        }

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSenderId(authId.get());
        chatMessage.setMessage(chatMessageDTO.getMessage());
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage.setSenderRole(ERole.valueOf(chatMessageDTO.getSenderRole()));

        liveSupportRepository.save(chatMessage);
        messagingTemplate.convertAndSend("/topic/messages", chatMessage);
    }

}