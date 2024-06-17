package ru.mentola.authlib.pool.auth;

import com.google.common.hash.Hashing;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.mentola.authlib.AuthLib;
import ru.mentola.authlib.model.DataUser;
import ru.mentola.authlib.storage.impl.UserStorage;
import ru.mentola.authlib.util.Util;

import java.nio.charset.StandardCharsets;

@Getter
public final class Register {
    private final Player player;
    @Setter
    private int time;

    public Register(final Player player) {
        this.player = player;
        this.time = 60;
    }

    public boolean attempt(final String query) {
        Bukkit.getScheduler().runTaskAsynchronously(AuthLib.getPlugin(), () -> {
            final String queryHash = Util.processHash(player.getName(), Hashing.sha256()
                    .hashString(query, StandardCharsets.UTF_8)
                    .toString());
            final DataUser dataUser = new DataUser(player.getName(), player.getUniqueId().toString(), queryHash);
            final UserStorage userStorage = AuthLib.getPlugin().getUserStorage();
            userStorage.addUser(dataUser);
        });
        return true;
    }


}
