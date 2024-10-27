package com.todoTask.taskLog.service;
import com.todoTask.taskLog.entity.UserAccount;

public interface UserService {
    UserAccount findUserbyUserName(String userName);
    UserAccount findUserbyId(Long Id);
    UserAccount newUser(UserAccount newUserAccount);
    UserAccount updateUser(UserAccount existingUserAccount);
    boolean deleteUser(String UserName);
    UserAccount verifyUser(String Password, String UserName);
}
