package ru.mentola.authlib.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.mentola.authlib.AuthLib;
import ru.mentola.authlib.config.impl.ConfigurationPlugin;
import ru.mentola.authlib.pool.auth.Register;

public final class RegisterCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            final AuthLib plugin = AuthLib.getInstance();

            if (plugin == null)
                return false;

            final ConfigurationPlugin config = plugin.getConfigurationPlugin();
            final Register registerData = plugin.getRegisterPool().get((data) -> data.getPlayer().getUniqueId().toString().equals(player.getUniqueId().toString()));

            if (registerData == null) {
                player.sendMessage(config.getRegisterNoRequireMessage());
                return true;
            }

            if (args.length != 1) {
                player.sendMessage(config.getRegisterUsageMessage());
                return true;
            }

            final String pass = args[0];
            if (pass.length() < config.getMinLengthPass()
                    || pass.length() > config.getMaxLengthPass()) {
                player.sendMessage(config.getRegisterInvalidPasswordLengthMessage()
                        .replaceAll("%max_len%", String.valueOf(config.getMaxLengthPass()))
                        .replaceAll("%min_len%", String.valueOf(config.getMinLengthPass())));
                return true;
            }

            registerData.attempt(pass, (response) -> {
                if (response) {
                    plugin.getRegisterPool().remove((data) -> data.getPlayer().getUniqueId().toString().equals(player.getUniqueId().toString()));
                    player.sendMessage(config.getRegisterSuccessMessage());
                } else {
                    player.sendMessage(config.getRegisterFailedMessage());
                }
            });

            return true;
        }

        return false;
    }
}
