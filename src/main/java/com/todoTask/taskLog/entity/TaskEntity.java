package com.todoTask.taskLog.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

@Entity
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long TaskId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount userAccount;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String taskName;

    @Column(columnDefinition = "TEXT")
    private String taskDescription;

    @Column(nullable = false)
    private Date taskDate;

    public TaskEntity() {
    }

    public TaskEntity(Long taskId, UserAccount userAccount, String taskName, String taskDescription) {
        TaskId = taskId;
        this.userAccount = userAccount;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        LocalDate currentDate = LocalDate.now();
        this.taskDate = Date.valueOf(currentDate);
    }

    public Long getTaskId() {
        return TaskId;
    }

    public void setTaskId(Long taskId) {
        TaskId = taskId;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Date getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(Date taskDate) {
        this.taskDate = taskDate;
    }
}
