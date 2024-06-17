package ru.mentola.authlib.listener;

import io.papermc.paper.event.block.PlayerShearBlockEvent;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import ru.mentola.authlib.AuthLib;
import ru.mentola.authlib.pool.auth.Login;
import ru.mentola.authlib.model.DataUser;
import ru.mentola.authlib.pool.auth.Register;

public final class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(AuthLib.getPlugin(), () -> {
            final AuthLib plugin = AuthLib.getPlugin();
            final Player player = event.getPlayer();
            final DataUser dataUser = plugin.getUserStorage()
                    .getUser(player.getUniqueId());

            if (dataUser != null) {
                plugin.getAuthPool()
                        .add(new Login(dataUser));
            }
            else {
                plugin.getRegisterPool()
                        .add(new Register(player));
            }
        });
    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        processEventOnPool(event, event.getPlayer());
    }

    @EventHandler
    public void onPlayerSendMessage(final AsyncChatEvent event) {
        processEventOnPool(event, event.getPlayer());
    }

    @EventHandler
    public void onPlayerDamage(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player)
            processEventOnPool(event, player);
    }

    private void processEventOnPool(final Cancellable event, final Player player) {
        final AuthLib plugin = AuthLib.getPlugin();
        final Login loginData = plugin.getAuthPool().get((data) -> data.getUser()
                .getUuid()
                .equals(player.getUniqueId().toString()));
        final Register registerData = plugin.getRegisterPool()
                .get((data) -> data.getPlayer().equals(player));
        if (loginData != null
                || registerData != null)
            event.setCancelled(true);
    }
}
