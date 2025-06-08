package com.boardgameenjoyers.boardgamebuddy.dao.gameCategory;

import com.boardgameenjoyers.boardgamebuddy.dao.game.Game;
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
@Table(name = "GAME_CATEGORY_JOIN")
public class GameCategoryLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GAME_ID")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "GAME_CATEGORY_ID")
    private GameCategory gameCategory;
}
