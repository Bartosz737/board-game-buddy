package com.boardgameenjoyers.boardgamebuddy.service.user;

import com.boardgameenjoyers.boardgamebuddy.dao.gameCategory.GameCategory;
import com.boardgameenjoyers.boardgamebuddy.dao.gameCategory.GameCategoryRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.user.*;
import com.boardgameenjoyers.boardgamebuddy.service.request.AddGameCategoryToUserFavoriteListRequest;
import com.boardgameenjoyers.boardgamebuddy.service.request.AddGameToUserFavoriteListRequest;
import com.boardgameenjoyers.boardgamebuddy.service.response.UserFavoriteGameCategoryResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserFavoriteGameCategoryServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserFavoriteGameCategoryRepository userFavoriteGameCategoryRepository;
    @Mock
    private UserFavoriteGameCategoryMapper mapper;
    @Mock
    private GameCategoryRepository gameCategoryRepository;
    @Spy
    @InjectMocks
    private UserFavoriteGameCategoryServiceImpl userFavoriteGameCategoryServiceImpl;

    private final Long userId = 1L;
    private final Long gameCategoryId = 10L;

    @Test
    void shouldGetAllUserFavoriteGameCategories() {
        //given
        User user = new User();
        user.setId(userId);

        UserFavoriteGameCategory userFirstFavoriteGameCategory = new UserFavoriteGameCategory();
        userFirstFavoriteGameCategory.setId(1L);

        UserFavoriteGameCategoryResponse response = new UserFavoriteGameCategoryResponse(100L, "First game");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userFavoriteGameCategoryRepository.findAllByUserId(userId)).thenReturn(List.of(userFirstFavoriteGameCategory));
        when(mapper.toResponse(userFirstFavoriteGameCategory)).thenReturn(response);

        //when
        List<UserFavoriteGameCategoryResponse> listOfUsersFavoriteGameCategory = userFavoriteGameCategoryServiceImpl
                .getUserFavoriteGameCategoryList(userId);

        //then
        assertThat(listOfUsersFavoriteGameCategory).hasSize(1).containsExactly(response);
        verify(userRepository).findById(userId);
        verify(userFavoriteGameCategoryRepository).findAllByUserId(userId);
        verify(mapper).toResponse(userFirstFavoriteGameCategory);
        verifyNoMoreInteractions(userRepository, userFavoriteGameCategoryRepository, mapper);
    }

    @Test
    void shouldGetEmptyUserFavoriteGameCategoryList() {
        //given
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userFavoriteGameCategoryRepository.findAllByUserId(userId)).thenReturn(List.of());

        //when
        List<UserFavoriteGameCategoryResponse> emptyListOfUserFavoriteGameCategories =
                userFavoriteGameCategoryServiceImpl.getUserFavoriteGameCategoryList(userId);

        //then
        assertThat(emptyListOfUserFavoriteGameCategories).isEmpty();
        verify(userRepository).findById(userId);
        verify(userFavoriteGameCategoryRepository).findAllByUserId(userId);
        verifyNoMoreInteractions(userRepository, userFavoriteGameCategoryRepository);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenUserNotFound() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> userFavoriteGameCategoryServiceImpl.getUserFavoriteGameCategoryList(userId));

        //then
        assertThat(thrown.getMessage()).contains("User does not exists with id: " + userId);
        verify(userRepository).findById(userId);
        verifyNoInteractions(userFavoriteGameCategoryRepository, mapper);
    }

    @Test
    void shouldAddGameCategoryToUserFavoriteListWhenGameCategoryExist() {
        //given
        User user = new User();
        user.setId(userId);

        GameCategory gameCategory = new GameCategory();
        gameCategory.setId(gameCategoryId);

        AddGameCategoryToUserFavoriteListRequest request = new AddGameCategoryToUserFavoriteListRequest();
        request.setGameCategoryId(gameCategoryId);

        doReturn(userId).when(userFavoriteGameCategoryServiceImpl).getCurrentUserId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(gameCategoryRepository.findGameCategoryById(gameCategoryId)).thenReturn(Optional.of(gameCategory));
        when(userRepository.findAndLockById(userId)).thenReturn(null);
        when(userFavoriteGameCategoryRepository.countByUserId(userId)).thenReturn(0L);
        when(userFavoriteGameCategoryRepository.existsByUserIdAndGameCategoryId(userId, gameCategoryId)).thenReturn(false);

        //when
        userFavoriteGameCategoryServiceImpl.addGameCategoryAsFavorite(request);

        //then
        ArgumentCaptor<UserFavoriteGameCategory> captor = ArgumentCaptor.forClass(UserFavoriteGameCategory.class);
        verify(userFavoriteGameCategoryRepository).save(captor.capture());

        UserFavoriteGameCategory savedGameCategory = captor.getValue();
        assertThat(savedGameCategory.getUser()).isEqualTo(user);
        assertThat(savedGameCategory.getGameCategory()).isEqualTo(gameCategory);

        verify(userRepository).findById(userId);
        verify(gameCategoryRepository).findGameCategoryById(gameCategoryId);
        verify(userRepository).findAndLockById(userId);
        verify(userFavoriteGameCategoryRepository).countByUserId(userId);
        verify(userFavoriteGameCategoryRepository).existsByUserIdAndGameCategoryId(userId, gameCategoryId);
        verifyNoMoreInteractions(userRepository, gameCategoryRepository, userFavoriteGameCategoryRepository);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenGameCategoryNotFoundWhenAttemptToAddGameCategoryToList() {
        //given
        User user = new User();
        user.setId(userId);

        AddGameCategoryToUserFavoriteListRequest request = new AddGameCategoryToUserFavoriteListRequest();
        request.setGameCategoryId(gameCategoryId);

        doReturn(userId).when(userFavoriteGameCategoryServiceImpl).getCurrentUserId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(gameCategoryRepository.findGameCategoryById(gameCategoryId)).thenReturn(Optional.empty());

        //when
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> userFavoriteGameCategoryServiceImpl.addGameCategoryAsFavorite(request));

        //then
        assertThat(thrown.getMessage()).contains("Game category not found with id: " + request.getGameCategoryId());
        verify(userRepository).findById(userId);
        verify(gameCategoryRepository).findGameCategoryById(gameCategoryId);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userFavoriteGameCategoryRepository);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUserTryToAddMoreGameCategoriesToListThanLimit() {
        //given
        User user = new User();
        user.setId(userId);

        GameCategory gameCategory = new GameCategory();
        gameCategory.setId(gameCategoryId);

        AddGameCategoryToUserFavoriteListRequest request = new AddGameCategoryToUserFavoriteListRequest();
        request.setGameCategoryId(gameCategoryId);

        doReturn(userId).when(userFavoriteGameCategoryServiceImpl).getCurrentUserId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(gameCategoryRepository.findGameCategoryById(gameCategoryId)).thenReturn(Optional.of(gameCategory));
        when(userRepository.findAndLockById(userId)).thenReturn(null);
        when(userFavoriteGameCategoryRepository.countByUserId(userId)).thenReturn(5L);

        //when
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> userFavoriteGameCategoryServiceImpl.addGameCategoryAsFavorite(request));

        //then
        assertThat(thrown.getMessage()).contains("Favorite category list is full (5), remove some first before you add");
        verify(userRepository).findById(userId);
        verify(userRepository).findAndLockById(userId);
        verify(gameCategoryRepository).findGameCategoryById(gameCategoryId);
        verify(userFavoriteGameCategoryRepository).countByUserId(userId);
        verifyNoMoreInteractions(userRepository, userFavoriteGameCategoryRepository, gameCategoryRepository);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUserTryToAddAlreadyExistedGameCategoryInList() {
        //given
        User user = new User();
        user.setId(userId);

        GameCategory gameCategory = new GameCategory();
        gameCategory.setId(gameCategoryId);

        AddGameCategoryToUserFavoriteListRequest request = new AddGameCategoryToUserFavoriteListRequest();
        request.setGameCategoryId(gameCategoryId);

        doReturn(userId).when(userFavoriteGameCategoryServiceImpl).getCurrentUserId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(gameCategoryRepository.findGameCategoryById(gameCategoryId)).thenReturn(Optional.of(gameCategory));
        when(userRepository.findAndLockById(userId)).thenReturn(null);
        when(userFavoriteGameCategoryRepository.countByUserId(userId)).thenReturn(1L);
        when(userFavoriteGameCategoryRepository.existsByUserIdAndGameCategoryId(userId, gameCategoryId)).thenReturn(true);

        //when
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> userFavoriteGameCategoryServiceImpl.addGameCategoryAsFavorite(request));

        //then
        assertThat(thrown.getMessage()).contains("Game category already in a list");
        verify(userRepository).findById(userId);
        verify(userRepository).findAndLockById(userId);
        verify(gameCategoryRepository).findGameCategoryById(gameCategoryId);
        verify(userFavoriteGameCategoryRepository).countByUserId(userId);
        verify(userFavoriteGameCategoryRepository).existsByUserIdAndGameCategoryId(userId, gameCategoryId);
        verifyNoMoreInteractions(userRepository, gameCategoryRepository, userFavoriteGameCategoryRepository);
    }

    @Test
    void shouldRemoveGameCategoryFromFavoriteListWhenUserAndGameCategoryExists() {
        //given
        User user = new User();
        user.setId(userId);

        GameCategory gameCategory = new GameCategory();
        gameCategory.setId(gameCategoryId);

        UserFavoriteGameCategory userFavoriteGameCategory = new UserFavoriteGameCategory();
        userFavoriteGameCategory.setId(100L);
        userFavoriteGameCategory.setUser(user);
        userFavoriteGameCategory.setGameCategory(gameCategory);

        doReturn(userId).when(userFavoriteGameCategoryServiceImpl).getCurrentUserId();
        when(userFavoriteGameCategoryRepository.findByUserIdAndGameCategoryId(userId, gameCategoryId)).thenReturn(Optional.of(userFavoriteGameCategory));

        //when
        userFavoriteGameCategoryServiceImpl.removeGameCategoryFromUserFavoriteList(gameCategoryId);

        //then
        verify(userFavoriteGameCategoryRepository).findByUserIdAndGameCategoryId(userId, gameCategoryId);
        verify(userFavoriteGameCategoryRepository).delete(userFavoriteGameCategory);
        verifyNoInteractions(userRepository, gameCategoryRepository, mapper);
        verifyNoMoreInteractions(userFavoriteGameCategoryRepository);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenUserTryToRemoveGameCategoryThatNotExistInList() {
        //given
        User user = new User();
        user.setId(userId);

        GameCategory gameCategory = new GameCategory();
        gameCategory.setId(gameCategoryId);

        UserFavoriteGameCategory userFavoriteGameCategory = new UserFavoriteGameCategory();
        userFavoriteGameCategory.setId(100L);
        userFavoriteGameCategory.setUser(user);
        userFavoriteGameCategory.setGameCategory(gameCategory);

        doReturn(userId).when(userFavoriteGameCategoryServiceImpl).getCurrentUserId();
        when(userFavoriteGameCategoryRepository.findByUserIdAndGameCategoryId(userId, gameCategoryId)).thenReturn(Optional.empty());

        //when
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> userFavoriteGameCategoryServiceImpl.removeGameCategoryFromUserFavoriteList(gameCategoryId));

        //then
        assertThat(thrown.getMessage()).contains("Game category not found with id: " + gameCategoryId);
        verify(userFavoriteGameCategoryRepository).findByUserIdAndGameCategoryId(userId, gameCategoryId);
        verifyNoInteractions(userRepository, gameCategoryRepository, mapper);
        verifyNoMoreInteractions(userFavoriteGameCategoryRepository);
    }
}