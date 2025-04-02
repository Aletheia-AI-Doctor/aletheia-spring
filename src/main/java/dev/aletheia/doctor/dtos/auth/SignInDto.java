package dev.aletheia.doctor.dtos.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignInDto {
    private String email;
    private String password;
}
