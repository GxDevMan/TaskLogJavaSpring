package com.todoTask.taskLog.controller;

import com.todoTask.taskLog.entity.TaskEntity;
import com.todoTask.taskLog.entity.UserAccount;
import com.todoTask.taskLog.exception.TaskNotFoundException;
import com.todoTask.taskLog.service.SessionObjectMapperService;
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
import java.util.Optional;

@RestController
@RequestMapping(path = "TaskAPI/v1/Task/")
public class taskController {

    private final TaskService taskService;
    private final UserService userService;
    private final SessionObjectMapperService sessionObjectMapperService;

    @Autowired
    public taskController(TaskService taskService, UserService userService, SessionObjectMapperService sessionObjectMapperService) {
        this.sessionObjectMapperService = sessionObjectMapperService;
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping
    @RequestMapping(value = "taskFindbyId/{task_id}/", method = RequestMethod.GET)
    public ResponseEntity<TaskEntity> getTaskByTaskId(@PathVariable("task_id") Long Id, HttpSession session) {
        try {
            Optional<UserAccount> loggedInuserAccountOpt = sessionObjectMapperService.BytetoaccountObj((byte[]) session.getAttribute("userAcc"));
            if(!loggedInuserAccountOpt.isPresent()){
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error Converting bytes to Account OBJ");
            }

            TaskEntity taskEntity = taskService.findbyTaskId(Id);
            if(loggedInuserAccountOpt.get().getUserName().equals(taskEntity.getUserAccount().getUserName())){
                return new ResponseEntity<>(taskEntity, HttpStatus.OK);
            }

            if (loggedInuserAccountOpt.get().getUserRole().equals(roleEnum.ADMIN.toString())){
                return new ResponseEntity<>(taskEntity, HttpStatus.OK);
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Unauthorized access");

        } catch (TaskNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found by Task Id");
        }
    }

    @GetMapping
    @RequestMapping(value = "taskFindbyUserAcc/", method = RequestMethod.GET)
    public ResponseEntity<List<TaskEntity>> getListofTasks(HttpSession session){
        Optional<UserAccount> loggedInUserOpt =  sessionObjectMapperService.BytetoaccountObj((byte[]) session.getAttribute("userAcc"));
        if(!loggedInUserOpt.isPresent()){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error Converting byte to Account Obj");
        }

        List<TaskEntity> tasks = taskService.findtasksbyUserAccount(loggedInUserOpt.get());
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
            Optional<UserAccount> loggedInUserOpt =  sessionObjectMapperService.BytetoaccountObj((byte[]) session.getAttribute("userAcc"));
            if(!loggedInUserOpt.isPresent()){
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error Converting byte to Account Obj");
            }
            newTask.setUserAccount(loggedInUserOpt.get());
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
            Optional<UserAccount> loggedInUserOpt =  sessionObjectMapperService.BytetoaccountObj((byte[]) session.getAttribute("userAcc"));
            if(!loggedInUserOpt.isPresent()){
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error Converting byte to Account Obj");
            }

            if(loggedInUserOpt.get().getUserRole().equals(roleEnum.ADMIN.toString())){
                TaskEntity updatedTask = taskService.updateTask(updateTask);
                return new ResponseEntity<TaskEntity>(updatedTask, HttpStatus.OK);
            }

            TaskEntity dbTask = taskService.findbyTaskId(updateTask.getTaskId());
            if(loggedInUserOpt.get().getUserName().equals(dbTask.getUserAccount().getUserName())){
                updateTask.setUserAccount(loggedInUserOpt.get());
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
        Optional<UserAccount> loggedInUserOpt =  sessionObjectMapperService.BytetoaccountObj((byte[]) session.getAttribute("userAcc"));
        if(!loggedInUserOpt.isPresent()){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error Converting byte to Account Obj");
        }
        TaskEntity dbTask = taskService.findbyTaskId(task_id);
        if(loggedInUserOpt.get().getUserRole().equals(roleEnum.ADMIN.toString())){
            taskService.deleteTask(dbTask);
            return ResponseEntity.status(HttpStatus.OK).body("Task Deleted Successfully");
        }

        if(loggedInUserOpt.get().getUserName().equals(dbTask.getUserAccount().getUserName())){
            taskService.deleteTask(dbTask);
            return ResponseEntity.status(HttpStatus.OK).body("Task Deleted Successfully");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized Access");
    }
}
