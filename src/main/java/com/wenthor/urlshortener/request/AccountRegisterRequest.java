package com.wenthor.urlshortener.request;

import javax.validation.constraints.Email;

public class AccountRegisterRequest {
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
}
