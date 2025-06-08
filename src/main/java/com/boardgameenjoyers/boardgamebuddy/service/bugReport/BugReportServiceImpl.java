package com.boardgameenjoyers.boardgamebuddy.service.bugReport;

import com.boardgameenjoyers.boardgamebuddy.dao.BugReport.BugReport;
import com.boardgameenjoyers.boardgamebuddy.dao.BugReport.BugReportRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserRepository;
import com.boardgameenjoyers.boardgamebuddy.service.request.BugReportRequest;
import com.boardgameenjoyers.boardgamebuddy.service.response.BugReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BugReportServiceImpl implements BugReportService {

    private final UserRepository userRepository;
    private final BugReportRepository bugReportRepository;
    private final BugReportMapper bugReportMapper;

    @Override
    @Transactional
    public void reportBug(BugReportRequest bugReportRequest) {
        if (bugReportRequest == null) {
            throw new IllegalArgumentException("Bug report request must not be null");
        }

        User user = userRepository.findByUserName(getCurrentUsername())
                .orElseThrow(() -> new EntityNotFoundException("user not found"));

        BugReport report = new BugReport();
        report.setText(bugReportRequest.getText());
        report.setReportedAt(LocalDateTime.now());
        report.setUser(user);

        bugReportRepository.save(report);
    }

    @Override
    public List<BugReportResponse> getAllReports() {
        List<BugReport> listOfReports = bugReportRepository.findAllByOrderByReportedAtDesc();
        return listOfReports.stream().map(bugReportMapper::toDto).collect(Collectors.toList());
    }

    protected String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
