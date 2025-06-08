package com.boardgameenjoyers.boardgamebuddy.service.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class GameCommentDTO {
    private Long id;
    private String text;
    private LocalDateTime created;
}
