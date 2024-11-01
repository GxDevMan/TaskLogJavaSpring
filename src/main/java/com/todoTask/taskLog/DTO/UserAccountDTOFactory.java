package com.todoTask.taskLog.DTO;

import com.todoTask.taskLog.entity.UserAccount;

public interface UserAccountDTOFactory {
    UserAccountDTO returnUserProfile(UserAccount userAccount);
}
