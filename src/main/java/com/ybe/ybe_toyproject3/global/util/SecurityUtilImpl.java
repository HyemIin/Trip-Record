package com.ybe.ybe_toyproject3.global.util;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtilImpl implements SecurityUtilProvider{

    @Override
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }

        return Long.parseLong(authentication.getName());
    }
}
