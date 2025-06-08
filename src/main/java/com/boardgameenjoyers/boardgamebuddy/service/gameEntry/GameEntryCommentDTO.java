package com.boardgameenjoyers.boardgamebuddy.service.gameEntry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class GameEntryCommentDTO {
    private Long id;
    private String text;
    private LocalDateTime created;

}
