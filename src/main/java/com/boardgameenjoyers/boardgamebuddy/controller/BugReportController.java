package com.boardgameenjoyers.boardgamebuddy.controller;

import com.boardgameenjoyers.boardgamebuddy.service.bugReport.BugReportService;
import com.boardgameenjoyers.boardgamebuddy.service.request.utilRequest.BugReportRequest;
import com.boardgameenjoyers.boardgamebuddy.service.response.BugReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class BugReportController {

    private final BugReportService bugReportService;

    @PostMapping("report-bug")
    public ResponseEntity<?> reportABug(@RequestBody BugReportRequest bugReportRequest) {
        bugReportService.reportBug(bugReportRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("bugs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BugReportResponse>> getAllReports() {
        return ResponseEntity.ok(bugReportService.getAllReports());
    }
}
