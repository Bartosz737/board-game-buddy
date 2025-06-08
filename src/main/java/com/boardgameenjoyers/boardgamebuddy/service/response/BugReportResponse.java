package com.boardgameenjoyers.boardgamebuddy.service.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BugReportResponse {

    private Long id;
    private String username;
    private String text;
    private LocalDateTime reportedAt;
}
