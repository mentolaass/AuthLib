package ru.mentola.authlib.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public final class DataUser {
    private final String username;
    private final String uuid;
    private final String pass;
}
