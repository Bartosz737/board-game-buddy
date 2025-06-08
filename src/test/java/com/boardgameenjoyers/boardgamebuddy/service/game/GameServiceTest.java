package com.boardgameenjoyers.boardgamebuddy.service.game;

import com.boardgameenjoyers.boardgamebuddy.dao.game.Game;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.GameEntry;
import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.GameEntryRepository;
import com.boardgameenjoyers.boardgamebuddy.security.UserRole;
import com.boardgameenjoyers.boardgamebuddy.service.request.GamesPlayedPerMonth;
import com.boardgameenjoyers.boardgamebuddy.service.user.ApplicationUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;
    @Mock
    private ApplicationUser applicationUser;
    @Mock
    private GameMapper gameMapper;
    @Mock
    private GameEntryRepository gameEntryRepository;

    @InjectMocks
    private GameServiceImpl gameService;

    @Test
    void shouldCreateNewGameWhenUserIsAdmin() {
        // given
        GameDTO gameDTO = new GameDTO(1L, "title", "desc", 1, 2, 3, 45);
        when(applicationUser.getRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // when
        gameService.createNewGame(gameDTO);

        // then
        ArgumentCaptor<Game> gameCaptor = ArgumentCaptor.forClass(Game.class);

        Mockito.verify(applicationUser).getRoles();
        Mockito.verify(gameRepository).save(gameCaptor.capture());
        Mockito.verify(gameMapper).toDTO(any());

        Game game = gameCaptor.getValue();

        assertThat(game.getId()).isEqualTo(gameDTO.getGameId());
        assertThat(game.getGameTitle()).isEqualTo(gameDTO.getGameTitle());
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenUserIsNotAdmin() {
        // given
        GameDTO gameDTO = new GameDTO(1L, "title", "desc", 1, 2, 3, 45);
        when(applicationUser.getRoles()).thenReturn(Set.of());

        // when
        AccessDeniedException accessDeniedException = assertThrows(AccessDeniedException.class, () -> gameService.createNewGame(gameDTO));

        // then
        assertEquals("Unauthorized operation.", accessDeniedException.getMessage());
    }

    @Test
    void shouldGetGamesPlayedThisMonth_WhenGameExistsAndGameEntryExists() {
        // given
        Game game = new Game();
        Long gameId = 1L;
        game.setId(gameId);
        game.setGameTitle("Test Game");

        List<GameEntry> entries = List.of(new GameEntry(), new GameEntry());
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(gameEntryRepository.findAllGameEntriesWithGame(game)).thenReturn(entries);
        when(gameRepository.countGamesInGameEntriesLastMonth(eq(game), any(LocalDateTime.class)))
                .thenReturn(2L);

        // when
        GamesPlayedPerMonth result = gameService.getGamesPlayedPerMonth(gameId);

        // then
        assertThat(result).isNotNull()
                .extracting(GamesPlayedPerMonth::getGameId, GamesPlayedPerMonth::getGameTitle, GamesPlayedPerMonth::getGamesPlayed)
                .containsExactly(gameId, "Test Game", 2L);
    }

    @Test
    void shouldThrowRunTimeExceptionWhenGameDoesNotExists() {
        // given
        Long gameId = 1L;
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> gameService.getGamesPlayedPerMonth(gameId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Game with id 1 doesnt exists");

        // then
        verify(gameRepository).findById(gameId);
        verifyNoMoreInteractions(gameRepository, gameEntryRepository);
    }

    @Test
    void shouldThrowRunTimeExceptionWhenGameEntryDoesNotContainsGame() {
        // given
        Game game = new Game();
        Long gameId = 1L;
        game.setId(gameId);
        game.setGameTitle("Test Game");
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(gameEntryRepository.findAllGameEntriesWithGame(game)).thenReturn(Collections.emptyList());

        // when
        assertThatThrownBy(() -> gameService.getGamesPlayedPerMonth(gameId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Game entries with game 1 doesnt exists");
        // then
        verify(gameRepository).findById(gameId);
        verify(gameEntryRepository).findAllGameEntriesWithGame(game);
        verifyNoMoreInteractions(gameRepository);

    }
}