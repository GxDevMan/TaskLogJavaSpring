package com.todoTask.taskLog.service;

import com.todoTask.taskLog.entity.User;
import com.todoTask.taskLog.exception.UserNotFoundException;
import com.todoTask.taskLog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

   @Autowired
   private UserRepository userRepository;

    @Override
    public User findUserbyUserName(String userName) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByUser_Name(userName));

        if(userOptional.isPresent()){
            return userOptional.get();
        } else {
            throw new UserNotFoundException("User was not found by use of User Name");
        }
    }

    @Override
    public User findUserbyId(Long Id) {
        Optional<User> userOptional = userRepository.findById(Id);
        if(userOptional.isPresent()){
            return userOptional.get();
        } else {
            throw new UserNotFoundException("User was not found by use of Id");
        }
    }

    @Override
    public User newUser(User newUser) {
        User insertedUser = userRepository.save(newUser);
        return insertedUser;
    }

    @Override
    public User updateUser(User existingUser) {
        User updatedUser = userRepository.save(existingUser);
        return updatedUser;
    }

    @Override
    public boolean deleteUser(String UserName) {
        User selectedUser = findUserbyUserName(UserName);
        userRepository.delete(selectedUser);
        return true;
    }


}
