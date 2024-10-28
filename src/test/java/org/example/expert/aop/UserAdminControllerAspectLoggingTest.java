package org.example.expert.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.config.JwtUtil;
import org.example.expert.domain.user.controller.UserAdminController;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.example.expert.domain.user.service.UserAdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserAdminControllerAspectLoggingTest {

    MockMvc mockMvc;

    @MockBean
    JwtUtil jwtUtil;

    @MockBean
    UserAdminService userAdminService;

    @Autowired
    UserAdminController userAdminController;

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(userAdminController).build();
    }


    @Test
    public void testApiLog() throws Exception {
        // given
        String mockToken = "mockTokenValue";
        String mockUserId = "user";

        when(jwtUtil.getSubject(mockToken)).thenReturn(mockUserId);
        doNothing().when(userAdminService).changeUserRole(anyLong(), any());

        // when
        UserRoleChangeRequest userRoleChangeRequest = new UserRoleChangeRequest("ROLE_USER");

        // then
        mockMvc.perform(patch("/admin/users/{userId}", 1L)
                        .header("Authorization", mockToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userRoleChangeRequest))
                )
                .andExpect(status().isOk());
    }
}
