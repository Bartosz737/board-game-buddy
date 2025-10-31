package com.boardgameenjoyers.boardgamebuddy.service.game.gameParticipants;

import com.boardgameenjoyers.boardgamebuddy.service.user.UserDTS;
import lombok.Value;

@Value
public class GameParticipantsDts {
    UserDTS userDTS;
    Long points;
}
