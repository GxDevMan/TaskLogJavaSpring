package com.todoTask.taskLog.controller;

import com.todoTask.taskLog.entity.UserAccount;
import com.todoTask.taskLog.exception.PasswordMismatchException;
import com.todoTask.taskLog.exception.UserNotFoundException;
import com.todoTask.taskLog.service.PasswordService;
import com.todoTask.taskLog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping(path = "TaskAPI/v1/")
public class authController {

    private final PasswordService passwordService;
    private final UserService userService;

    @Autowired
    public authController(PasswordService passwordService, UserService userService){
        this.passwordService = passwordService;
        this.userService = userService;
    }

    @Operation(summary = "\"userAcc\" pojo saved when logged in")
    @PostMapping
    @RequestMapping(value = "login/", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody UserAccount loginthisUser, HttpSession session, HttpServletResponse response) {
        UserAccount account;
        try {
            account = userService.verifyUser(loginthisUser.getPassword(), loginthisUser.getUserName());
        } catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User was not found");
        } catch (PasswordMismatchException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Entered password is invalid");
        }

        account.setPassword("");
        account.setPasswordSalt("");
        session.setAttribute("userAcc", account);
        return ResponseEntity.status(HttpStatus.OK).body("Login Successful");
    }

    @DeleteMapping
    @RequestMapping(value = "logout/", method = RequestMethod.DELETE)
    public ResponseEntity<?> logout(HttpSession session, HttpServletResponse response){
        if (session != null) {
            session.invalidate();
        }

        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.status(HttpStatus.OK).body("Logout successful");
    }
}
