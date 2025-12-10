package com.soil_humidity_api.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class TesterController {

    private final SimpMessagingTemplate messagingTemplate;

    public TesterController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/send-data")
    public void receiveMessageAndRespond(String receivedData) {

        System.out.println("Received data from client: " + receivedData);

        // 1. Log the receipt
        String logMessage = "Server received: " + receivedData;

        // 2. Send a confirmation message back to all subscribed clients on a public topic
        // The client must subscribe to /topic/response to see this.
        String response = "SERVER ACK: Received your message: '" + receivedData + "'";

        messagingTemplate.convertAndSend("/topic/response", response);
    }
}
