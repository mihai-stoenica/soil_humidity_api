package com.soil_humidity_api.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SoilStompController {
    private final SimpMessagingTemplate messagingTemplate;

    public SoilStompController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/device")
    @SendTo("/topic/device")
    public void handleDeviceMessage(String payload, SimpMessageHeaderAccessor headerAccessor) {

        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();

        assert sessionAttributes != null;
        String ownerUserId = (String) sessionAttributes.get("targetUserId");

        if (ownerUserId == null) {
            System.out.println("Error: No User ID found in session. Interceptor might have failed.");
            return;
        }

        System.out.println("Routing data to User ID: " + ownerUserId);

        messagingTemplate.convertAndSend("/topic/device/" + ownerUserId, payload);
    }
}

