package com.boardgameenjoyers.boardgamebuddy.service.group;

import com.boardgameenjoyers.boardgamebuddy.service.gameEntry.GameEntryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupDTO {
    private Long id;
    private String groupName;
    private LocalDateTime created;
    private List<GameEntryDTO> gameEntries;
    private List<Long> groupParticipants;
}

