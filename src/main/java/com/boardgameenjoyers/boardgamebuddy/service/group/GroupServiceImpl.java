package com.boardgameenjoyers.boardgamebuddy.service.group;

import com.boardgameenjoyers.boardgamebuddy.dao.game.GroupParticipantsRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.group.GroupRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.user.UserRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.group.Group;
import com.boardgameenjoyers.boardgamebuddy.dao.group.GroupParticipants;
import com.boardgameenjoyers.boardgamebuddy.dao.user.User;
import com.boardgameenjoyers.boardgamebuddy.service.user.ApplicationUser;
import com.boardgameenjoyers.boardgamebuddy.service.user.UserDTO;
import com.boardgameenjoyers.boardgamebuddy.service.user.UserMapper;
import com.boardgameenjoyers.boardgamebuddy.util.EntityOwnershipChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupParticipantsRepository groupParticipantsRepository;
    private final ApplicationUser applicationUser;
    private final UserMapper userMapper;
    private final EntityOwnershipChecker entityOwnershipChecker;


    @Override
    @Transactional
    public Group updateGroup(Group group, Long groupId) {
        groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));

        group.setId(groupId);
        group.setGroupName(group.getGroupName());
        group.setGroupParticipants(group.getGroupParticipants());
        group.setGameEntries(group.getGameEntries());

        return groupRepository.save(group);
    }

    @Override
    @Transactional
    public void deleteGroupById(Long groupId) {
        List<GroupParticipants> groupParticipants = groupParticipantsRepository.findByGroupId(groupId);

        if (isCurrentUserInGroupParticipants(groupParticipants)) {
            groupRepository.deleteById(groupId);
        } else {
            throw new AccessDeniedException("Unauthorized operation.");
        }
    }

    @Override
    public GroupDTS getGroupById(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Group not found with ID: " + id));

        if (isCurrentUserInGroup(group)) {
            return createGroupDTS(group, entityOwnershipChecker.isCurrentUserOwner(group.getCreatedBy()));
        } else {
            throw new AccessDeniedException("Unauthorized operation.");
        }
    }

    @Override
    public List<GroupDTS> getGroups(String group) {
        List<Group> groups = groupRepository.findByGroupName(group);

        List<Group> groupsWithCurrentUser = groups.stream()
                .filter(this::isCurrentUserInGroup)
                .toList();

        return groupsWithCurrentUser.stream()
                .map(userGroups -> createGroupDTS(userGroups, entityOwnershipChecker.isCurrentUserOwner(userGroups.getCreatedBy())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void createGroup(GroupDTO groupDTO, String username) {
        Group group = new Group();
        group.setGroupName(groupDTO.getGroupName());
        groupRepository.save(group);

        User user = userRepository.findByUserName(username).
                orElseThrow(() -> new AccessDeniedException("You need to log in first"));

        GroupParticipants groupParticipant = new GroupParticipants();
        groupParticipant.setUser(user);
        groupParticipant.setGroup(group);

        groupParticipantsRepository.save(groupParticipant);
        group.setGroupParticipants(List.of(groupParticipant));
    }

    @Override
    @Transactional
    public void addParticipantsToGroup(Long groupId, String userName) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with ID: " + groupId));

        validateUserGroupOwner(group);

        User user = userRepository.findByUserName(userName).orElseThrow(() -> new EntityNotFoundException("user not found with userName: " + userName));
        GroupParticipants groupParticipant = new GroupParticipants();
        groupParticipant.setGroup(group);
        groupParticipant.setUser(user);


        groupParticipantsRepository.save(groupParticipant);
    }

    @Override
    @Transactional
    public void removeParticipantFromGroup(Long groupId, Long participantId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with ID: " + groupId));

        validateUserGroupOwner(group);

        groupParticipantsRepository.deleteById(participantId);
    }

    @Override
    public List<UserDTO> getUsersInGroup(Long groupId) {
        List<GroupParticipants> groupParticipants = groupParticipantsRepository.findByGroupId(groupId);
        List<User> usersInGroup = groupParticipants.stream().map(GroupParticipants::getUser).toList();

        return usersInGroup.stream().map(userMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<GroupParticipantsDTS> getGroupParticipants(Long groupId) {
        List<GroupParticipants> groupParticipants = groupParticipantsRepository.findByGroupId(groupId);

        return groupParticipants.stream().map(groupParticipant -> new GroupParticipantsDTS(groupParticipant.getId(), groupParticipant.getUser().getUserName())).toList();
    }

    private GroupDTS createGroupDTS(Group group, boolean owner) {
        return new GroupDTS(group.getId(), group.getGroupName(), group.getCreated(), group.getCreatedBy(), owner);
    }

    private boolean isCurrentUserInGroup(Group group) {
        List<GroupParticipants> groupParticipants = groupParticipantsRepository.findByGroupId(group.getId());
        return isCurrentUserInGroupParticipants(groupParticipants);
    }

    private boolean isCurrentUserInGroupParticipants(List<GroupParticipants> groupParticipants) {
        return groupParticipants.stream().anyMatch(groupParticipant -> groupParticipant.getUser().getUserName().equals(applicationUser.getUserDetails().getUsername()));
    }

    private void validateUserGroupOwner(Group group) {
        if (!entityOwnershipChecker.isCurrentUserOwner(group.getCreatedBy())) {
            throw new AccessDeniedException("Unauthorized operation.");
        }
    }
}







