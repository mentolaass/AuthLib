package ru.mentola.authlib.storage;

import ru.mentola.authlib.model.DataUser;

import java.util.UUID;

public interface Storage {
    DataUser getUser(final UUID uuid);
    DataUser getUser(final String username);

    void addUser(final DataUser user);
}
