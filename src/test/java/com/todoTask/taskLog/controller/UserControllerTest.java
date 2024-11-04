package com.todoTask.taskLog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todoTask.taskLog.DTO.UserAccountLoginDTO;
import com.todoTask.taskLog.DTO.UserAccountLoginDTOMapper;
import com.todoTask.taskLog.DTO.UserAccountTestDTOFactory;
import com.todoTask.taskLog.entity.UserAccount;
import com.todoTask.taskLog.service.UserService;
import jakarta.servlet.http.Cookie;
import net.bytebuddy.utility.RandomString;
import org.junit.After;
import org.junit.Before;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private Cookie cookie[];

    @Before
    public void setupUp(){
        System.out.println("SETTING UP");
        UserAccount loginUser = new UserAccount();
        loginUser.setUserName("ADMINTEST1");
        loginUser.setUserRole("ADMIN");
        loginUser.setPassword("123");

        UserAccountLoginDTO userAccountLoginDTO = new UserAccountLoginDTO();

        try {
            UserAccountTestDTOFactory factory = new UserAccountLoginDTOMapper();
            userAccountLoginDTO = factory.convertToUserLoginDTO(loginUser);
            userService.newUser(loginUser);
        } catch (Exception e) {}
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String reqBody = objectMapper.writeValueAsString(userAccountLoginDTO);

            MvcResult result = mockMvc.perform(post("/TaskAPI/v1/login/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reqBody)).andReturn();

            MockHttpServletResponse response = result.getResponse();
            assertEquals(HttpStatus.OK.value(), response.getStatus());

            cookie = response.getCookies();
            System.out.println("HEY COOKIE WHERE?");
        } catch (Exception e) {}
    }

    @After
    public void tearDown() {
        System.out.println("TEARDOWN");
        try{
            MvcResult result = mockMvc.perform(delete("/TaskAPI/v1/logout/")
                            .cookie(this.cookie)).andReturn();

            MockHttpServletResponse response = result.getResponse();
            assertEquals(HttpStatus.OK.value(), response.getStatus());

            userService.deleteUser("ADMINTEST1");
            cookie = null;
        } catch (Exception e) {}
    }

    @WithMockUser(username = "ADMIN", roles = {"ADMIN"})
    @Test
    public void userNameFindAccTest() throws Exception {

        //NOT FOUND TEST
        String userNameDoesNotExist = RandomString.make();

        MvcResult result = mockMvc.perform(
                get(String.format("/TaskAPI/v1/UserAccount/userNameFindAcc/%s/",userNameDoesNotExist))
                        .cookie(this.cookie)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());


        //SAMPLE FOUND TEST
        UserAccount SampleSearch = new UserAccount();
        String userName = "SEARCH";
        SampleSearch.setUserName(userName);
        SampleSearch.setUserRole("ADMIN");
        SampleSearch.setPassword("123");

        try {
            userService.newUser(SampleSearch);
        } catch (Exception e) {}


        result = mockMvc.perform(
                get(String.format("/TaskAPI/v1/UserAccount/userNameFindAcc/%s/",userName))
                        .cookie(this.cookie)
                        .contentType(MediaType.APPLICATION_JSON)).andReturn();

        response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        try{
            userService.deleteUser("SEARCH");
        } catch (Exception e) {}
    }

    @WithMockUser(username = "ADMIN", roles = {"ADMIN"})
    @Test
    public void newUserAccTest() throws Exception {

        //NEW USER TEST
        UserAccount SampleSearch = new UserAccount();
        String userName = "INSERT";
        SampleSearch.setUserName(userName);
        SampleSearch.setUserRole("USERACC");
        SampleSearch.setPassword("123");

        ObjectMapper objectMapper = new ObjectMapper();
        String reqBody = objectMapper.writeValueAsString(SampleSearch);

        MvcResult result = mockMvc.perform(
                post("/TaskAPI/v1/UserAccount/newUserAcc/")
                        .cookie(this.cookie)
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON)).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());


        try{
            userService.deleteUser("INSERT");
        } catch (Exception e) {}

    }

    @WithMockUser(username = "ADMIN", roles = {"ADMIN"})
    @Test
    public void userLoggedInInfoTest() throws Exception {

    }

    @WithMockUser(username = "ADMIN", roles = {"ADMIN"})
    @Test
    public void updateUserAccTest() throws Exception {

    }

    @WithMockUser(username = "ADMIN", roles = {"ADMIN"})
    @Test
    public void deleteAccTest() throws Exception {

    }


}
