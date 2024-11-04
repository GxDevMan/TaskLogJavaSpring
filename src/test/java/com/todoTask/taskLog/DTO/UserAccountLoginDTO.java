package com.todoTask.taskLog.DTO;

public class UserAccountLoginDTO {
    private String userName;
    private String password;

    public UserAccountLoginDTO() {
    }

    public UserAccountLoginDTO(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
