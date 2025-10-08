package com.boardgameenjoyers.boardgamebuddy.service.user;

import com.boardgameenjoyers.boardgamebuddy.dao.game.GameParticipantsRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserProfile;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserProfileRepository;
import com.boardgameenjoyers.boardgamebuddy.util.EntityOwnershipChecker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;


import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;
    @Mock
    private GameParticipantsRepository gameParticipantsRepository;
    @Mock
    private EntityOwnershipChecker entityOwnershipChecker;

    @InjectMocks
    private UserProfileServiceImpl userProfileServiceImpl;

    @Test
    void shouldGetUserProfileWhenUserExist() {
        //given
        Long userId = 1L;
        String username = "Tester";
        String email = "Tester@gmail.com";
        String bio = "";

        User user = new User();
        user.setUserName(username);
        user.setEmail(email);

        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setBio(bio);

        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(userProfile));
        when(gameParticipantsRepository.countByUserProfileUserId(userId)).thenReturn(5L);

        //when
        UserProfileDTO userProfileDTO = userProfileServiceImpl.getUserProfile(userId);

        //then
        assertThat(userProfileDTO).isNotNull();
        assertThat(userProfileDTO.getUsername()).isEqualTo(username);
        assertThat(userProfileDTO.getEmail()).isEqualTo(email);
        assertThat(userProfileDTO.getBio()).isEqualTo(bio);
        assertThat(userProfileDTO.getGamesPlayed()).isEqualTo(5L);

        verify(userProfileRepository).findByUserId(userId);
        verify(gameParticipantsRepository).countByUserProfileUserId(userId);
        verifyNoMoreInteractions(userProfileRepository, gameParticipantsRepository);

    }

    @Test
    void shouldThrownEntityNotFoundExceptionWhenUserDoesNotExist() {
        //given
        Long userId = 1L;

        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.empty());

        //when
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> userProfileServiceImpl.getUserProfile(userId));

        //then
        assertThat(thrown.getMessage()).isEqualTo("User not found");

        verifyNoMoreInteractions(userProfileRepository);
        verifyNoInteractions(gameParticipantsRepository);
    }

    @Test
    void shouldCreateUserProfileWhenUserExist() {
        //given
        String username = "Tester";
        String email = "Tester@gmail.com";

        User user = new User();
        user.setUserName(username);
        user.setEmail(email);

        when(userProfileRepository.save(any(UserProfile.class))).thenAnswer(inv -> {
            UserProfile profile = inv.getArgument(0, UserProfile.class);
            profile.setId(1L);
            return profile;
        });

        //when
        userProfileServiceImpl.createProfileForUser(user);

        //then
        ArgumentCaptor<UserProfile> captor = ArgumentCaptor.forClass(UserProfile.class);
        verify(userProfileRepository).save(captor.capture());

        UserProfile savedUserProfile = captor.getValue();

        assertThat(savedUserProfile.getUser()).isEqualTo(user);
        assertThat(savedUserProfile.getBio()).isEqualTo("");
        assertThat(savedUserProfile.getGamesPlayed()).isZero();

        verifyNoMoreInteractions(userProfileRepository);

    }

    @Test
    void shouldThrowNullPointerExceptionWhenUserIsNull() {

        //when
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> userProfileServiceImpl.createProfileForUser(null));

        //then
        assertThat(thrown.getMessage()).isEqualTo(" User must exist");
        verifyNoMoreInteractions(userProfileRepository);
    }


    @Test
    void shouldUpdateBioInUserProfileWhenUserProfileExists() {
        //given
        Long userId = 1L;

        String username = "Tester";
        String email = "Tester@gmail.com";
        String oldBio = "";
        String newBio = "New bio";

        User user = new User();
        user.setUserName(username);
        user.setEmail(email);

        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setBio(oldBio);

        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setBio(newBio);

        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(userProfile));

        when(entityOwnershipChecker.isCurrentUserOwnerOfUserProfile(username)).thenReturn(true);

        //when
        userProfileServiceImpl.updateProfileForUser(userId, userProfileDTO);

        //then
        ArgumentCaptor<UserProfile> captor = ArgumentCaptor.forClass(UserProfile.class);
        verify(userProfileRepository).save(captor.capture());

        UserProfile savedUserProfile = captor.getValue();

        assertThat(savedUserProfile.getUser()).isEqualTo(user);
        assertThat(savedUserProfile.getBio()).isEqualTo(newBio);

        verify(userProfileRepository).findByUserId(userId);
        verify(entityOwnershipChecker).isCurrentUserOwnerOfUserProfile(username);
        verify(userProfileRepository).save(userProfile);
        verifyNoMoreInteractions(userProfileRepository, entityOwnershipChecker);
        verifyNoInteractions(gameParticipantsRepository);


    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenUserProfileNotFound() {
        //given
        Long userId = 1L;
        UserProfileDTO userProfileDTO = new UserProfileDTO();

        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.empty());

        //when
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> userProfileServiceImpl.updateProfileForUser(userId, userProfileDTO));
        //then
        assertThat(thrown.getMessage()).isEqualTo("User not found");
        verify(userProfileRepository).findByUserId(userId);
        verify(userProfileRepository, never()).save(any());
        verifyNoMoreInteractions(userProfileRepository);
        verifyNoInteractions(gameParticipantsRepository, entityOwnershipChecker);
    }

    @Test
    void shouldThrowAccessDeniedWhenUserTryUpdateProfileButHeIsNotAnOwnerOfTheProfile() {
        //given
        Long userId = 1L;

        String username = "Tester";
        String email = "Tester@gmail.com";
        String oldBio = "";
        String newBio = "New bio";

        User user = new User();
        user.setUserName(username);
        user.setEmail(email);

        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setBio(oldBio);

        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setBio(newBio);

        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(userProfile));

        when(entityOwnershipChecker.isCurrentUserOwnerOfUserProfile(username)).thenReturn(false);

        //when
        AccessDeniedException thrown = assertThrows(AccessDeniedException.class,
                () -> userProfileServiceImpl.updateProfileForUser(userId, userProfileDTO));

        //then
        assertThat(thrown.getMessage()).isEqualTo("It is not your profile, you do not have permission to change anything");
        verify(userProfileRepository).findByUserId(userId);
        verify(entityOwnershipChecker).isCurrentUserOwnerOfUserProfile(username);
        verifyNoInteractions(gameParticipantsRepository);
        verifyNoMoreInteractions(entityOwnershipChecker, userProfileRepository);
    }

}