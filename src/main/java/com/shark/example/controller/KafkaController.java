package com.shark.example.controller;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/kafka")
@RestController
public class KafkaController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    //producer
    @PostMapping("/producer")
    public String producer(@RequestBody String message) {
        System.out.println("message: " + message);
        for(int i = 0; i < 100; i ++) {
            kafkaTemplate.sendDefault(message + i);
        }
        return message;
    }

    //consumer
    @KafkaListener(topics = "${kafka.topic.streams}")
    public void listen(List<String> messageList, Acknowledgment acknowledgment) {
        int i = 0;
        for(String message: messageList) {
            System.out.println("listen message length: " + message);
            i ++;
        }
        System.out.println("message count: " + i);
        acknowledgment.acknowledge();
    }
}
