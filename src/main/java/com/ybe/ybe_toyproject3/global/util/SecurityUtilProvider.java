package com.ybe.ybe_toyproject3.global.util;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

public interface SecurityUtilProvider {
    Long getCurrentUserId();
}
