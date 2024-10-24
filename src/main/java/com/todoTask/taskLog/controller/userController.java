package com.todoTask.taskLog.controller;
import com.todoTask.taskLog.entity.User;
import com.todoTask.taskLog.exception.UserNotFoundException;
import com.todoTask.taskLog.service.UserService; import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping(path = "TaskAPI/v1/User/")
public class userController {

    private final UserService userService;

    @Autowired
    public userController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    @RequestMapping("userNameFind/{user_name}")
    public ResponseEntity<User> getUserbyUserName(@PathVariable("user_name") String user_name){
        try{
            return new ResponseEntity<User>(userService.findUserbyUserName(user_name), HttpStatus.OK);
             } catch(UserNotFoundException userNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found");
        }
    }

    @PostMapping
    @RequestMapping("newUser/")
    public ResponseEntity<User> postNewUser(@RequestBody User newUser){
        return new ResponseEntity<User>(userService.newUser(newUser), HttpStatus.OK);
    }

    @DeleteMapping
    @RequestMapping("deleteUser/{user_name}")
    public ResponseEntity<?> deleteUser(@PathVariable("user_name") String user_name){
       try {
          userService.deleteUser(user_name);
          return ResponseEntity.status(HttpStatus.OK).body("User was successfully deleted");
       } catch (UserNotFoundException userNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found");
       }
    }

}
