package com.boardgameenjoyers.boardgamebuddy.service.request;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AddGameToCategoryRequest {
    private Long gameId;
    private Long gameCategoryId;
}
