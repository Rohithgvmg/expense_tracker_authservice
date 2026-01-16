package org.example.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.example.DTO.UserInfoDto;

import java.util.Map;

public class UserInfoSerializer implements Serializer<UserInfoDto> {


    @Override
    public byte[] serialize(String arg0,UserInfoDto arg1){
        byte[] retVal=null;
        ObjectMapper objectMapper=new ObjectMapper();
        try{
            retVal=objectMapper.writeValueAsString(arg1).getBytes();

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return retVal;
    }
}
