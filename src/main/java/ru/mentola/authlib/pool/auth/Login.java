package ru.mentola.authlib.pool.auth;

import lombok.Getter;
import lombok.Setter;
import ru.mentola.authlib.model.LoginUser;
import ru.mentola.authlib.util.Util;

@Getter
public final class Login {
    private final LoginUser user;
    @Setter
    private int time;

    public Login(final LoginUser user) {
        this.user = user;
        this.time = 60;
    }

    public boolean attempt(final String pass) {
        final String passHash = Util.processHash(pass);
        return passHash.equals(user.getPass());
    }
}
