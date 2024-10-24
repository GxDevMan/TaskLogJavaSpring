package com.todoTask.taskLog.controller;
import com.todoTask.taskLog.entity.UserAccount;
import com.todoTask.taskLog.exception.UserNotFoundException;
import com.todoTask.taskLog.service.UserService; import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping(path = "TaskAPI/v1/UserAccount/")
public class userController {

    private final UserService userService;

    @Autowired
    public userController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    @RequestMapping("userNameFindAcc/{user_name}/")
    public ResponseEntity<UserAccount> getUserbyUserName(@PathVariable("user_name") String user_name){
        try{
            return new ResponseEntity<UserAccount>(userService.findUserbyUserName(user_name), HttpStatus.OK);
             } catch(UserNotFoundException userNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "UserAccount was not found");
        }
    }

    @PostMapping
    @RequestMapping("newUserAcc/")
    public ResponseEntity<UserAccount> postNewUser(@RequestBody UserAccount newUserAccount){
        return new ResponseEntity<UserAccount>(userService.newUser(newUserAccount), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping("updateUserAcc/")
    public ResponseEntity<UserAccount> updateExistingUser(@RequestBody UserAccount existingUserAccount){
        return new ResponseEntity<UserAccount>(userService.updateUser(existingUserAccount), HttpStatus.OK);
    }


    @DeleteMapping
    @RequestMapping("deleteUserAcc/{user_name}/")
    public ResponseEntity<?> deleteUser(@PathVariable("user_name") String user_name){
       try {
          userService.deleteUser(user_name);
          return ResponseEntity.status(HttpStatus.OK).body("UserAccount was successfully deleted");
       } catch (UserNotFoundException userNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "UserAccount was not found");
       }
    }

}
