package com.soil_humidity_api.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class TesterController {

    static public class MessagePayloadDTO {

        private Long messageId; // Matches 'messageId' (you're sending Date.now())
        private String data;    // Matches 'data'

        // You must include standard getters and setters (or use Lombok)
        // for Jackson to work!

        public Long getMessageId() {
            return messageId;
        }

        public void setMessageId(Long messageId) {
            this.messageId = messageId;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "MessagePayloadDTO{" +
                    "messageId=" + messageId +
                    ", data='" + data + '\'' +
                    '}';
        }
    }

    private final SimpMessagingTemplate messagingTemplate;

    public TesterController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/send-data")
    // Change the parameter type from String to your new DTO
    public void receiveMessageAndRespond(MessagePayloadDTO receivedPayload) {

        System.out.println("Received data from client: " + receivedPayload.toString());

        // You can now access fields directly:
        // Long id = receivedPayload.getMessageId();
        // String text = receivedPayload.getData();

        // 2. Send a confirmation message back...
        String response = "SERVER ACK: Received ID: " + receivedPayload.getMessageId();

        messagingTemplate.convertAndSend("/topic/response", response);
    }
}
