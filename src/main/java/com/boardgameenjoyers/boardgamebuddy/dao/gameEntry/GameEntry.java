package com.boardgameenjoyers.boardgamebuddy.dao.gameEntry;

import com.boardgameenjoyers.boardgamebuddy.dao.game.Game;
import com.boardgameenjoyers.boardgamebuddy.dao.group.Group;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "GAME_ENTRY")
public class GameEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DESCRIPTION")
    private String description;

    @CreatedDate
    @Column(name = "CREATED")
    private LocalDateTime created;

    @CreatedBy
    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "ENTRY_TITLE")
    private String entryTitle;

    @ManyToOne
    @JoinColumn(name = "GAME_ID")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "GROUP_ID")
    private Group group;

    @OneToMany(mappedBy = "gameEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameEntryComment> comments;

}
