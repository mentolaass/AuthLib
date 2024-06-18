package ru.mentola.authlib.listener;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import org.bukkit.entity.Player;
import ru.mentola.authlib.AuthLib;
import ru.mentola.authlib.pool.auth.Login;
import ru.mentola.authlib.pool.auth.Register;

public final class PacketListener extends PacketListenerAbstract {
    @Override
    public void onPacketReceive(final PacketReceiveEvent e) {
        if (this.isPacketHandle(e.getPacketType()))
            processPacketEventOnPool(e, (Player) e.getPlayer());
    }

    private boolean isPacketHandle(final PacketTypeCommon type) {
        return type != PacketType.Play.Client.CHAT_COMMAND;
    }

    private void processPacketEventOnPool(final ProtocolPacketEvent<Object> e, final Player player) {
        final AuthLib plugin = AuthLib.getInstance();

        if (plugin != null) {
            final Login loginData = plugin.getLoginPool().get((data) -> data.getUser().getUuid().equals(player.getUniqueId().toString()));
            final Register registerData = plugin.getRegisterPool().get((data) -> data.getPlayer().equals(player));

            if (loginData != null || registerData != null || plugin.getPreAuthPool().contains(player))
                e.setCancelled(true);
        }
    }
}
