package com.todoTask.taskLog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todoTask.taskLog.DTO.UserAccountLoginDTO;
import com.todoTask.taskLog.DTO.UserAccountLoginDTOMapper;
import com.todoTask.taskLog.DTO.UserAccountTestDTOFactory;
import com.todoTask.taskLog.entity.UserAccount;
import com.todoTask.taskLog.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.RequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class authControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserService userService;


    @Test
    public void testLogin() throws Exception {
        // Test user that does not exist
        UserAccount loginUser = new UserAccount();
        loginUser.setUserName("sampleUser");
        loginUser.setPassword("samplePassword");

        UserAccountTestDTOFactory factory = new UserAccountLoginDTOMapper();
        UserAccountLoginDTO userAccountLoginDTO = factory.convertToUserLoginDTO(loginUser);

        ObjectMapper objectMapper = new ObjectMapper();
        String reqBody = objectMapper.writeValueAsString(userAccountLoginDTO);
        System.out.println(reqBody);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/TaskAPI/v1/login/")
                .accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqBody);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());

    }

    @WithMockUser(username = "sampleUser", roles = {"ADMIN"})
    @Test
    public void TestLoginExisting() throws Exception {
        //Test User that exists login Test
        UserAccount loginUser = new UserAccount();
        String passwordTest = "SAMPLEPASSWORD";
        loginUser.setUserName("USERTEST1");
        loginUser.setUserRole("USERACC");
        loginUser.setPassword(passwordTest);

        try {
            loginUser = userService.newUser(loginUser);
        } catch (Exception e){

        }

        //Best Case Scenario Test
        UserAccount actualTestLogin = new UserAccount();
        actualTestLogin.setUserName("USERTEST1");
        actualTestLogin.setPassword(passwordTest);

        UserAccountTestDTOFactory factory = new UserAccountLoginDTOMapper();
        UserAccountLoginDTO userAccountLoginDTO = factory.convertToUserLoginDTO(actualTestLogin);

        ObjectMapper objectMapper = new ObjectMapper();
        String reqBody = objectMapper.writeValueAsString(userAccountLoginDTO);

        MvcResult result = mockMvc.perform(post("/TaskAPI/v1/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqBody))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        //User Doest Not Exist Test
        actualTestLogin = new UserAccount();
        actualTestLogin.setUserName("USERDOESNOTEXIST");
        actualTestLogin.setPassword(passwordTest);

        userAccountLoginDTO = factory.convertToUserLoginDTO(actualTestLogin);
        reqBody = objectMapper.writeValueAsString(userAccountLoginDTO);

        result = mockMvc.perform(post("/TaskAPI/v1/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqBody))
                .andExpect(status().isNotFound())
                .andReturn();

        response = result.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());

        //PASSWORD ERROR
        actualTestLogin = new UserAccount();
        actualTestLogin.setUserName("USERTEST1");
        actualTestLogin.setPassword("PASSWORDERROR");

        userAccountLoginDTO = factory.convertToUserLoginDTO(actualTestLogin);
        reqBody = objectMapper.writeValueAsString(userAccountLoginDTO);

        result = mockMvc.perform(post("/TaskAPI/v1/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqBody))
                .andExpect(status().isUnauthorized())
                .andReturn();

        response = result.getResponse();
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());

        try{
            userService.deleteUser(loginUser.getUserName());
        } catch (Exception e){

        }
    }
}
