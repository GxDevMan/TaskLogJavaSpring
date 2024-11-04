package com.todoTask.taskLog.DTO;

import com.todoTask.taskLog.entity.UserAccount;

public class UserAccountLoginDTOMapper implements UserAccountTestDTOFactory{

    @Override
    public UserAccountLoginDTO convertToUserLoginDTO(UserAccount userAccount) {
        UserAccountLoginDTO userAccountLoginDTO = new UserAccountLoginDTO();
        userAccountLoginDTO.setUserName(userAccount.getUserName());
        userAccountLoginDTO.setPassword(userAccount.getPassword());
        return userAccountLoginDTO;
    }
}
