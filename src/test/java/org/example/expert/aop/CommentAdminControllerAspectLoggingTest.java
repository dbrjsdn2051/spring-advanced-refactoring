package org.example.expert.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.config.JwtUtil;
import org.example.expert.domain.comment.controller.CommentAdminController;
import org.example.expert.domain.comment.service.CommentAdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CommentAdminControllerAspectLoggingTest {

    MockMvc mockMvc;

    @MockBean
    JwtUtil jwtUtil;

    @MockBean
    CommentAdminService commentAdminService;

    @Autowired
    CommentAdminController commentAdminController;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentAdminController).build();
    }

    @Test
    public void testApiLog() throws Exception {
        // given
        String mockToken = "mockTokenValue";
        String mockUserId = "user";

        when(jwtUtil.getSubject(mockToken)).thenReturn(mockUserId);
        doNothing().when(commentAdminService).deleteComment(1L);

        // when

        // then
        mockMvc.perform(delete("/admin/comments/{commentId}", 1L)
                        .header("Authorization", mockToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(1L))
                )
                .andExpect(status().isOk());

    }
}
