package com.boardgameenjoyers.boardgamebuddy.service.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameEntryWhichUserParticipated {
    private Long gameEntryId;
    private String gameEntryTitle;
    private String groupName;
    private String gameTitle;
    private List<String> gameParticipants;
    private LocalDateTime created;
    private Integer comments;
}
