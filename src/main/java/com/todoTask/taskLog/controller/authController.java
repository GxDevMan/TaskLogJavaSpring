package com.todoTask.taskLog.controller;

import com.todoTask.taskLog.UserAccountDetails;
import com.todoTask.taskLog.entity.UserAccount;
import com.todoTask.taskLog.exception.PasswordMismatchException;
import com.todoTask.taskLog.exception.UserNotFoundException;
import com.todoTask.taskLog.service.PasswordService;
import com.todoTask.taskLog.service.SessionObjectMapperService;
import com.todoTask.taskLog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Profile({"development","deploy"})
@RestController
@RequestMapping(path = "TaskAPI/v1/")
public class authController {


    @Autowired
    private AuthenticationManager authenticationManager;

    private final PasswordService passwordService;
    private final UserService userService;
    private final SessionObjectMapperService sessionMapperService;

    @Autowired
    public authController(PasswordService passwordService, UserService userService, SessionObjectMapperService sessionMapperService) {
        this.passwordService = passwordService;
        this.userService = userService;
        this.sessionMapperService = sessionMapperService;

    }

    @Operation(summary = "\"userAcc\" pojo saved when logged in")
    @PostMapping
    @RequestMapping(value = "login/", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody UserAccount loginthisUser, HttpSession session, HttpServletResponse response) {
        UserAccount account;
        try {
            account = userService.verifyUser(loginthisUser.getPassword(), loginthisUser.getUserName());
            Optional<byte[]> byteAccOpt = sessionMapperService.accountObjtoByte(account);
            UserAccountDetails userAccountDetails = new UserAccountDetails(account);

            if(byteAccOpt.isPresent()) {
                session.setAttribute("userAcc", byteAccOpt.get());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(byteAccOpt.get(), userAccountDetails.getPassword(), userAccountDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            }
            else{
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error converting Account OBJ to bytes");
            }
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User was not found");
        } catch (PasswordMismatchException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Entered password is invalid");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Login Successful");
    }

    @DeleteMapping
    @RequestMapping(value = "logout/", method = RequestMethod.DELETE)
    public ResponseEntity<?> logout(HttpSession session, HttpServletResponse response) {
        if (session != null) {
            session.invalidate();
        }

        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.OK).body("Logout successful");
    }
}
