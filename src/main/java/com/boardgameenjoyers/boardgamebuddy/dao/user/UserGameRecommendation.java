package com.boardgameenjoyers.boardgamebuddy.dao.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "USER_GAME_RECOMMENDATION")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserGameRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "USER_FAVORITE_GAMES_ID")
    private UserFavoriteGames userFavoriteGames;
}
