package com.taskManager.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserLoginDto {

    @Email(message = "Please enter a valid e-mail address")
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public @Email(message = "Please enter a valid e-mail address") @NotBlank String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Please enter a valid e-mail address") @NotBlank String email) {
        this.email = email;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }
}
