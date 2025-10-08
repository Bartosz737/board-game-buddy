package com.boardgameenjoyers.boardgamebuddy.service.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private String username;
    private String email;
    private String bio;
    private long gamesPlayed;
}
