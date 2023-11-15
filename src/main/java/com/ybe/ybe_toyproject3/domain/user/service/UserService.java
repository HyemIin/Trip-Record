package com.ybe.ybe_toyproject3.domain.user.service;

import com.ybe.ybe_toyproject3.domain.user.dto.response.DeleteUserResponse;
import com.ybe.ybe_toyproject3.domain.user.dto.response.UserInfo;
import com.ybe.ybe_toyproject3.domain.user.exception.UserNotFoundException;
import com.ybe.ybe_toyproject3.domain.user.model.User;
import com.ybe.ybe_toyproject3.domain.user.repository.UserRepository;
import com.ybe.ybe_toyproject3.global.util.SecurityUtil;
import com.ybe.ybe_toyproject3.global.util.SecurityUtilProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SecurityUtilProvider securityUtilProvider;

    @Transactional(readOnly = true)
    public UserInfo getUserInfo() {
        Long currentUserId = securityUtilProvider.getCurrentUserId();
        User user = userRepository.findById(currentUserId).orElseThrow(UserNotFoundException::new);
        return UserInfo.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    @Transactional
    public DeleteUserResponse deleteUser() {
        Long currentUserId = securityUtilProvider.getCurrentUserId();
        if (!userRepository.existsById(currentUserId)) {
            throw new IllegalArgumentException("이미 삭제된 유저 입니다.");
        }
        userRepository.deleteById(currentUserId);
        return new DeleteUserResponse(currentUserId);
    }
}
