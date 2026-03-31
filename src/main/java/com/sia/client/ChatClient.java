package com.sia.client;

import com.sia.dto.MessageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "kufar-chat-service", url = "${services.chat-service.url}")
public interface ChatClient {

    @GetMapping("/api/messages/ad/{adId}")
    List<MessageDTO> getMessageByAdId(@PathVariable Integer adId);
}
