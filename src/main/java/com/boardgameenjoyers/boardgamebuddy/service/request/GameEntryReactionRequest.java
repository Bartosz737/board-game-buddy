package com.boardgameenjoyers.boardgamebuddy.service.request;

import com.boardgameenjoyers.boardgamebuddy.dao.enums.ReactionType;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameEntryReactionRequest {

    private ReactionType reactionType;

}
