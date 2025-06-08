package com.boardgameenjoyers.boardgamebuddy.service.game;

import com.boardgameenjoyers.boardgamebuddy.dao.game.Game;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.gameComment.GameComment;
import com.boardgameenjoyers.boardgamebuddy.dao.gameComment.GameCommentRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserRepository;
import com.boardgameenjoyers.boardgamebuddy.service.user.ApplicationUser;
import com.boardgameenjoyers.boardgamebuddy.util.EntityOwnershipChecker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameCommentTest {

    @Mock
    private GameCommentRepository gameCommentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ApplicationUser applicationUser;
    @Mock
    private GameRepository gameRepository;
    @InjectMocks
    private GameCommentServiceImpl gameCommentService;
    @Mock
    private EntityOwnershipChecker entityOwnershipChecker;

    @Test
    void shouldCreateNewGameComment() {
        //given
        Long gameId = 1L;
        String gameComment = "Great game";

        UserDetails userDetails = mock(UserDetails.class);
        when(applicationUser.getUserDetails()).thenReturn(userDetails);

        String username = "TestUser";
        when(userDetails.getUsername()).thenReturn(username);

        User user = new User();
        user.setUserName(username);
        when(userRepository.findByUserName(username)).thenReturn(Optional.of(user));

        Game game = new Game();
        game.setId(gameId);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        //when
        gameCommentService.addComment(gameId, gameComment);

        //then
        ArgumentCaptor<GameComment> captor = ArgumentCaptor.forClass(GameComment.class);
        verify(gameCommentRepository).save(captor.capture());

        GameComment savedGameComment = captor.getValue();
        assertThat(savedGameComment.getText()).isEqualTo(gameComment);
        assertThat(savedGameComment.getGame()).isEqualTo(game);
        assertThat(savedGameComment.getUser()).isEqualTo(user);

        verify(userRepository, times(1)).findByUserName(username);
        verify(gameRepository, times(1)).findById(gameId);
        verify(gameCommentRepository, times(1)).save(any(GameComment.class));
    }

    @Test
    void shouldNotCreateACommentWhenUserDoesNotExists() {
        //given
        Long gameId = 1L;
        String gameComment = "Great game";

        UserDetails userDetails = mock(UserDetails.class);
        when(applicationUser.getUserDetails()).thenReturn(userDetails);

        String username = "TestUser";
        when(userDetails.getUsername()).thenReturn(username);

        when(userRepository.findByUserName(username)).thenReturn(Optional.empty());

        //when
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () ->
                gameCommentService.addComment(gameId, gameComment));

        //then
        assertThat(thrown.getMessage()).isEqualTo("User is no logged in");

        verify(userRepository, times(1)).findByUserName(username);
        verifyNoInteractions(gameCommentRepository);
    }

    @Test
    void shouldNotCreateACommentWhenGameDoesNotExists() {
        //given
        Long gameId = 1L;
        String gameComment = "Great game";

        UserDetails userDetails = mock(UserDetails.class);
        when(applicationUser.getUserDetails()).thenReturn(userDetails);

        String username = "TestUser";
        when(userDetails.getUsername()).thenReturn(username);

        User user = new User();
        user.setUserName(username);
        when(userRepository.findByUserName(username)).thenReturn(Optional.of(user));

        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        //when
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () ->
                gameCommentService.addComment(gameId, gameComment));

        //then
        assertThat(thrown.getMessage()).isEqualTo("Game With id " + gameId + " does not exist");

        verify(userRepository, times(1)).findByUserName(username);
        verifyNoInteractions(gameCommentRepository);
    }

    @Test
    void shouldRemoveGameComment() {
        //given
        String username = "TestUser";
        Long gameCommentId = 1L;
        Long gameId = 1L;

        User user = new User();
        user.setId(1L);
        user.setUserName(username);

        GameComment gameComment = new GameComment();
        gameComment.setId(gameCommentId);
        gameComment.setUser(user);

        when(gameCommentRepository.findById(gameCommentId)).thenReturn(Optional.of(gameComment));
        when(entityOwnershipChecker.isCurrentUserOwner(username)).thenReturn(true);

        //when
        gameCommentService.deleteGameComment(gameId);

        //then
        Mockito.verify(gameCommentRepository, times(1)).delete(gameComment);
        Mockito.verify(entityOwnershipChecker, times(1)).isCurrentUserOwner(username);
    }

}