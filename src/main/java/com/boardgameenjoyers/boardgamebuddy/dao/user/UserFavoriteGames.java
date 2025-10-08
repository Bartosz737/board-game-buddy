package com.boardgameenjoyers.boardgamebuddy.dao.user;

import com.boardgameenjoyers.boardgamebuddy.dao.game.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "USER_FAVORITE_GAMES")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFavoriteGames {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "GAME_ID")
    private Game game;
}
