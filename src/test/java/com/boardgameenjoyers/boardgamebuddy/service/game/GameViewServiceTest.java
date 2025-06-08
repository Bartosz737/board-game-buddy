package com.boardgameenjoyers.boardgamebuddy.service.game;

import com.boardgameenjoyers.boardgamebuddy.dao.game.Game;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameView;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameViewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameViewServiceTest {

    @Mock
    private GameRepository gameRepository;
    @Mock
    private GameViewRepository gameViewRepository;
    @InjectMocks
    private GameViewServiceImpl gameViewService;
    @Mock
    private GameViewMapper gameViewMapper;

    @Test
    void shouldCreateANewViewWhenAGameHasNotRecordYet() {
        //given
        Long gameId = 1L;

        Game game = new Game();
        game.setId(gameId);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(gameViewRepository.findByGame(game)).thenReturn(Optional.empty());

        //when
        gameViewService.recordView(gameId);

        //then
        ArgumentCaptor<GameView> captor = ArgumentCaptor.forClass(GameView.class);
        verify(gameViewRepository).save(captor.capture());

        GameView savedGameView = captor.getValue();
        assertThat(savedGameView.getGame()).isEqualTo(game);
        assertThat(savedGameView.getCountView()).isEqualTo(1);

        verify(gameViewRepository, times(1)).save(savedGameView);
        verify(gameRepository, times(1)).findById(gameId);
        verify(gameViewRepository, times(1)).findByGame(game);
    }

    @Test
    void shouldIncrementTheViewCountWhenUserVisitsAGame() {
        //given
        Long gameId = 1L;

        Game game = new Game();
        game.setId(gameId);

        GameView gameView = new GameView();
        gameView.setGame(game);
        gameView.setCountView(2L);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(gameViewRepository.findByGame(game)).thenReturn(Optional.of(gameView));

        //when
        gameViewService.recordView(gameId);

        //then
        ArgumentCaptor<GameView> captor = ArgumentCaptor.forClass(GameView.class);
        verify(gameViewRepository).save(captor.capture());

        GameView savedGameView = captor.getValue();
        assertThat(savedGameView.getGame()).isEqualTo(game);
        assertThat(savedGameView.getCountView()).isEqualTo(3L);

        verify(gameViewRepository, times(1)).findByGame(game);
        verify(gameRepository, times(1)).findById(gameId);
        verify(gameViewRepository, times(1)).save(savedGameView);

    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenGameDoesNotExists() {
        //given
        Long gameId = 1L;

        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        //when
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> gameViewService.recordView(gameId));

        //then
        assertThat(thrown.getMessage()).isEqualTo("game not found");

        verifyNoMoreInteractions(gameRepository);
        verifyNoInteractions(gameViewRepository);

    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenGettingViewsForNoExistingGame() {
        //given
        Long gameId = 1L;

        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        //when
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> gameViewService.getGameViews(gameId));

        //then
        assertThat(thrown.getMessage()).isEqualTo("game not found");

        verifyNoMoreInteractions(gameRepository);
        verifyNoInteractions(gameViewRepository, gameViewMapper);
    }


    @Test
    void shouldReturnGameViewDTOWhenGameExists() {
        //given
        Long gameId = 1L;
        String gameTitle = "Test Game";

        Game game = new Game();
        game.setId(gameId);
        game.setGameTitle(gameTitle);

        GameView gameView = new GameView();
        gameView.setGame(game);
        gameView.setCountView(2L);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(gameViewRepository.findByGame(game)).thenReturn(Optional.of(gameView));

        GameViewDTO expectedGameViewDTO = new GameViewDTO(gameId, gameTitle, 2L);
        when(gameViewMapper.toDTO(game, gameView)).thenReturn((expectedGameViewDTO));

        //when
        GameViewDTO actualDTO = gameViewService.getGameViews(gameId);

        //then
        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getGameId()).isEqualTo(gameId);
        assertThat(actualDTO.getGameTitle()).isEqualTo(gameTitle);
        assertThat(actualDTO.getViewCounts()).isEqualTo(2L);

        verify(gameRepository, times(1)).findById(gameId);
        verify(gameViewRepository, times(1)).findByGame(game);
        verify(gameViewMapper, times(1)).toDTO(game, gameView);
    }
}