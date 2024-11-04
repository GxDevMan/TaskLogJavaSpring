package com.todoTask.taskLog.controller;


import com.todoTask.taskLog.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class taskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;


    @Test
    public void testTaskFind(){

    }


    @Test
    public void testTaskFindUserAcc(){

    }

    @Test
    public void testNewTask(){

    }

    @Test
    public void updateTask(){

    }

    @Test
    public void deleteTask(){

    }
}
