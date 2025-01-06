package com.taskManager.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserRegistrationDto {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email(message = "Please enter a valid e-mail address")
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String role;

    public @NotBlank String getRole() {
        return role;
    }

    public void setRole(@NotBlank String role) {
        this.role = role;
    }

    public @NotBlank String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank String lastName) {
        this.lastName = lastName;
    }

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
        if (!isStrongPassword(password)) {
            throw new IllegalArgumentException("Password does not meet complexity requirements" +
                    "The password must be at least 8 characters long and contain uppercase and lowercase letters, numbers, and punctuation marks.");
        }
        this.password = password;
    }

    private boolean isStrongPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        boolean hasUpperCase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowerCase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasPunctuationMarks = password.chars().anyMatch(ch -> "!@#$%^&*()_-+=`~{}[];:.,|\'?".indexOf(ch) >= 0);

        return hasUpperCase && hasLowerCase && hasDigit && hasPunctuationMarks;
    }
}
