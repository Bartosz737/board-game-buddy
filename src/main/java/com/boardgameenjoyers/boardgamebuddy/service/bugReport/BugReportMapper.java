package com.boardgameenjoyers.boardgamebuddy.service.bugReport;

import com.boardgameenjoyers.boardgamebuddy.dao.BugReport.BugReport;
import com.boardgameenjoyers.boardgamebuddy.service.response.BugReportResponse;
import org.springframework.stereotype.Component;

@Component
public class BugReportMapper {

    public BugReportResponse toDto(BugReport bugReport) {
        return BugReportResponse.builder()
                .id(bugReport.getId())
                .username(bugReport.getUser().getUserName())
                .text(bugReport.getText())
                .reportedAt(bugReport.getReportedAt())
                .build();
    }
}
