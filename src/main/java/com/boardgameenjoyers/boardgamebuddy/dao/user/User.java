package com.boardgameenjoyers.boardgamebuddy.dao.user;

import com.boardgameenjoyers.boardgamebuddy.dao.game.GameParticipants;
import com.boardgameenjoyers.boardgamebuddy.dao.gameEntry.GameEntryComment;
import com.boardgameenjoyers.boardgamebuddy.dao.group.GroupParticipants;
import com.boardgameenjoyers.boardgamebuddy.security.UserRole;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USERNAME")
    private String userName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "CREATED", updatable = false)
    private LocalDateTime created = LocalDateTime.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<GameEntryComment> gameEntryComments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<GameParticipants> gameParticipants;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<GroupParticipants> groupParticipants;

    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "USER_ID"))
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;
    
    public User(String userName, String email, String password, LocalDateTime created, Set<UserRole> roles) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.created = created;
        this.roles = roles;
    }
}

