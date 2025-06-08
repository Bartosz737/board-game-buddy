package com.boardgameenjoyers.boardgamebuddy.service.bugReport;

import com.boardgameenjoyers.boardgamebuddy.dao.BugReport.BugReport;
import com.boardgameenjoyers.boardgamebuddy.dao.BugReport.BugReportRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserRepository;
import com.boardgameenjoyers.boardgamebuddy.service.request.BugReportRequest;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.boardgameenjoyers.boardgamebuddy.service.response.BugReportResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BugReportServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BugReportRepository bugReportRepository;
    @Mock
    private BugReportMapper bugReportMapper;
    @InjectMocks
    private BugReportServiceImpl bugReportService;

    @Test
    void shouldSaveBugReportWhenUserExists() {
        //given
        String username = "TestUser";
        BugReportRequest bugReportRequest = new BugReportRequest();
        bugReportRequest.setText("Test text");

        User user = new User();
        user.setUserName(username);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUserName(username)).thenReturn(Optional.of(user));

        //when
        bugReportService.reportBug(bugReportRequest);

        //then
        ArgumentCaptor<BugReport> captor = ArgumentCaptor.forClass(BugReport.class);
        verify(bugReportRepository).save(captor.capture());

        BugReport savedCaptor = captor.getValue();

        assertThat(savedCaptor.getText()).isEqualTo("Test text");
        assertThat(savedCaptor.getUser()).isSameAs(user);
        assertThat(savedCaptor.getReportedAt()).isBeforeOrEqualTo(LocalDateTime.now());

        verify(userRepository, times(1)).findByUserName(username);
    }

    @Test
    void shouldNotSaveBugReportWhenUserDoesNotExists() {
        //given
        String username = "TestUser";
        BugReportRequest bugReportRequest = new BugReportRequest();
        bugReportRequest.setText("Test text");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUserName(username)).thenReturn(Optional.empty());

        //when
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () ->
                bugReportService.reportBug(bugReportRequest));

        //then

        assertThat(thrown.getMessage()).isEqualTo("user not found");

        verify(userRepository, times(1)).findByUserName(username);
        verifyNoInteractions(bugReportRepository);

    }

    @Test
    void shouldThrowWhenBugReportRequestIsNull() {

        // when
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> bugReportService.reportBug(null)
        );

        //then
        assertThat(thrown.getMessage()).isEqualTo("Bug report request must not be null");

        verifyNoInteractions(userRepository, bugReportRepository);
    }

    @Test
    void shouldGetAllReportsInDescendingOrder() {
        //given
        BugReport report1 = new BugReport();
        report1.setId(1L);
        report1.setText("Bug one");
        report1.setReportedAt(LocalDateTime.now().minusDays(1));

        BugReport report2 = new BugReport();
        report2.setId(2L);
        report2.setText("Bug two");
        report2.setReportedAt(LocalDateTime.now().minusDays(1));

        List<BugReport> reports = List.of(report2, report1);

        BugReportResponse response1 = new BugReportResponse();
        response1.setText("Bug two");

        BugReportResponse response2 = new BugReportResponse();
        response2.setText("Bug one");

        when(bugReportRepository.findAllByOrderByReportedAtDesc()).thenReturn(reports);
        when(bugReportMapper.toDto(report2)).thenReturn(response1);
        when(bugReportMapper.toDto(report1)).thenReturn(response2);

        //when
        List<BugReportResponse> result = bugReportService.getAllReports();

        //then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getText()).isEqualTo("Bug two");
        assertThat(result.get(1).getText()).isEqualTo("Bug one");

        verify(bugReportRepository, times(1)).findAllByOrderByReportedAtDesc();
        verify(bugReportMapper, times(1)).toDto(report2);
        verify(bugReportMapper, times(1)).toDto(report1);
        verifyNoMoreInteractions(bugReportRepository, bugReportMapper);

    }
}