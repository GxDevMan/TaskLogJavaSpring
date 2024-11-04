package com.todoTask.taskLog;

import com.todoTask.taskLog.entity.UserAccount;
import com.todoTask.taskLog.exception.UserNotFoundException;
import com.todoTask.taskLog.service.PasswordService;
import com.todoTask.taskLog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import com.todoTask.taskLog.entity.roleEnum;

@Component
public class UserAccountInitializer {
    private final UserService userService;
    private final PasswordService passwordService;

    @Autowired
    public UserAccountInitializer(UserService userService, PasswordService passwordService) {
        this.userService = userService;
        this.passwordService = passwordService;
    }

    @Bean
    public CommandLineRunner initializeAdminUser() {
        return args -> {
            String defaultUsername = System.getenv("ADMIN_USERNAME");
            String defaultPassword = System.getenv("ADMIN_PASSWORD");

            String adminUsername = (defaultUsername != null) ? defaultUsername : "admindefault";
            String adminPassword = (defaultPassword != null) ? defaultPassword : "adminpassword";

            try {
                userService.findUserbyUserName(adminUsername);
                System.out.println("Default Account Already Exists");
            } catch (UserNotFoundException e){
                UserAccount admin = new UserAccount();
                admin.setUserName(adminUsername);
                admin.setPassword(passwordService.encode(adminPassword));
                admin.setUserRole(roleEnum.ADMIN.name());
                userService.newUser(admin);

                System.out.println(String.format("username: %s, password: %s default account created",adminUsername,adminPassword));
            } catch (Exception e) {

                System.out.println("Error Creating ADMIN ACCOUNT");
                System.out.println("User may already exist in the database");
            }
        };
    }


}
