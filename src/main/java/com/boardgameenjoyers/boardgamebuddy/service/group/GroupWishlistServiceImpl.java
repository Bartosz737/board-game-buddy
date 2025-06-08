package com.boardgameenjoyers.boardgamebuddy.service.group;

import com.boardgameenjoyers.boardgamebuddy.dao.game.GameRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.group.GroupRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.group.GroupWishlistRepository;
import com.boardgameenjoyers.boardgamebuddy.dao.game.Game;
import com.boardgameenjoyers.boardgamebuddy.dao.group.Group;
import com.boardgameenjoyers.boardgamebuddy.dao.group.GroupWishlist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupWishlistServiceImpl implements GroupWishlistService {

    private final GroupWishlistRepository groupWishlistRepository;
    private final GroupRepository groupRepository;
    private final GameRepository gameRepository;

    @Override
    @Transactional
    public List<GroupWishlistDTS> getWishlist(Long groupId) {
        List<GroupWishlist> groupWishlist = groupWishlistRepository.findByGroupId(groupId);
        return groupWishlist.stream().map(groupWishlistEntry -> new GroupWishlistDTS(groupWishlistEntry.getId(), groupWishlistEntry.getGame().getGameTitle())).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addGameToGroupWishlist(Long groupId, Long gameId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new EntityNotFoundException("Cannot find Group by id: " + groupId));
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new EntityNotFoundException("Cannot find Game by id: " + gameId));
        GroupWishlist groupWishlist = new GroupWishlist();
        groupWishlist.setGroup(group);
        groupWishlist.setGame(game);
        groupWishlistRepository.save(groupWishlist);
    }

    @Override
    public void removeFromWishlist(Long id) {
        groupWishlistRepository.deleteById(id);
    }
}
