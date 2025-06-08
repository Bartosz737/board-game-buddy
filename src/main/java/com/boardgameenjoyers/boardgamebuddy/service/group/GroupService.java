package com.boardgameenjoyers.boardgamebuddy.service.group;

import com.boardgameenjoyers.boardgamebuddy.dao.group.Group;
import com.boardgameenjoyers.boardgamebuddy.service.user.UserDTO;

import java.util.List;

public interface GroupService {

    Group updateGroup(Group group, Long groupId);

    void deleteGroupById(Long groupId);

    GroupDTS getGroupById(Long id);

    void createGroup(GroupDTO groupDTO, String username);

    void addParticipantsToGroup(Long groupId, String userName);

    void removeParticipantFromGroup(Long groupId, Long participantId);

    List<GroupDTS> getGroups(String search);

    List<UserDTO> getUsersInGroup(Long groupId);

    List<GroupParticipantsDTS> getGroupParticipants(Long groupId);
}
