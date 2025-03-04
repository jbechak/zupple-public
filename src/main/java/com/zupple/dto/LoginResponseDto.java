package com.zupple.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zupple.model.UserModel;

public class LoginResponseDto {

    private String token;
    private UserModel user;

    public LoginResponseDto(String token, UserModel user) {
        this.token = token;
        this.user = user;
    }

    @JsonProperty("token")
    String getToken() {
        return token;
    }

    void setToken(String token) {
        this.token = token;
    }

    @JsonProperty("user")
    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
