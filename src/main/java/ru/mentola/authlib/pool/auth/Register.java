package ru.mentola.authlib.pool.auth;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.mentola.authlib.AuthLib;
import ru.mentola.authlib.model.LoginUser;
import ru.mentola.authlib.storage.impl.UserStorage;
import ru.mentola.authlib.util.Util;

import java.util.function.Consumer;

@Getter
public final class Register {
    private final Player player;
    @Setter
    private int time;

    public Register(final Player player) {
        this.player = player;
        this.time = 60;
    }

    public void attempt(final String pass, final Consumer<Boolean> callback) {
        final AuthLib plugin = AuthLib.getInstance();

        if (plugin == null) {
            callback.accept(false);
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                final String passHash = Util.processHash(pass);
                final LoginUser loginUser = new LoginUser(player.getName(), player.getUniqueId().toString(), passHash);
                final UserStorage userStorage = plugin.getUserStorage();
                userStorage.addUser(loginUser);
                callback.accept(true);
            } catch (Exception ignored) {
                callback.accept(false);
            }
        });
    }
}
