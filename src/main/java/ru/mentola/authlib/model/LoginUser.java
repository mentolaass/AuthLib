package ru.mentola.authlib.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public final class LoginUser {
    private final String username;
    private final String uuid;
    private final String pass;
}
