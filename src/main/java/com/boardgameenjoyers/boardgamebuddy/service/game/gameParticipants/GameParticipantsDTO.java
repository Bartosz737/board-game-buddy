package com.boardgameenjoyers.boardgamebuddy.service.game.gameParticipants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameParticipantsDTO {
    private Long id;
    private int point;
    private String userName;
}
