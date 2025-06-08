package com.boardgameenjoyers.boardgamebuddy.dao.BugReport;

import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "BUG_REPORT")
public class BugReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TEXT")
    private String text;

    @CreatedDate
    @Column(name = "REPORTED")
    private LocalDateTime reportedAt;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
}
