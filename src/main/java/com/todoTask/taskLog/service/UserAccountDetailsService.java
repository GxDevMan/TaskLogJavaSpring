package com.todoTask.taskLog.service;

import com.todoTask.taskLog.UserAccountDetails;
import com.todoTask.taskLog.entity.UserAccount;
import com.todoTask.taskLog.exception.UserNotFoundException;
import com.todoTask.taskLog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserAccountDetailsService implements UserDetailsService {

    private final UserRepository userAccountRepository;

    @Autowired
    public UserAccountDetailsService(UserRepository userRepository){
        this.userAccountRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserAccount userAccount = userAccountRepository.findByuserName(username);
        return new UserAccountDetails(userAccount);
    }
}
