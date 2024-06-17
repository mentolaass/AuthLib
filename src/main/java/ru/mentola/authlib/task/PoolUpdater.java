package ru.mentola.authlib.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.mentola.authlib.AuthLib;

public final class PoolUpdater implements Runnable {
    @Override
    public void run() {
        final AuthLib authLib = AuthLib.getPlugin();

        authLib.getAuthPool()
                .getUnModifiableCachePool()
                .forEach((login) -> {
                    final Player player = Bukkit.getPlayer(login.getUser().getUsername());
                    if (login.getTime() % 5 == 0)
                        if (player != null)
                            player.sendMessage(authLib.getConfigurationPlugin().getLoginRequireMessage());
                    login.setTime(login.getTime() - 1);
                    if (login.getTime() <= 0
                        && player != null)
                        player.kick();
                });

        authLib.getRegisterPool()
                .getUnModifiableCachePool()
                .forEach((register) -> {
                    if (register.getTime() % 5 == 0)
                        register.getPlayer().sendMessage(authLib.getConfigurationPlugin().getRegisterRequireMessage());
                    register.setTime(register.getTime() - 1);
                    if (register.getTime() <= 0
                            && register.getPlayer() != null)
                        register.getPlayer().kick();
                });

        authLib.getRegisterPool()
                .remove((register) -> register.getTime() <= 0);
        authLib.getAuthPool()
                .remove((login) -> login.getTime() <= 0);
    }
}
