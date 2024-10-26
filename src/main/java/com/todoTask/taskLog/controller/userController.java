package com.todoTask.taskLog.controller;
import com.todoTask.taskLog.entity.UserAccount;
import com.todoTask.taskLog.exception.UserNotFoundException;
import com.todoTask.taskLog.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "TaskAPI/v1/UserAccount/")
public class userController {

    private final UserService userService;

    private final String admin = "ADMIN";
    private final String user = "USER";

    @Autowired
    public userController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    @RequestMapping(value = "userNameFindAcc/{user_name}/", method = RequestMethod.GET)
    public ResponseEntity<UserAccount> getUserbyUserName(@PathVariable("user_name") String user_name){
        try{
            return new ResponseEntity<UserAccount>(userService.findUserbyUserName(user_name), HttpStatus.OK);
             } catch(UserNotFoundException userNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "UserAccount was not found");
        }
    }

    @PostMapping
    @RequestMapping(value = "newUserAcc/", method =  RequestMethod.POST)
    public ResponseEntity<UserAccount> postNewUser(@RequestBody UserAccount newUserAccount){
        return new ResponseEntity<UserAccount>(userService.newUser(newUserAccount), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "updateUserAcc/", method = RequestMethod.POST)
    public ResponseEntity<UserAccount> updateExistingUser(
            @RequestBody UserAccount existingUserAccount,
            HttpSession session){

        String role = (String) session.getAttribute("role");
        if(role.equals(this.admin)){
            return new ResponseEntity<UserAccount>(userService.updateUser(existingUserAccount), HttpStatus.OK);
        }
        existingUserAccount.setUserName((String) session.getAttribute("username"));
        return new ResponseEntity<UserAccount>(userService.updateUser(existingUserAccount), HttpStatus.OK);
    }


    @DeleteMapping
    @RequestMapping(value = "deleteUserAcc/{user_name}/", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable("user_name") String user_name){
       try {
          userService.deleteUser(user_name);
          return ResponseEntity.status(HttpStatus.OK).body("UserAccount was successfully deleted");
       } catch (UserNotFoundException userNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "UserAccount was not found");
       }
    }

}
