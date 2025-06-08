package com.boardgameenjoyers.boardgamebuddy.service.gameEntry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameEntryDTO {
    private Long id;
    private LocalDateTime created;
    private String entryTitle;
    private Long gameId;
    private Long groupId;
}
