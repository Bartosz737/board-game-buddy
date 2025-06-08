package com.boardgameenjoyers.boardgamebuddy.dao.gameCategory;

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
@Table(name = "GAME_CATEGORY")
public class GameCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "GAME_CATEGORY_NAME")
    private String gameCategoryName;

    @Column(name = "GAME_CATEGORY_DESCRIPTION")
    private String gameCategoryDescription;
}
