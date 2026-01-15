package org.example.eventProducer;


import org.example.DTO.UserInfoDto;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
@Service
public class UserInfoProducer {

   // private final kafkaTemplate<String, UserInfoDto> kafkaTemplate;

    private KafkaTemplate<String,UserInfoDto> kafkaTemplate;



}

