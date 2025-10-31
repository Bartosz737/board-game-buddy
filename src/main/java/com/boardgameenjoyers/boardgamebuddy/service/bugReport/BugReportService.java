package com.boardgameenjoyers.boardgamebuddy.service.bugReport;

import com.boardgameenjoyers.boardgamebuddy.service.request.utilRequest.BugReportRequest;
import com.boardgameenjoyers.boardgamebuddy.service.response.BugReportResponse;

import java.util.List;

public interface BugReportService {

    void reportBug(BugReportRequest bugReportRequest);

    List<BugReportResponse> getAllReports();
}
