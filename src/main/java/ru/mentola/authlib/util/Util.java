package ru.mentola.authlib.util;

import com.google.common.hash.Hashing;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;

@UtilityClass
public class Util {
    public String processHash(final String pass) {
        return getSHA256(pass);
    }

    private String getSHA256(final String str) {
        return Hashing.sha256()
                .hashString(str, StandardCharsets.UTF_8)
                .toString();
    }
}
