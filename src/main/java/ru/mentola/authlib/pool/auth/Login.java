package ru.mentola.authlib.pool.auth;

import com.google.common.hash.Hashing;
import lombok.Getter;
import lombok.Setter;
import ru.mentola.authlib.model.DataUser;
import ru.mentola.authlib.util.Util;

import java.nio.charset.StandardCharsets;

@Getter
public final class Login {
    private final DataUser user;
    @Setter
    private int time;

    public Login(final DataUser user) {
        this.user = user;
        this.time = 60;
    }

    public boolean attempt(final String query) {
        final String queryHash = Util.processHash(user.getUsername(), Hashing.sha256()
                .hashString(query, StandardCharsets.UTF_8)
                .toString());
        return queryHash.equals(user.getPass());
    }
}
