package com.boardgameenjoyers.boardgamebuddy.dao.game;

import com.boardgameenjoyers.boardgamebuddy.dao.enums.TeamOutcome;
import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.GameEntry;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "GAME_PARTICIPANTS")
public class GameParticipants {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "POINT")
    private Long point;

    @Enumerated(EnumType.STRING)
    @Column(name = "TEAM_OUTCOME")
    private TeamOutcome teamOutcome;

    @ManyToOne
    @JoinColumn(name = "GAME_ENTRY_ID")
    private GameEntry gameEntry;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "USER_PROFILE_ID")
    private UserProfile userProfile;
}
