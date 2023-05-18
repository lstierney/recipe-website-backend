package com.lstierneyltd.recipebackend.entities;

public class AuthenticationResponse {
    private String token;

    public AuthenticationResponse() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
