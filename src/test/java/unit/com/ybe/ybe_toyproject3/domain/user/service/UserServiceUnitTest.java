package com.ybe.ybe_toyproject3.domain.user.service;

import com.ybe.ybe_toyproject3.domain.user.dto.response.UserInfo;
import com.ybe.ybe_toyproject3.domain.user.model.User;
import com.ybe.ybe_toyproject3.domain.user.repository.UserRepository;
import com.ybe.ybe_toyproject3.global.util.SecurityUtilProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.ybe.ybe_toyproject3.global.common.Authority.ROLE_USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class UserServiceUnitTest {
    @Autowired
    UserService userService;
    @MockBean
    SecurityUtilProvider securityUtilProvider;
    @MockBean
    UserRepository userRepository;

    @Test
    @DisplayName("현재_유저_정보_조회_성공_테스트")
    public void getCurrentUserSuccess() throws Exception{
        // given
        when(securityUtilProvider.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder()
                .id(1L)
                .name("tempuser")
                .email("tempuser@email.com")
                .authority(ROLE_USER)
                .build()));
        // when
        UserInfo userInfo = userService.getUserInfo();

        // then
        assert userInfo != null;
        assert userInfo.getId() == 1L;
    }

    @Test
    @DisplayName("현재_유저_정보_조회_실패_테스트_없는_유저")
    public void getCurrentUserFail() throws Exception{
        // given
        when(securityUtilProvider.getCurrentUserId()).thenThrow(IllegalArgumentException.class);

        // when

        // then
        assertThrows(IllegalArgumentException.class, () -> userService.getUserInfo());
    }
}