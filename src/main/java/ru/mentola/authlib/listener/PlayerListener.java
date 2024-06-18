package ru.mentola.authlib.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.mentola.authlib.AuthLib;
import ru.mentola.authlib.model.LoginUser;
import ru.mentola.authlib.pool.auth.Login;
import ru.mentola.authlib.pool.auth.Register;

public final class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        final AuthLib plugin = AuthLib.getInstance();
        final Player player = e.getPlayer();

        if (plugin != null) {
            plugin.getPreAuthPool().add(player);

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                final LoginUser loginUser = plugin.getUserStorage().getUser(player.getUniqueId());
                plugin.getPreAuthPool().remove((p) -> p.equals(player));

                if (loginUser != null)
                    plugin.getLoginPool().add(new Login(loginUser));
                else
                    plugin.getRegisterPool().add(new Register(player));
            });
        }
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        final AuthLib plugin = AuthLib.getInstance();
        final Player player = e.getPlayer();

        if (plugin != null) {
            plugin.getPreAuthPool().remove((p) -> p.equals(e.getPlayer()));
            plugin.getLoginPool().remove((data) -> data.getUser().getUuid().equals(player.getUniqueId().toString()));
            plugin.getRegisterPool().remove((data) -> data.getPlayer().equals(player));
        }
    }
}
