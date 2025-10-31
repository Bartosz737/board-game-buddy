package com.boardgameenjoyers.boardgamebuddy.service.user;

import com.boardgameenjoyers.boardgamebuddy.dao.game.Game;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.user.*;
import com.boardgameenjoyers.boardgamebuddy.service.request.userFavoriteList.AddGameToUserFavoriteListRequest;
import com.boardgameenjoyers.boardgamebuddy.service.response.UserFavoriteGamesResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserFavoriteGamesServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserFavoriteGamesRepository userFavoriteGamesRepository;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private UserFavoriteGamesMapper userFavoriteGamesMapper;
    @Spy
    @InjectMocks
    private UserFavoriteGamesServiceImpl userFavoriteGamesServiceImpl;

    @Test
    void shouldGetUserAllFavoriteGames() {
        //given
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        UserFavoriteGames firstFavoriteGame = new UserFavoriteGames();
        UserFavoriteGames secondFavoriteGame = new UserFavoriteGames();
        firstFavoriteGame.setId(1L);
        secondFavoriteGame.setId(2L);

        UserFavoriteGamesResponse firstResponse = new UserFavoriteGamesResponse(100L, "First game");
        UserFavoriteGamesResponse secondResponse = new UserFavoriteGamesResponse(2L, "Second game");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userFavoriteGamesRepository.findAllByUser_Id(userId)).thenReturn(List.of(firstFavoriteGame, secondFavoriteGame));
        when(userFavoriteGamesMapper.toResponse(firstFavoriteGame)).thenReturn(firstResponse);
        when(userFavoriteGamesMapper.toResponse(secondFavoriteGame)).thenReturn(secondResponse);

        //when
        List<UserFavoriteGamesResponse> userFavoriteGamesList = userFavoriteGamesServiceImpl.getUserFavoriteGames(userId);

        //then
        assertThat(userFavoriteGamesList).containsExactly(firstResponse, secondResponse);
        verify(userRepository).findById(userId);
        verify(userFavoriteGamesRepository).findAllByUser_Id(userId);
        verify(userFavoriteGamesMapper).toResponse(firstFavoriteGame);
        verify(userFavoriteGamesMapper).toResponse(secondFavoriteGame);
        verifyNoMoreInteractions(userFavoriteGamesRepository, userRepository);
    }

    @Test
    void shouldGetEmptyUserFavoriteList() {
        //given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userFavoriteGamesRepository.findAllByUser_Id(userId)).thenReturn(List.of());

        //when
        List<UserFavoriteGamesResponse> emptyFavoriteList = userFavoriteGamesServiceImpl.getUserFavoriteGames(userId);

        //then
        assertThat(emptyFavoriteList).isEmpty();
        verify(userRepository).findById(userId);
        verify(userFavoriteGamesRepository).findAllByUser_Id(userId);
        verifyNoMoreInteractions(userRepository, userFavoriteGamesRepository);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenUserDoesNotExists() {
        //given
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> userFavoriteGamesServiceImpl.getUserFavoriteGames(userId));

        //then
        assertTrue(thrown.getMessage().contains("User not found with id: " + userId));
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userFavoriteGamesRepository);
    }

    @Test
    void shouldAddGameToUserFavoriteList() {
        //given
        Long userId = 1L;
        Long gameId = 100L;
        AddGameToUserFavoriteListRequest request = new AddGameToUserFavoriteListRequest();
        request.setGameId(gameId);

        User user = new User();
        user.setId(userId);

        Game game = new Game();
        game.setId(gameId);

        doReturn(userId).when(userFavoriteGamesServiceImpl).getCurrentUserId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(userFavoriteGamesRepository.countByUserId(userId)).thenReturn(0L);
        when(userFavoriteGamesRepository.existsByUserIdAndGameId(userId, gameId)).thenReturn(false);

        //when
        userFavoriteGamesServiceImpl.addGameToUserFavoriteList(request);

        //then
        ArgumentCaptor<UserFavoriteGames> captor = ArgumentCaptor.forClass(UserFavoriteGames.class);
        verify(userFavoriteGamesRepository).save(captor.capture());

        UserFavoriteGames savedGamed = captor.getValue();
        assertThat(savedGamed.getUser()).isEqualTo(user);
        assertThat(savedGamed.getGame()).isEqualTo(game);

        verify(userRepository).findById(userId);
        verify(gameRepository).findById(gameId);
        verify(userRepository).findAndLockById(userId);
        verify(userFavoriteGamesRepository).countByUserId(userId);
        verify(userFavoriteGamesRepository).existsByUserIdAndGameId(userId, gameId);
        verifyNoMoreInteractions(userRepository, gameRepository, userFavoriteGamesRepository);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenUserDoesNotExistsWhenAttemptToAddGameToList() {
        //given
        Long userId = 1L;
        Long gameId = 100L;

        AddGameToUserFavoriteListRequest request = new AddGameToUserFavoriteListRequest();
        request.setGameId(gameId);

        doReturn(userId).when(userFavoriteGamesServiceImpl).getCurrentUserId();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> userFavoriteGamesServiceImpl.addGameToUserFavoriteList(request));

        //then
        assertThat(thrown.getMessage()).contains("User not found with id: " + userId);
        verify(userRepository).findById(userId);
        verifyNoInteractions(gameRepository, userFavoriteGamesRepository);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenGameDoesNotExists() {
        //given
        Long userId = 1L;
        Long gameId = 100L;

        User user = new User();
        user.setId(userId);

        AddGameToUserFavoriteListRequest request = new AddGameToUserFavoriteListRequest();
        request.setGameId(gameId);

        doReturn(userId).when(userFavoriteGamesServiceImpl).getCurrentUserId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        //when
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> userFavoriteGamesServiceImpl.addGameToUserFavoriteList(request));

        //then
        assertThat(thrown.getMessage()).contains("Game not found with id: " + request.getGameId());
        verify(gameRepository).findById(gameId);
        verify(userRepository).findById(userId);
        verifyNoInteractions(userFavoriteGamesRepository);

    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUserTryToAddMoreGamesToListThanLimit() {
        //given
        Long userId = 1L;
        Long gameId = 100L;

        User user = new User();
        user.setId(userId);

        Game game = new Game();
        game.setId(gameId);

        AddGameToUserFavoriteListRequest request = new AddGameToUserFavoriteListRequest();
        request.setGameId(gameId);

        doReturn(userId).when(userFavoriteGamesServiceImpl).getCurrentUserId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(userRepository.findAndLockById(userId)).thenReturn(null);
        when(userFavoriteGamesRepository.countByUserId(userId)).thenReturn(5L);

        //when
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> userFavoriteGamesServiceImpl.addGameToUserFavoriteList(request));

        //then
        assertThat(thrown.getMessage()).contains("Favorite games list is full (5), remove some first before you add");
        verify(userRepository).findById(userId);
        verify(gameRepository).findById(gameId);
        verify(userRepository).findAndLockById(userId);
        verify(userFavoriteGamesRepository).countByUserId(userId);
        verify(userFavoriteGamesRepository, never()).existsByUserIdAndGameId(anyLong(), anyLong());
        verify(userFavoriteGamesRepository, never()).save(any());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUserTryToAddGameThatAlreadyExistsInList() {
        //given
        Long userId = 1L;
        Long gameId = 1L;

        User user = new User();
        user.setId(userId);

        Game game = new Game();
        game.setId(gameId);

        AddGameToUserFavoriteListRequest request = new AddGameToUserFavoriteListRequest();
        request.setGameId(gameId);

        doReturn(userId).when(userFavoriteGamesServiceImpl).getCurrentUserId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(userFavoriteGamesRepository.countByUserId(userId)).thenReturn(1L);
        when(userFavoriteGamesRepository.existsByUserIdAndGameId(userId, gameId)).thenReturn(true);

        //when
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> userFavoriteGamesServiceImpl.addGameToUserFavoriteList(request));

        //then
        assertThat(thrown.getMessage()).contains("Game already in a list");
        verify(userRepository).findById(userId);
        verify(gameRepository).findById(gameId);
        verify(userRepository).findAndLockById(userId);
        verify(userFavoriteGamesRepository).countByUserId(userId);
        verify(userFavoriteGamesRepository).existsByUserIdAndGameId(userId, gameId);
        verify(userFavoriteGamesRepository, never()).save(any());
    }

    @Test
    void shouldRemoveGameFromUserFavoriteList() {
        //given
        Long userId = 1L;
        Long gameId = 100L;

        User user = new User();
        user.setId(userId);

        Game game = new Game();
        game.setId(gameId);

        UserFavoriteGames userFavoriteGame = new UserFavoriteGames();
        userFavoriteGame.setId(10L);
        userFavoriteGame.setId(gameId);
        userFavoriteGame.setUser(user);

        doReturn(userId).when(userFavoriteGamesServiceImpl).getCurrentUserId();
        when(userFavoriteGamesRepository.findByUserIdAndGameId(userId, gameId)).thenReturn(Optional.of(userFavoriteGame));

        //when
        userFavoriteGamesServiceImpl.removeGameFromUserFavoriteList(gameId);

        //then
        verify(userFavoriteGamesRepository).findByUserIdAndGameId(userId, gameId);
        verify(userFavoriteGamesRepository).delete(userFavoriteGame);
        verifyNoMoreInteractions(userFavoriteGamesRepository);
        verifyNoInteractions(gameRepository);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUserTryToRemoveGameThatNotExistsInList() {
        //given
        Long userId = 1L;
        Long gameId = 100L;

        doReturn(userId).when(userFavoriteGamesServiceImpl).getCurrentUserId();
        when(userFavoriteGamesRepository.findByUserIdAndGameId(userId, gameId)).thenReturn(Optional.empty());

        //when
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> userFavoriteGamesServiceImpl.removeGameFromUserFavoriteList(gameId));

        //then
        assertThat(thrown.getMessage()).contains("Game is not on a list");
        verify(userFavoriteGamesRepository).findByUserIdAndGameId(userId, gameId);
        verifyNoMoreInteractions(userFavoriteGamesRepository);
        verifyNoMoreInteractions(gameRepository);
    }
}