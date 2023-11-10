package com.ybe.ybe_toyproject3.domain.user.service;

import com.ybe.ybe_toyproject3.domain.user.dto.response.UserInfo;
import com.ybe.ybe_toyproject3.domain.user.exception.UserNotFoundException;
import com.ybe.ybe_toyproject3.domain.user.model.User;
import com.ybe.ybe_toyproject3.domain.user.repository.UserRepository;
import com.ybe.ybe_toyproject3.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserInfo getUserInfo() {
        Long currentMemberId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(currentMemberId).orElseThrow(UserNotFoundException::new);
        return UserInfo.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
