package com.todoTask.taskLog.service;
import com.todoTask.taskLog.entity.User;

public interface UserService {
    User findUserbyUserName(String userName);
    User findUserbyId(Long Id);
    User newUser(User newUser);
    User updateUser(User existingUser);
    boolean deleteUser(String UserName);
}
