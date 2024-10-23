package com.todoTask.taskLog.entity;

import jakarta.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long User_Id;

    @Column(columnDefinition = "TXT", nullable = false, unique = true)
    private String User_Name;

    @Column(columnDefinition = "TXT", nullable = false)
    private String Password;
    private String UserRole;

    public User(){

    }

    public User(Long user_Id, String user_Name, String password) {
        User_Id = user_Id;
        User_Name = user_Name;
        Password = password;
    }

    public Long getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(Long user_Id) {
        User_Id = user_Id;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
