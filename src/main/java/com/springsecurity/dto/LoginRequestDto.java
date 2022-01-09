package com.springsecurity.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@ToString(exclude = "password")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequestDto {

    @NotBlank(message = "validation.emailEmpty")
    @Email(message = "validation.emailFormat")
    String email;

    @NotBlank(message = "validation.passwordEmpty")
    @Size(min = 6, message = "validation.passwordMinLength")
    String password;

    public void setEmail(String email) {
        this.email = email != null ? email.trim() : null;
    }

    public void setPassword(String password) {
        this.password = password != null ? password.trim() : null;
    }
}
