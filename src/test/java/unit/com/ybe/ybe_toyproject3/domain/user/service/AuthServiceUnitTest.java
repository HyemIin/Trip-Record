package com.ybe.ybe_toyproject3.domain.user.service;

import com.ybe.ybe_toyproject3.domain.user.dto.request.SignUpRequest;
import com.ybe.ybe_toyproject3.domain.user.dto.response.SignUpResponse;
import com.ybe.ybe_toyproject3.domain.user.model.User;
import com.ybe.ybe_toyproject3.domain.user.repository.UserRepository;
import com.ybe.ybe_toyproject3.global.common.Authority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static com.ybe.ybe_toyproject3.global.common.Authority.ROLE_USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class AuthServiceUnitTest {
    @Autowired
    AuthService authService;
    @MockBean
    UserRepository userRepository;

    @Test
    @DisplayName("회원가입_성공_테스트")
    public void signUpSuccess() throws Exception{
        // given
        SignUpRequest signUpRequest = new SignUpRequest("tempuser", "tempuser@email.com", "12341234");

        when(userRepository.save(any())).thenReturn(User.builder()
                        .id(1L)
                        .name("tempuser")
                        .email("tempuser@email.com")
                        .authority(ROLE_USER)
                .build());

        // when
        SignUpResponse signUpResponse = authService.signUp(signUpRequest);

        // then
        assert signUpResponse != null;
        assert signUpResponse.getEmail().equals("tempuser@email.com");
    }

    @Test
    @DisplayName("회원가입_실패_테스트_이메일_중복")
    public void signUpFail() throws Exception{
        // given
        SignUpRequest signUpRequest = new SignUpRequest("tempuser", "tempuser@email.com", "12341234");
        // when
        when(userRepository.existsByEmail("tempuser@email.com")).thenReturn(true);

        // then
        assertThrows(IllegalArgumentException.class, () -> authService.signUp(signUpRequest));
    }
}