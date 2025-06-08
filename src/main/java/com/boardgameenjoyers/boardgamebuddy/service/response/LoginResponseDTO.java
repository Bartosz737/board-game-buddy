package com.boardgameenjoyers.boardgamebuddy.service.response;

public class LoginResponseDTO {
    private final String jwt;

    public LoginResponseDTO(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

}
