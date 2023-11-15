package com.ybe.ybe_toyproject3.domain.user.service;

import com.ybe.ybe_toyproject3.domain.user.dto.request.LoginRequest;
import com.ybe.ybe_toyproject3.domain.user.dto.request.SignUpRequest;
import com.ybe.ybe_toyproject3.domain.user.dto.response.SignUpResponse;
import com.ybe.ybe_toyproject3.domain.user.model.User;
import com.ybe.ybe_toyproject3.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static com.ybe.ybe_toyproject3.global.common.Authority.ROLE_USER;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class AuthServiceIntegrateTest {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthService authService;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .name("userForTest")
                .email("userForTest@email.com")
                .authority(ROLE_USER)
                .password(passwordEncoder.encode("12341234"))
                .build();
        userRepository.save(user);

    }

    @AfterEach
    void tearDown() {
        userRepository.deleteByEmail("userForTest@email.com");
    }
    @Test
    @DisplayName("회원가입_성공_테스트")
    public void signUpSuccess() throws Exception{
        // given
        SignUpRequest signUpRequest = new SignUpRequest("tempuser", "tempuser@email.com", "12341234");

        // when
        SignUpResponse signUpResponse = authService.signUp(signUpRequest);
        // then
        assert signUpResponse != null;
        assert signUpResponse.getId() != null;
        assert signUpResponse.getEmail().equals("tempuser@email.com");
    }

    @Test
    @DisplayName("회원가입_실패_테스트_이메일_중복")
    public void signUpFailTest() throws Exception{
        // given
        SignUpRequest signUpRequest = new SignUpRequest("tempuser", "userForTest@email.com", "12341234");

        // when

        // then
        assertThrows(IllegalArgumentException.class, () -> authService.signUp(signUpRequest));
    }

}