package com.todoTask.taskLog.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class UserAccount {

    @JsonProperty("userId")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @JsonProperty("userName")
    @Column(unique = true, nullable = false)
    private String userName;

    @JsonProperty("password")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String Password;

    @JsonProperty("userRole")
    @Column(nullable = false)
    private String userRole;

    @JsonIgnore
    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.REMOVE)
    private List<TaskEntity> accountTasks;


    public UserAccount(){

    }

    public UserAccount(Long user_Id, String user_Name, String password) {
        userId = user_Id;
        userName = user_Name;
        Password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        if(userRole.toUpperCase().equals(roleEnum.ADMIN.name())){
            this.userRole = roleEnum.ADMIN.name();
        }
        else {
            this.userRole = roleEnum.USERACC.name();
        }
    }

    public List<TaskEntity> getAccountTasks() {
        return accountTasks;
    }

    public void setAccountTasks(List<TaskEntity> accountTasks) {
        this.accountTasks = accountTasks;
    }
}
