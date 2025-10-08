package com.boardgameenjoyers.boardgamebuddy.dao.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USER_PROFILE")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "USER_ID", nullable = false, unique = true)
    private User user;

    @Column(name = "BIO")
    private String bio;

    @Column(name = "FAVORITE_GAME_TYPES")
    private String favoriteGameTypes;

    @Column(name = "GAMES_PLAYED")
    private long gamesPlayed = 0L;
}
