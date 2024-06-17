package ru.mentola.authlib.util;

import com.google.common.hash.Hashing;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;

@UtilityClass
public class Util {
    public String processHash(final String username, final String hashPass) {
        return Hashing.sha256()
                .hashString(username + hashPass, StandardCharsets.UTF_8)
                .toString();
    }
}
