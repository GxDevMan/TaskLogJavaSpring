package com.todoTask.taskLog.DTO;

import com.todoTask.taskLog.entity.UserAccount;
import org.springframework.stereotype.Service;

@Service
public class UserAccountMapperService {
    public UserAccountDTO toUserDTO(UserAccount userAccount){
        UserAccountDTO userAccountDTO = new UserAccountDTO(userAccount.getUserId(),userAccount.getUserName(), userAccount.getUserRole());
        return userAccountDTO;
    }
}
