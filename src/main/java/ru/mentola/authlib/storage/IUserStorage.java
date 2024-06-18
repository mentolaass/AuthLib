package ru.mentola.authlib.storage;

import ru.mentola.authlib.model.LoginUser;

import java.util.UUID;

public interface IUserStorage {
    LoginUser getUser(final UUID uuid);
    LoginUser getUser(final String username);
    void addUser(final LoginUser user);
}
