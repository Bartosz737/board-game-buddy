package com.boardgameenjoyers.boardgamebuddy.service.game;

import com.boardgameenjoyers.boardgamebuddy.dao.game.GameParticipants;
import com.boardgameenjoyers.boardgamebuddy.dao.game.GameParticipantsRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.GameEntry;
import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.GameEntryRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.service.game.gameParticipants.GameParticipantsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameParticipantsServiceTest {

    @Mock
    private GameParticipantsRepository gameParticipantsRepository;
    @Mock
    private GameEntryRepository gameEntryRepository;
    @InjectMocks
    private GameParticipantsServiceImpl gameParticipantsService;

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