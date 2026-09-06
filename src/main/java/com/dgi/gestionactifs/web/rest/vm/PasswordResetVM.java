package com.dgi.gestionactifs.web.rest.vm;

import jakarta.validation.constraints.Size;

public class PasswordResetVM {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String newPassword;

    public PasswordResetVM() {
        // Empty constructor needed for Jackson.
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
