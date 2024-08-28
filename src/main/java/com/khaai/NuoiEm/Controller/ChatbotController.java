package com.khaai.NuoiEm.Controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin(origins = "*") 
public class ChatbotController {

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/chatbot/message")
    public ResponseEntity<String> sendMessage(@RequestBody Message message) {
        String rasaUrl = "http://localhost:5005/webhooks/rest/webhook";
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Message> request = new HttpEntity<>(message, headers);
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(rasaUrl, HttpMethod.POST, request, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    

    static class Message {
        private String sender;
        private String message;

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
