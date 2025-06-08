package com.boardgameenjoyers.boardgamebuddy.service.game;

import com.boardgameenjoyers.boardgamebuddy.dao.game.GameParticipants;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameParticipantsRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GroupParticipantsRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.GameEntry;
import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.GameEntryRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.group.GroupParticipants;
import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.service.request.AddGameParticipantToGameEntryRequest;
import com.boardgameenjoyers.boardgamebuddy.util.EntityOwnershipChecker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameParticipantsServiceTest {

    @Mock
    private GameParticipantsRepository gameParticipantsRepository;
    @Mock
    private GroupParticipantsRepository groupParticipantsRepository;
    @Mock
    private GameEntryRepository gameEntryRepository;
    @InjectMocks
    private GameParticipantsServiceImpl gameParticipantsService;
    @Mock
    private EntityOwnershipChecker entityOwnershipChecker;

    @Test
    void shouldAddGameParticipantsToGameEntryWhenUserIsAnOwner() {
        //given
        Long gameEntryId = 1L;
        String username = "TestUser";
        Long groupParticipantId = 10L;
        Long points = 10L;

        User user = new User();
        user.setId(1L);
        user.setUserName(username);

        when(entityOwnershipChecker.isCurrentUserOwner(username)).thenReturn(true);

        AddGameParticipantToGameEntryRequest request = new AddGameParticipantToGameEntryRequest(
                user.getId(),
                groupParticipantId,
                points
        );
        GameEntry gameEntry = new GameEntry();
        gameEntry.setId(gameEntryId);
        gameEntry.setCreatedBy(username);

        GroupParticipants groupParticipants = new GroupParticipants();
        groupParticipants.setId(groupParticipantId);
        groupParticipants.setUser(user);

        when(gameEntryRepository.findById(gameEntryId)).thenReturn(Optional.of(gameEntry));
        when(groupParticipantsRepository.findById(groupParticipantId)).thenReturn(Optional.of(groupParticipants));

        //when
        gameParticipantsService.addGameParticipantToGameEntry(request, gameEntryId);

        //then
        ArgumentCaptor<GameParticipants> captor = ArgumentCaptor.forClass(GameParticipants.class);
        verify(gameParticipantsRepository).save(captor.capture());

        GameParticipants savedGameParticipant = captor.getValue();
        assertThat(savedGameParticipant.getGameEntry()).isEqualTo(gameEntry);
        assertThat(savedGameParticipant.getUser()).isEqualTo(user);
        assertThat(savedGameParticipant.getPoint()).isEqualTo(points);

        verify(gameEntryRepository, times(1)).findById(gameEntryId);
        verify(groupParticipantsRepository, times(1)).findById(groupParticipantId);
        verify(gameParticipantsRepository, times(1)).save(savedGameParticipant);
    }

    @Test
    void shouldThrowAccessDeniesExceptionWhenUserIsNotAnOwner() {
        //given
        Long gameEntryId = 1L;
        String username = "TestUser";
        String userOwner = "owner";
        Long groupParticipantId = 10L;
        Long points = 10L;

        User user = new User();
        user.setId(1L);
        user.setUserName(username);

        GameEntry gameEntry = new GameEntry();
        gameEntry.setId(gameEntryId);
        gameEntry.setCreatedBy(userOwner);

        AddGameParticipantToGameEntryRequest request = new AddGameParticipantToGameEntryRequest(
                user.getId(),
                groupParticipantId,
                points
        );
        when(gameEntryRepository.findById(gameEntryId)).thenReturn(Optional.of(gameEntry));

        //when
        AccessDeniedException thrown = assertThrows(AccessDeniedException.class,
                () -> gameParticipantsService.addGameParticipantToGameEntry(request, gameEntryId));

        //then
        assertThat(thrown.getMessage()).isEqualTo("Unauthorized operation.");

        verify(gameEntryRepository, times(1)).findById(gameEntryId);
        verifyNoInteractions(groupParticipantsRepository, gameParticipantsRepository);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenGameEntryDoesNotExists() {
        //given
        Long gameEntryId = 1L;
        String username = "TestUser";
        Long groupParticipantId = 10L;
        Long points = 10L;

        User user = new User();
        user.setId(1L);
        user.setUserName(username);

        AddGameParticipantToGameEntryRequest request = new AddGameParticipantToGameEntryRequest(
                user.getId(),
                groupParticipantId,
                points
        );

        GroupParticipants groupParticipants = new GroupParticipants();
        groupParticipants.setId(groupParticipantId);
        groupParticipants.setUser(user);

        when(gameEntryRepository.findById(gameEntryId)).thenReturn(Optional.empty());

        //when
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> gameParticipantsService.addGameParticipantToGameEntry(request, gameEntryId));

        //then
        assertThat(thrown).isInstanceOf(EntityNotFoundException.class);

        verify(gameEntryRepository, times(1)).findById(gameEntryId);
        verifyNoInteractions(groupParticipantsRepository, gameParticipantsRepository);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenGroupParticipantDoesNotExists() {
        Long gameEntryId = 1L;
        String username = "TestUser";
        Long groupParticipantId = 10L;
        Long points = 100L;

        User user = new User();
        user.setId(1L);
        user.setUserName(username);

        GameEntry gameEntry = new GameEntry();
        gameEntry.setId(gameEntryId);
        gameEntry.setCreatedBy(username);

        AddGameParticipantToGameEntryRequest request = new AddGameParticipantToGameEntryRequest(
                user.getId(),
                groupParticipantId,
                points
        );

        when(gameEntryRepository.findById(gameEntryId)).thenReturn(Optional.of(gameEntry));
        when(entityOwnershipChecker.isCurrentUserOwner(username)).thenReturn(true);
        when(groupParticipantsRepository.findById(groupParticipantId)).thenReturn(Optional.empty());

        //when
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> gameParticipantsService.addGameParticipantToGameEntry(request, gameEntryId));

        //then
        assertThat(thrown).isInstanceOf(EntityNotFoundException.class);

        verify(groupParticipantsRepository, times(1)).findById(groupParticipantId);
        verify(gameEntryRepository, times(1)).findById(gameEntryId);
        verifyNoInteractions(gameParticipantsRepository);

    }

    @Test
    void shouldRemoveParticipantFromGameEntry() {
        //given
        Long gameEntryId = 10L;
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setUserName("TestUser");

        GameEntry gameEntry = new GameEntry();
        gameEntry.setId(gameEntryId);

        GameParticipants gameParticipants = new GameParticipants();
        gameParticipants.setUser(user);
        gameParticipants.setGameEntry(gameEntry);

        when(gameParticipantsRepository.findByUserId(userId)).thenReturn(Optional.of(gameParticipants));

        //when
        gameParticipantsService.delete(userId);

        //then
        verify(gameParticipantsRepository, times(1)).delete(gameParticipants);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenGameParticipantDoesNotExists() {
        //given
        Long gameEntryId = 10L;
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setUserName("TestUser");

        GameEntry gameEntry = new GameEntry();
        gameEntry.setId(gameEntryId);

        GameParticipants gameParticipants = new GameParticipants();
        gameParticipants.setUser(user);
        gameParticipants.setGameEntry(gameEntry);

        when(gameParticipantsRepository.findByUserId(userId)).thenReturn(Optional.empty());

        //when
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> gameParticipantsService.delete(userId));

        //then
        assertThat(thrown.getMessage()).isEqualTo("Participant not found");

        verify(gameParticipantsRepository, times(1)).findByUserId(userId);
        verify(gameParticipantsRepository, never()).delete(gameParticipants);
        verifyNoMoreInteractions(gameEntryRepository);
    }
}