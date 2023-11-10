package com.ybe.ybe_toyproject3.global.security.service;

import com.ybe.ybe_toyproject3.domain.user.model.User;
import com.ybe.ybe_toyproject3.domain.user.repository.UserRepository;
import com.ybe.ybe_toyproject3.global.security.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byEmail = userRepository.findByEmail(username);
        if (byEmail.isPresent()) {
            return UserPrincipal.create(byEmail.get());
        } else {
            throw new IllegalArgumentException(username+ " -> 데이터베이스에 존재하지 않는 사용자 입니다");
        }

    }

    public UserDetails loadUserById(String userIdString) {
        Long userId = Long.parseLong(userIdString);
        Optional<User> byId = userRepository.findById(userId);
        if (byId.isPresent()) {
            return UserPrincipal.create(byId.get());
        } else {
            throw new IllegalArgumentException(userId + " -> 데이터베이스에 존재하지 않는 사용자 입니다");
        }
    }
}
