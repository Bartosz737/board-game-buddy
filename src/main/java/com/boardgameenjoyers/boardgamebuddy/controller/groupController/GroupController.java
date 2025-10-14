package com.boardgameenjoyers.boardgamebuddy.controller.groupController;

import com.boardgameenjoyers.boardgamebuddy.service.group.*;
import com.boardgameenjoyers.boardgamebuddy.service.user.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("group")
@CrossOrigin
public class GroupController implements Serializable {
    private final GroupService groupService;
    private final CurrentUserService currentUserService;

    @PostMapping("creation")
    @ResponseBody
    public ResponseEntity<Void> createGroup(@RequestBody GroupDTO groupDTO) {
        groupService.createGroup(groupDTO, currentUserService.getUsername());
        return ResponseEntity.ok().build();
    }

    @PostMapping("{groupId}/participant")
    public ResponseEntity<Void> addParticipantsToGroup(@PathVariable Long groupId, @RequestBody String userName) {
        groupService.addParticipantsToGroup(groupId, userName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("{groupId}/participants")
    public ResponseEntity<List<GroupParticipantsDTS>> getGroupParticipants(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupService.getGroupParticipants(groupId));
    }

    @GetMapping("/groups")
    @ResponseBody
    public ResponseEntity<List<GroupDTS>> getAllGroups(@RequestParam(required = false) String search) {
        List<GroupDTS> groups = groupService.getGroups(search);
        return ResponseEntity.ok(groups);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroupById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}")
    @ResponseBody
    public ResponseEntity<GroupDTS> getGroupById(@PathVariable Long id) {
        GroupDTS group = groupService.getGroupById(id);
        return ResponseEntity.ok(group);
    }

    @DeleteMapping("{groupId}/participant/{id}")
    public ResponseEntity<?> removeParticipantsFromGroup(@PathVariable Long groupId, @PathVariable Long id) {
        groupService.removeParticipantFromGroup(groupId, id);
        return ResponseEntity.ok().build();
    }
}


