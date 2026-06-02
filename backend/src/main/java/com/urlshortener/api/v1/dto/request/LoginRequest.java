package com.urlshortener.api.v1.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    public LoginRequest(){

    }

    public LoginRequest(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String email(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String password(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }
}
