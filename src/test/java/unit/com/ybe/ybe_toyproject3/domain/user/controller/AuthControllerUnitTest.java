package com.ybe.ybe_toyproject3.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ybe.ybe_toyproject3.domain.user.dto.request.LoginRequest;
import com.ybe.ybe_toyproject3.domain.user.dto.request.SignUpRequest;
import com.ybe.ybe_toyproject3.domain.user.dto.response.LoginResponse;
import com.ybe.ybe_toyproject3.domain.user.dto.response.SignUpResponse;
import com.ybe.ybe_toyproject3.domain.user.service.AuthService;
import com.ybe.ybe_toyproject3.domain.user.service.UserService;
import com.ybe.ybe_toyproject3.global.error.response.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
class AuthControllerUnitTest {
    @Autowired
    MockMvc mvc;
    @MockBean
    AuthService authService;
    @Autowired
    ObjectMapper objectMapper;
    static final String AUTH_URL = "/auth";

    @Test
    @DisplayName("회원가입_성공_테스트")
    public void signUpSuccess() throws Exception{
        // given
        SignUpRequest signUpRequest = new SignUpRequest("tempuser", "tempuser@email.com", "12341234");
        when(authService.signUp(any())).thenReturn(SignUpResponse.builder()
                        .name("tempuser")
                        .id(1L)
                        .email("tempuser@email.com")
                .build());

        // when
        ResultActions resultActions = mvc.perform(post(AUTH_URL + "/signup")
                        .content(objectMapper.writeValueAsBytes(signUpRequest))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    SignUpResponse signUpResponse = objectMapper.readValue(contentAsString, SignUpResponse.class);
                    assert signUpResponse != null;
                    assert signUpResponse.getId() != null;
                    assert signUpResponse.getEmail().equals("tempuser@email.com");
                });

    }

    @Test
    @DisplayName("회원가입_실패_테스트_중복된_이메일")
    public void signUpFail() throws Exception{
        // given
        SignUpRequest signUpRequest = new SignUpRequest("tempuser", "userForTest@email.com", "12341234");
        when(authService.signUp(any())).thenThrow(IllegalArgumentException.class);

        // when
        ResultActions resultActions = mvc.perform(post(AUTH_URL + "/signup")
                        .content(objectMapper.writeValueAsBytes(signUpRequest))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isConflict())
                .andDo(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    ErrorResponse errorResponse = objectMapper.readValue(contentAsString, ErrorResponse.class);
                    assert errorResponse != null;
                    assert errorResponse.getErrorCode() == 409;
                });

    }

}