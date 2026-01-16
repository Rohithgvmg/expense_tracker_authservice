package org.example.eventProducer;


import org.example.DTO.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
@Service
public class UserInfoProducer {

    private final  KafkaTemplate<String,UserInfoDto> kafkaTemplate;
    //kafka takes key and value pair, here that is of type String and UserInfoDto

    @Value("${spring.kafka.topic.name}")
    private String TOPIC_NAME;

    @Autowired
    UserInfoProducer(KafkaTemplate<String,UserInfoDto> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }

     public void sendEventToKafka(UserInfoDto userInfoDto){
         Message<UserInfoDto> message= MessageBuilder.withPayload(userInfoDto).setHeader(KafkaHeaders.TOPIC,TOPIC_NAME).build();
         kafkaTemplate.send(message);
     }

}

