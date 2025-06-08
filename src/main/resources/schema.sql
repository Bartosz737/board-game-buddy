create SCHEMA IF NOT EXISTS BOARD_GAME_BUDDY;

use BOARD_GAME_BUDDY;
create TABLE IF NOT EXISTS "USER" (
    id LONG PRIMARY KEY NOT NULL AUTO_INCREMENT,
    userName VARCHAR2(255) NOT NULL,
    email VARCHAR2(255),
    password VARCHAR2(255),
    created TIMESTAMP NOT NULL
);

create TABLE IF NOT EXISTS "GAME_ENTRY_COMMENT" (
    id LONG PRIMARY KEY NOT NULL AUTO_INCREMENT,
    text VARCHAR2(255) NOT NULL,
    created TIMESTAMP NOT NULL,
    game_entry_id LONG,
    user_id LONG
);

create TABLE IF NOT EXISTS GROUP_PARTICIPANTS(
    id LONG  PRIMARY KEY NOT NULL AUTO_INCREMENT,
    group_id LONG,
    user_id LONG
);

create TABLE IF NOT EXISTS GAME_PARTICIPANTS (
    id LONG PRIMARY KEY NOT NULL AUTO_INCREMENT,
    point LONG,
    game_entry_id LONG,
    user_id LONG
);

create TABLE IF NOT EXISTS "GROUP" (
    id  LONG  PRIMARY KEY NOT NULL AUTO_INCREMENT,
    group_name VARCHAR2(255) NOT NULL,
    created TIMESTAMP NOT NULL,
    createdBy VARCHAR2(255),
    wishlist_id LONG
);

create TABLE IF NOT EXISTS GAME_ENTRY(
    id LONG PRIMARY KEY NOT NULL AUTO_INCREMENT,
    description VARCHAR2(255),
    created TIMESTAMP NOT NULL,
    entry_title VARCHAR2(255),
    game_id LONG,
    group_id LONG
);

create TABLE IF NOT EXISTS WISHLIST(
    id LONG PRIMARY KEY  NOT NULL AUTO_INCREMENT,
    group_id LONG,
    game_id LONG
);

create TABLE IF NOT EXISTS GAME(
    id LONG PRIMARY KEY NOT NULL AUTO_INCREMENT,
    minimum_players INTEGER,
    maximum_players INTEGER,
    description VARCHAR2(255),
    game_title VARCHAR2(255) NOT NULL
);

create TABLE IF NOT EXISTS USER_ROLES(
    id LONG PRIMARY KEY  NOT NULL AUTO_INCREMENT,
    user_id LONG,
    role VARCHAR2(255)
);

alter table game
ADD FOREIGN KEY(game_entry_id) REFERENCES GAME_ENTRY(id);

alter table game
Add  FOREIGN KEY(user_id) REFERENCES "USER"(id);

alter table GROUP_PARTICIPANTS
add FOREIGN KEY(group_id) REFERENCES "GROUP"(id);

alter table GROUP_PARTICIPANTS
add FOREIGN KEY(user_id) REFERENCES "USER"(id);

alter table GAME_PARTICIPANTS
add FOREIGN KEY(game_entry_id) REFERENCES GAME_ENTRY(id);

alter table GAME_PARTICIPANTS
add FOREIGN KEY(user_id) REFERENCES "USER"(id);

alter table WISHLIST
add FOREIGN KEY(game_id) REFERENCES GAME(id);

alter table WISHLIST
add FOREIGN KEY(group_id) REFERENCES GROUP(id);

alter table GAME_ENTRY
add FOREIGN KEY(game_id) REFERENCES GAME(id);
















