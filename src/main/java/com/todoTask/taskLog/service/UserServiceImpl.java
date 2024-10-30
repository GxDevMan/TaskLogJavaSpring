package com.todoTask.taskLog.service;

import com.todoTask.taskLog.entity.UserAccount;
import com.todoTask.taskLog.exception.BlankPasswordException;
import com.todoTask.taskLog.exception.PasswordMismatchException;
import com.todoTask.taskLog.exception.UserNotFoundException;
import com.todoTask.taskLog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordService passwordService;

    @Override
    public UserAccount findUserbyUserName(String userName) {
        Optional<UserAccount> userOptional = Optional.ofNullable(userRepository.findByuserName(userName));

        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UserNotFoundException("UserAccount was not found by use of UserAccount Name");
        }
    }

    @Override
    public UserAccount findUserbyId(Long Id) {
        Optional<UserAccount> userOptional = userRepository.findById(Id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UserNotFoundException("UserAccount was not found by use of Id");
        }
    }

    @Override
    public UserAccount newUser(UserAccount newUserAccount) {
        if (newUserAccount.getPassword().trim().equals(""))
            throw new BlankPasswordException("Password is Blank");

        String hashedPasswordWithSalt = passwordService.encode(newUserAccount.getPassword());
        newUserAccount.setPassword(hashedPasswordWithSalt);

        UserAccount insertedUserAccount = userRepository.save(newUserAccount);
        return insertedUserAccount;
    }

    @Override
    public UserAccount updateUser(UserAccount incomingChangeAcc) {
        UserAccount toUpdateUserAccount = findUserbyUserName(incomingChangeAcc.getUserName());
        Long userId = toUpdateUserAccount.getUserId();

        String hashedPasswordWithSalt = passwordService.encode(incomingChangeAcc.getPassword());
        incomingChangeAcc.setPassword(hashedPasswordWithSalt);

        toUpdateUserAccount = incomingChangeAcc;
        toUpdateUserAccount.setUserId(userId);

        UserAccount updatedUserAccount = userRepository.save(toUpdateUserAccount);
        return updatedUserAccount;
    }

    @Override
    public UserAccount updateLoggedInUser(UserAccount incomingChange, UserAccount loggedInUser) {
        loggedInUser.setUserName(incomingChange.getUserName());
        loggedInUser.setUserRole(incomingChange.getUserRole());
        loggedInUser.setPassword(passwordService.encode(incomingChange.getPassword()));
        loggedInUser.setUserId(loggedInUser.getUserId());
        UserAccount updatedUserAccount = userRepository.save(loggedInUser);
        return updatedUserAccount;
    }

    @Override
    public boolean deleteUser(String UserName) {
        UserAccount selectedUserAccount = findUserbyUserName(UserName);
        userRepository.delete(selectedUserAccount);
        return true;
    }

    @Override
    public UserAccount verifyUser(String Password, String UserName) {
        UserAccount logginUserAccount = findUserbyUserName(UserName);
        boolean check = passwordService.matches(Password, logginUserAccount.getPassword());
        if (check) {
            return logginUserAccount;
        }
        throw new PasswordMismatchException("Password Mismatch");
    }

}
