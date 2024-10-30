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
import com.todoTask.taskLog.entity.roleEnum;

@RestController
@RequestMapping(path = "TaskAPI/v1/UserAccount/")
public class userController {

    private final UserService userService;

    @Autowired
    public userController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @RequestMapping(value = "userNameFindAcc/{user_name}/", method = RequestMethod.GET)
    public ResponseEntity<UserAccount> getUserbyUserName(@PathVariable("user_name") String user_name) {
        try {
            UserAccount foundedUser = userService.findUserbyUserName(user_name);
            foundedUser.setPassword(null);
            return new ResponseEntity<UserAccount>(foundedUser, HttpStatus.OK);
        } catch (UserNotFoundException userNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "UserAccount was not found");
        }
    }

    @PostMapping
    @RequestMapping(value = "newUserAcc/", method = RequestMethod.POST)
    public ResponseEntity<UserAccount> postNewUser(@RequestBody UserAccount newUserAccount, HttpSession session) {
        newUserAccount.setUserId(null);
        return new ResponseEntity<UserAccount>(userService.newUser(newUserAccount), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "userLoggedInInfo/", method = RequestMethod.GET)
    public ResponseEntity<UserAccount> getLoggedInUserInfo(HttpSession session){
        UserAccount loggedInUser = (UserAccount) session.getAttribute("userAcc");
        loggedInUser.setPassword(null);
        return new ResponseEntity<UserAccount>(loggedInUser, HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "updateUserAcc/", method = RequestMethod.POST)
    public ResponseEntity<UserAccount> updateExistingUser(
            @RequestBody UserAccount accountToBeUpdated,
            HttpSession session) {

        UserAccount loggedInUser = (UserAccount) session.getAttribute("userAcc");
        if (loggedInUser.getUserRole().equals(roleEnum.ADMIN.toString())) {
            if(accountToBeUpdated.getUserName().equals(loggedInUser.getUserName())){
                UserAccount updateUserAccount = userService.updateLoggedInUser(accountToBeUpdated,loggedInUser);
                session.setAttribute("userAcc", updateUserAccount);
                return new ResponseEntity<>(updateUserAccount, HttpStatus.OK);
            }
            return new ResponseEntity<UserAccount>(userService.updateUser(accountToBeUpdated), HttpStatus.OK);
        }

        UserAccount updateUserAccount = userService.updateLoggedInUser(accountToBeUpdated,loggedInUser);
        session.setAttribute("userAcc", updateUserAccount);
        return new ResponseEntity<UserAccount>(userService.updateUser(updateUserAccount), HttpStatus.OK);
    }

    @DeleteMapping
    @RequestMapping(value = "deleteUserAcc/{user_name}/", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable("user_name") String user_name, HttpSession session) {
        try {
            UserAccount loggedInUser = (UserAccount) session.getAttribute("userAcc");

            if(loggedInUser.getUserName().equals(user_name)){
                userService.deleteUser(user_name);
                session.invalidate();
                return ResponseEntity.status(HttpStatus.OK).body("you have deleted your own account");
            }

            if(loggedInUser.getUserRole().equals(roleEnum.ADMIN.toString())){
                userService.deleteUser(user_name);
                return ResponseEntity.status(HttpStatus.OK).body("UserAccount was successfully deleted");
            }

            userService.deleteUser(loggedInUser.getUserName());
            return ResponseEntity.status(HttpStatus.OK).body("UserAccount:User Role, that was logged in was successfully deleted");
        } catch (UserNotFoundException userNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "UserAccount was not found");
        }
    }

}
