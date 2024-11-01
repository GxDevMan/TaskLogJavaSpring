package com.todoTask.taskLog.DTO;

import com.todoTask.taskLog.entity.UserAccount;
import org.springframework.stereotype.Service;

@Service
public class UserAccountMapperServiceImpl implements UserAccountDTOFactory {
    public UserAccountDTO toUserDTO(UserAccount userAccount){
        UserAccountDTO userAccountDTO = new UserAccountDTO(userAccount.getUserId(),userAccount.getUserName(), userAccount.getUserRole());
        return userAccountDTO;
    }

    @Override
    public UserAccountDTO returnUserProfile(UserAccount userAccount) {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setUserId(userAccount.getUserId());
        userAccountDTO.setUserName(userAccount.getUserName());
        userAccountDTO.setUserRole(userAccount.getUserRole());
        return userAccountDTO;
    }
}
