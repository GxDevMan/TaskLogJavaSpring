package com.todoTask.taskLog.controller;

import com.todoTask.taskLog.entity.TaskEntity;
import com.todoTask.taskLog.entity.UserAccount;
import com.todoTask.taskLog.exception.TaskNotFoundException;
import com.todoTask.taskLog.service.TaskService;
import com.todoTask.taskLog.service.UserService;
import jakarta.servlet.http.HttpSession;
import com.todoTask.taskLog.entity.roleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path = "TaskAPI/v1/Task/")
public class taskController {

    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public taskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping
    @RequestMapping(value = "taskFindbyId/{task_id}/", method = RequestMethod.GET)
    public ResponseEntity<TaskEntity> getTaskByTaskId(@PathVariable("task_id") Long Id, HttpSession session) {
        try {
            UserAccount loggedInUser = (UserAccount) session.getAttribute("userAcc");
            TaskEntity taskEntity = taskService.findbyTaskId(Id);


            if(loggedInUser.getUserName().equals(taskEntity.getUserAccount().getUserName())){
                return new ResponseEntity<TaskEntity>(taskEntity, HttpStatus.OK);
            }
            else if (loggedInUser.getUserRole().equals(roleEnum.ADMIN.toString())){
                return new ResponseEntity<TaskEntity>(taskEntity, HttpStatus.OK);
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Unauthorized access");

        } catch (TaskNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found by Task Id");
        }
    }

    @GetMapping
    @RequestMapping(value = "taskFindbyUserAcc/", method = RequestMethod.GET)
    public ResponseEntity<List<TaskEntity>> getListofTasks(HttpSession session){
        UserAccount loggedInUser = (UserAccount) session.getAttribute("userAcc");
        List<TaskEntity> tasks = taskService.findtasksbyUserAccount(loggedInUser);
        if(tasks.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        }
    }

    @PostMapping
    @RequestMapping(value = "newTask/", method = RequestMethod.POST)
    public ResponseEntity<TaskEntity> newTask(@RequestBody TaskEntity newTask, HttpSession session) {
        try {
            UserAccount loggedInUser = (UserAccount) session.getAttribute("userAcc");
            newTask.setTaskId(null);
            newTask.setUserAccount(loggedInUser);
            newTask = taskService.newTask(newTask);
            return new ResponseEntity<TaskEntity>(newTask, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in saving task");
        }
    }

    @PostMapping
    @RequestMapping(value = "updateTask/", method = RequestMethod.POST)
    public ResponseEntity<TaskEntity> updateTask(@RequestBody TaskEntity updateTask, HttpSession session) {
        try {
            UserAccount loggedInUser = (UserAccount) session.getAttribute("userAcc");
            if(loggedInUser.getUserRole().equals(roleEnum.ADMIN.toString())){
                TaskEntity updatedTask = taskService.updateTask(updateTask);
                return new ResponseEntity<TaskEntity>(updatedTask, HttpStatus.OK);
            }

            TaskEntity dbTask = taskService.findbyTaskId(updateTask.getTaskId());
            if(loggedInUser.getUserName().equals(dbTask.getUserAccount().getUserName())){
                updateTask.setUserAccount(loggedInUser);
                TaskEntity updatedTask = taskService.updateTask(updateTask);
                return new ResponseEntity<TaskEntity>(updatedTask, HttpStatus.OK);
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Unauthorized Access");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in updating task");
        }
    }

    @DeleteMapping
    @RequestMapping(value = "deleteTask/{task_id}/", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteTask(@PathVariable("task_id") Long task_id, HttpSession session){
        UserAccount loggedInUser = (UserAccount) session.getAttribute("userAcc");
        TaskEntity dbTask = taskService.findbyTaskId(task_id);
        if(loggedInUser.getUserRole().equals(roleEnum.ADMIN.toString())){
            taskService.deleteTask(dbTask);
            return ResponseEntity.status(HttpStatus.OK).body("Task Deleted Successfully");
        }

        if(loggedInUser.getUserName().equals(dbTask.getUserAccount().getUserName())){
            taskService.deleteTask(dbTask);
            return ResponseEntity.status(HttpStatus.OK).body("Task Deleted Successfully");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized Access");
    }
}
