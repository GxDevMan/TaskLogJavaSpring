package com.todoTask.taskLog.controller;

import com.todoTask.taskLog.entity.TaskEntity;
import com.todoTask.taskLog.entity.UserAccount;
import com.todoTask.taskLog.exception.TaskNotFoundException;
import com.todoTask.taskLog.service.TaskService;
import com.todoTask.taskLog.service.UserService;
import jakarta.servlet.http.HttpSession;
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
    @RequestMapping(value = "taskFindbyId/{task_id}", method = RequestMethod.GET)
    public ResponseEntity<TaskEntity> getTaskByTaskId(@PathVariable("task_id") Long Id) {
        try {
            return new ResponseEntity<TaskEntity>(taskService.findbyTaskId(Id), HttpStatus.OK);
        } catch (TaskNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found by Task Id");
        }
    }

    @GetMapping
    @RequestMapping(value = "taskFindbyUserAcc/", method = RequestMethod.POST)
    public ResponseEntity<List<TaskEntity>> getListofTasks(HttpSession session){
        UserAccount account = userService.findUserbyUserName((String) session.getAttribute("username"));
        List<TaskEntity> tasks = taskService.findtasksbyUserAccount(account);
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
            newTask.setUserAccount(userService.findUserbyUserName((String) session.getAttribute("username")));
            return new ResponseEntity<TaskEntity>(taskService.newTask(newTask), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in saving task");
        }
    }

    @PostMapping
    @RequestMapping(value = "updateTask/", method = RequestMethod.POST)
    public ResponseEntity<TaskEntity> updateTask(@RequestBody TaskEntity updateTask, HttpSession session) {
        try {
            updateTask.setUserAccount(userService.findUserbyUserName((String) session.getAttribute("username")));
            return new ResponseEntity<TaskEntity>(taskService.updateTask(updateTask), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in updating task");
        }
    }

    @DeleteMapping
    @RequestMapping(value = "deleteTask/", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteTask(@RequestBody TaskEntity deleteThisTask){
        taskService.deleteTask(deleteThisTask);
        return ResponseEntity.status(HttpStatus.OK).body("Task Deleted Successfully");
    }
}
