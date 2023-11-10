package com.ybe.ybe_toyproject3.domain.user.dto.request;

import com.ybe.ybe_toyproject3.domain.user.model.User;
import com.ybe.ybe_toyproject3.global.common.Authority;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.ybe.ybe_toyproject3.global.common.Authority.ROLE_USER;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @NotBlank
    private String name;
    @Email
    private String email;
    @NotBlank
    private String password;

    public User toEntity(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
        return User.builder()
                .email(email)
                .name(name)
                .password(password)
                .authority(ROLE_USER)
                .build();
    }


}
