package com.todoTask.taskLog.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.todoTask.taskLog.entity.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;

@Service
public class SessionObjectMapperService {

    private ObjectMapper mapper;

    @Autowired
    public SessionObjectMapperService(){
        this.mapper = new ObjectMapper();
    }


    public Optional<byte[]> accountObjtoByte(UserAccount userAccount) {
        byte[] serializedUserAcc;
        try {
            serializedUserAcc = mapper.writeValueAsBytes(userAccount);
        } catch (JsonProcessingException e){
            serializedUserAcc = null;
        }

        Optional<byte[]> bytesAccOpt = Optional.ofNullable(serializedUserAcc);
        return bytesAccOpt;
    }

    public Optional<UserAccount> BytetoaccountObj(byte[] incomingBytes) {
        UserAccount userAccount;
        try{
            userAccount = mapper.readValue(incomingBytes, UserAccount.class);
        } catch (IOException e){
            userAccount = null;
        }
        Optional<UserAccount> userAccountObjOpt = Optional.ofNullable(userAccount);
        return userAccountObjOpt;
    }
}
