package com.springsecurity.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@ToString(exclude = "password")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignupRequestDto {

    @NotBlank(message = "validation.nameEmpty")
    @Size(min = 3, message = "validation.nameMinLength")
    String name;

    @NotBlank(message = "validation.emailEmpty")
    @Email(message = "validation.emailFormat")
    String email;

    @NotBlank(message = "validation.passwordEmpty")
    @Size(min = 6, message = "validation.passwordMinLength")
    String password;

    @NotNull(message = "validation.birthDateEmpty")
    @Past(message = "validation.birthDateIncorrect")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday;

    public void setName(String name) {
        this.name = name != null ? name.trim() : null;
    }

    public void setEmail(String email) {
        this.email = email != null ? email.trim() : null;
    }

    public void setPassword(String password) {
        this.password = password != null ? password.trim() : null;
    }
}
