package com.ybe.ybe_toyproject3.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ybe.ybe_toyproject3.domain.user.dto.response.UserInfo;
import com.ybe.ybe_toyproject3.domain.user.repository.UserRepository;
import com.ybe.ybe_toyproject3.domain.user.service.UserService;
import com.ybe.ybe_toyproject3.global.common.annotation.WithMockCustomUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
class UserControllerIntegrateTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    MockMvc mvc;
    @MockBean
    UserService userService;
    @Autowired
    ObjectMapper objectMapper;
    static final String USER_URL = "/user";

    @Test
    @WithMockCustomUser
    @DisplayName("로그인_유저_정보_조회_성공_테스트")
    public void getUserLoginInfo() throws Exception{
        // given
        UserInfo userInfo = UserInfo.builder()
                .id(1L)
                .email("user@email.com")
                .name("name")
                .build();

        when(userService.getUserInfo()).thenReturn(userInfo);


        // when
        ResultActions resultActions = mvc.perform(get(USER_URL)
                        .accept(APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(result -> {
                    UserInfo response = objectMapper.readValue(result.getResponse().getContentAsString(), UserInfo.class);
                    assert response != null;
                    assert response.getId() != null;
                    assert response.getId() == 1L;

                });
    }
}