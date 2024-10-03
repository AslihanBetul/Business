package com.businessapi.controller;

import com.businessapi.entity.ChatMessage;
import com.businessapi.repository.LiveSupportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class LiveSupportController {

    private LiveSupportRepository liveSupportRepository;

    @GetMapping("/messages/{senderId}")
    public List<ChatMessage> getMessagesByUser(@PathVariable Long senderId) {
        return liveSupportRepository.findBySenderIdOrderByTimestampAsc(senderId);
    }

    @GetMapping("/messages")
    public List<ChatMessage> getAllMessages() {
        return liveSupportRepository.findAllByOrderByTimestampAsc();
    }

}
