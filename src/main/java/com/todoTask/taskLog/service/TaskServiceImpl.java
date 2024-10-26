package com.todoTask.taskLog.service;

import com.todoTask.taskLog.entity.TaskEntity;
import com.todoTask.taskLog.entity.UserAccount;
import com.todoTask.taskLog.exception.TaskNotFoundException;
import com.todoTask.taskLog.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;


    @Override
    public List<TaskEntity> findtasksbyUserAccount(UserAccount userAccount) {
        List<TaskEntity> tasksofThisUser = taskRepository.findByuserAccount(userAccount);
        return tasksofThisUser;
    }

    @Override
    public TaskEntity findbyTaskId(Long taskId) {
        Optional<TaskEntity> taskEntityOption = taskRepository.findById(taskId);

        if(taskEntityOption.isPresent()){
            return taskEntityOption.get();
        }
        throw new TaskNotFoundException("Task not found by Id");
    }

    @Override
    public TaskEntity newTask(TaskEntity newTask) {
        TaskEntity savedTasked = taskRepository.save(newTask);
        return savedTasked;
    }

    @Override
    public TaskEntity updateTask(TaskEntity updateTask) {
        TaskEntity updatedTask = taskRepository.save(updateTask);
        return updatedTask;
    }

    @Override
    public boolean deleteTask(TaskEntity deleteTask) {
        taskRepository.delete(deleteTask);
        return true;
    }
}
