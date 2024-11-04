package com.todoTask.taskLog.DTO;

import com.todoTask.taskLog.entity.UserAccount;

public interface UserAccountTestDTOFactory {
    UserAccountLoginDTO convertToUserLoginDTO(UserAccount userAccount);
}
