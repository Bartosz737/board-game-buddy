package com.boardgameenjoyers.boardgamebuddy.dao.BugReport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BugReportRepository extends JpaRepository<BugReport, Long> {
    List<BugReport> findAllByOrderByReportedAtDesc();
}
