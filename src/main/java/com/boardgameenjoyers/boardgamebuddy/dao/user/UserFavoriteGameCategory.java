package com.boardgameenjoyers.boardgamebuddy.dao.user;

import com.boardgameenjoyers.boardgamebuddy.dao.gameCategory.GameCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "USER_FAVORITE_CATEGORY")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFavoriteGameCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GAME_CATEGORY_ID")
    private GameCategory gameCategory;
}
