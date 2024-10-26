package com.todoTask.taskLog.service;
import com.todoTask.taskLog.entity.TaskEntity;
import com.todoTask.taskLog.entity.UserAccount;
import java.util.List;

public interface TaskService {
    List<TaskEntity> findtasksbyUserAccount(UserAccount userAccount);
    TaskEntity findbyTaskId(Long taskId);
    TaskEntity newTask(TaskEntity newTask);
    TaskEntity updateTask(TaskEntity updateTask);
    boolean deleteTask(TaskEntity deleteTask);
}
