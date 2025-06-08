package com.boardgameenjoyers.boardgamebuddy.service.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTS {
    private Long id;
    private String groupName;
    private LocalDateTime created;
    private String createdBy;
    private boolean owner;
}
