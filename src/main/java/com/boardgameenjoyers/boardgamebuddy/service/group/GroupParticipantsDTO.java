package com.boardgameenjoyers.boardgamebuddy.service.group;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GroupParticipantsDTO {
    private final Long participantId;
    private final Long groupId;
    private final Long userId;

}
