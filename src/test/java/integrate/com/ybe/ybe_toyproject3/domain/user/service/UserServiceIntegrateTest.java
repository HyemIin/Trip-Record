package com.ybe.ybe_toyproject3.domain.user.service;

import com.ybe.ybe_toyproject3.domain.user.dto.response.UserInfo;
import com.ybe.ybe_toyproject3.domain.user.exception.UserNotFoundException;
import com.ybe.ybe_toyproject3.domain.user.model.User;
import com.ybe.ybe_toyproject3.domain.user.repository.UserRepository;
import com.ybe.ybe_toyproject3.global.common.Authority;
import com.ybe.ybe_toyproject3.global.util.SecurityUtilProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static com.ybe.ybe_toyproject3.global.common.Authority.ROLE_USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class UserServiceIntegrateTest {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @MockBean
    SecurityUtilProvider securityUtilProvider;

    @Test
    @DisplayName("현재_유저_정보_조회_성공_테스트")
    public void getCurrentUserSuccess() throws Exception{
        // given
        User user = User.builder()
                .authority(ROLE_USER)
                .password("encodedPassword")
                .email("testTempUser@email.com")
                .name("testTempUser")
                .build();
        User saved = userRepository.save(user);


        // when
        when(securityUtilProvider.getCurrentUserId()).thenReturn(saved.getId());
        UserInfo userInfo = userService.getUserInfo();

        // then
        assert userInfo != null;
        assert userInfo.getId().equals(saved.getId());
        assert userInfo.getEmail().equals("testTempUser@email.com");
    }

    @Test
    @DisplayName("현재_유저_정보_조회_실패_테스트_없는_유저")
    public void getCurrentUserFail() throws Exception{
        // given
        User user = User.builder()
                .authority(ROLE_USER)
                .password("encodedPassword")
                .email("testTempUser@email.com")
                .name("testTempUser")
                .build();
        User saved = userRepository.save(user);


        // when
        when(securityUtilProvider.getCurrentUserId()).thenReturn(saved.getId()+1);


        // then
        assertThrows(UserNotFoundException.class, () -> userService.getUserInfo());
    }
}