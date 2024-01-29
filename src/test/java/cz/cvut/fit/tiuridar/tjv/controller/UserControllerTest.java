package cz.cvut.fit.tiuridar.tjv.controller;

import cz.cvut.fit.tiuridar.tjv.domain.User;
import cz.cvut.fit.tiuridar.tjv.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    UserService userService;
    @Test
    void putUserTest() throws Exception{
        User user = new User();
        user.setUsername("username");
        user.setRealName("User Name");
        user.setPassword("password");

        Mockito.when(userService.create(user)).thenReturn(user);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/user").
                        contentType(MediaType.APPLICATION_JSON).
                        content("{\"username\": \"username\"," +
                        "\"realName\": \"User Name\"," +
                        "\"password\": \"password\"}")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is("username")));
    }
}
