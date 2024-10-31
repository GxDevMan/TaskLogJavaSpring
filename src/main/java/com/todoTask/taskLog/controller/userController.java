package com.todoTask.taskLog.controller;

import com.todoTask.taskLog.DTO.UserAccountDTO;
import com.todoTask.taskLog.DTO.UserAccountMapperService;
import com.todoTask.taskLog.entity.UserAccount;
import com.todoTask.taskLog.exception.UserNotFoundException;
import com.todoTask.taskLog.service.SessionObjectMapperService;
import com.todoTask.taskLog.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.todoTask.taskLog.entity.roleEnum;

import java.util.Optional;

@RestController
@RequestMapping(path = "TaskAPI/v1/UserAccount/")
public class userController {

    private final UserService userService;
    private final SessionObjectMapperService sessionObjectMapperService;
    private final UserAccountMapperService userAccountMapperService;

    @Autowired
    public userController(UserService userService,
                          SessionObjectMapperService sessionObjectMapperService,
                          UserAccountMapperService userAccountMapperService) {
        this.sessionObjectMapperService = sessionObjectMapperService;
        this.userService = userService;
        this.userAccountMapperService = userAccountMapperService;
    }

    @GetMapping
    @RequestMapping(value = "userNameFindAcc/{user_name}/", method = RequestMethod.GET)
    public ResponseEntity<UserAccountDTO> getUserbyUserName(@PathVariable("user_name") String user_name) {
        try {
            UserAccount foundedUser = userService.findUserbyUserName(user_name);
            UserAccountDTO userfoundedDTO = userAccountMapperService.toUserDTO(foundedUser);
            return new ResponseEntity<UserAccountDTO>(userfoundedDTO, HttpStatus.OK);
        } catch (UserNotFoundException userNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "UserAccount was not found");
        }
    }

    @PostMapping
    @RequestMapping(value = "newUserAcc/", method = RequestMethod.POST)
    public ResponseEntity<UserAccount> postNewUser(@RequestBody UserAccount newUserAccount) {
        newUserAccount.setUserId(null);
        return new ResponseEntity<UserAccount>(userService.newUser(newUserAccount), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "userLoggedInInfo/", method = RequestMethod.GET)
    public ResponseEntity<?> getLoggedInUserInfo(HttpSession session) {

        byte[] loggedInUserBytes = (byte[]) session.getAttribute("userAcc");
        Optional<UserAccount> loggedInUserAccountOpt = sessionObjectMapperService.BytetoaccountObj(loggedInUserBytes);

        if (loggedInUserAccountOpt.isPresent()) {
            loggedInUserAccountOpt.get().setPassword(null);
            return new ResponseEntity<>(loggedInUserAccountOpt.get(), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error converting bytes to Account OBJ");
        }
    }

    @PostMapping
    @RequestMapping(value = "updateUserAcc/", method = RequestMethod.POST)
    public ResponseEntity<UserAccount> updateExistingUser(
            @RequestBody UserAccount accountToBeUpdated,
            HttpSession session) {

        byte[] loggedInUserBytes = (byte[]) session.getAttribute("userAcc");
        Optional<UserAccount> loggedInuserAccountOpt = sessionObjectMapperService.BytetoaccountObj(loggedInUserBytes);
        if (!loggedInuserAccountOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error converting bytes to Account OBJ");
        }

        if (loggedInuserAccountOpt.get().getUserRole().equals(roleEnum.ADMIN.toString())) {
            if (accountToBeUpdated.getUserName().equals(loggedInuserAccountOpt.get().getUserName())) {
                UserAccount updateUserAccount = userService.updateLoggedInUser(accountToBeUpdated, loggedInuserAccountOpt.get());

                Optional<byte[]> updateUserAccountBytes = sessionObjectMapperService.accountObjtoByte(updateUserAccount);

                if (!updateUserAccountBytes.isPresent()) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error converting Account OBJ to bytes");
                }
                session.setAttribute("userAcc", updateUserAccountBytes.get());
                return new ResponseEntity<>(updateUserAccount, HttpStatus.OK);
            }
            return new ResponseEntity<>(userService.updateUser(accountToBeUpdated), HttpStatus.OK);
        }

        UserAccount updateUserAccount = userService.updateLoggedInUser(accountToBeUpdated, loggedInuserAccountOpt.get());
        Optional<byte[]> updateUserAccountByte = sessionObjectMapperService.accountObjtoByte(updateUserAccount);

        if (!updateUserAccountByte.isPresent()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error converting Account OBJ to bytes");
        }
        session.setAttribute("userAcc", updateUserAccountByte.get());
        return new ResponseEntity<UserAccount>(updateUserAccount, HttpStatus.OK);
    }

    @DeleteMapping
    @RequestMapping(value = "deleteUserAcc/{user_name}/", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable("user_name") String user_name, HttpSession session) {
        Optional<UserAccount> loggedInUserOpt = sessionObjectMapperService.BytetoaccountObj((byte[]) session.getAttribute("userAcc"));
        if (!loggedInUserOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error converting bytes to OBJ");
        }

        if (loggedInUserOpt.get().getUserName().equals(user_name)) {
            userService.deleteUser(user_name);
            session.invalidate();
            SecurityContextHolder.clearContext();
            return ResponseEntity.status(HttpStatus.OK).body("you have deleted your own account");
        }

        if (loggedInUserOpt.get().getUserRole().equals(roleEnum.ADMIN.toString())) {
            userService.deleteUser(user_name);
            return ResponseEntity.status(HttpStatus.OK).body("UserAccount was successfully deleted");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Deletion");
    }
}
