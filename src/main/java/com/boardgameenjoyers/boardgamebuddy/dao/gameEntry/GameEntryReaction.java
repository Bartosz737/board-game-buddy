package com.boardgameenjoyers.boardgamebuddy.dao.gameEntry;

import com.boardgameenjoyers.boardgamebuddy.dao.enums.ReactionType;
import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "GAME_ENTRY_REACTION", uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_ID", "GAME_ENTRY_ID"})})
public class GameEntryReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "GAME_ENTRY_ID", nullable = false)
    private GameEntry gameEntry;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;
}


