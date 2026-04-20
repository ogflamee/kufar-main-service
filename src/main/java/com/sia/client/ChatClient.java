package com.sia.client;

import com.sia.dto.MessageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "kufar-chat-service", url = "${services.chat-service.url}")
public interface ChatClient {

    @PostMapping("/api/messages")
    void sendMessage(@RequestBody MessageDTO dto);

    @GetMapping("/api/messages/ad/{adId}")
    List<MessageDTO> getMessageByAdId(@PathVariable Integer adId);

    @GetMapping("/api/messages/chat/by-ad")
    List<MessageDTO> getChatByAdAndUsers(@RequestParam Integer adId,
                                         @RequestParam Integer firstUserId,
                                         @RequestParam Integer secondUserId);
}
