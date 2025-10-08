package com.boardgameenjoyers.boardgamebuddy.dao.game;

import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.GameEntry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "GAME")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "GAME_TITLE")
    private String gameTitle;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "MIN_PLAYERS")
    private Integer minPlayers;

    @Column(name = "MAX_PLAYERS")
    private Integer maxPLayers;

    @OneToMany(mappedBy = "game")
    private List<GameEntry> gameEntries;

    @Column(name = "GAME_TIME")
    private Integer gameTime;

    @Column(name = "AGE")
    private Integer age;


    public Game(Long id, String gameTitle, String description, Integer minPlayers, Integer maxPLayers, Integer age, Integer gameTime) {
        this.id = id;
        this.gameTitle = gameTitle;
        this.description = description;
        this.minPlayers = minPlayers;
        this.maxPLayers = maxPLayers;
        this.age = age;
        this.gameTime = gameTime;
    }
}
