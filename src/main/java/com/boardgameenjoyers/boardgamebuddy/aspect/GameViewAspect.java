package com.boardgameenjoyers.boardgamebuddy.aspect;

import com.boardgameenjoyers.boardgamebuddy.service.game.GameViewService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class GameViewAspect {

    private final GameViewService gameViewService;

    @AfterReturning(
            pointcut = "execution(* com.boardgameenjoyers.boardgamebuddy.controller.gameController.GameController.getGame(..)) && args(gameId)",
            returning = "response"
    )
    public void countGameView(Long gameId, Object response) {
        gameViewService.recordView(gameId);
    }
}
