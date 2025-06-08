package com.boardgameenjoyers.boardgamebuddy.dao.gameEntry;

import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "COMMENT")
public class GameEntryComment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TEXT")
    private String text;

    @CreatedDate
    @Column(name = "CREATED")
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "GAME_ENTRY_ID")
    private GameEntry gameEntry;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
}
