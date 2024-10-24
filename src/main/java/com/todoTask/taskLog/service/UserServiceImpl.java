package com.todoTask.taskLog.service;

import com.todoTask.taskLog.entity.User;
import com.todoTask.taskLog.exception.UserNotFoundException;
import com.todoTask.taskLog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

   @Autowired
   private UserRepository userRepository;

    @Override
    public User findUserbyUserName(String userName) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByuserName(userName));

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
        newUser.setPassword(hashString(newUser.getPassword()));
        User insertedUser = userRepository.save(newUser);
        return insertedUser;
    }

    @Override
    public User updateUser(User existingUser) {
        User oldUser = findUserbyId(existingUser.getUserId());

        if(oldUser.getPassword() != existingUser.getPassword()) {
            existingUser.setPassword(hashString(existingUser.getPassword()));
        }

        User updatedUser = userRepository.save(existingUser);
        return updatedUser;
    }

    @Override
    public boolean deleteUser(String UserName) {
        User selectedUser = findUserbyUserName(UserName);
        userRepository.delete(selectedUser);
        return true;
    }

    @Override
    public boolean verifyUser(String Password, String UserName) {
        User logginUser = findUserbyUserName(UserName);
        String hashedInputtedPassword = hashString(Password);
        if(logginUser.getPassword() == hashedInputtedPassword) {
            return true;
        }
        return false;
    }

    public static String hashString(String input) {
        try {
            // Get a MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Perform the hashing
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array into a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


}
