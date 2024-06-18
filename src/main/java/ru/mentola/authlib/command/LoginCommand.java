package ru.mentola.authlib.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.mentola.authlib.AuthLib;
import ru.mentola.authlib.config.impl.ConfigurationPlugin;
import ru.mentola.authlib.pool.auth.Login;

public final class LoginCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            final AuthLib plugin = AuthLib.getInstance();

            if (plugin == null)
                return false;

            final ConfigurationPlugin config = plugin.getConfigurationPlugin();
            final Login loginData = plugin.getLoginPool().get((data) -> data.getUser().getUuid().equals(player.getUniqueId().toString()));

            if (loginData == null) {
                player.sendMessage(config.getLoginNoRequireMessage());
                return true;
            }

            if (args.length != 1) {
                player.sendMessage(config.getLoginUsageMessage());
                return true;
            }

            if (loginData.attempt(args[0])) {
                plugin.getLoginPool().remove((data) -> data.getUser().getUuid().equals(player.getUniqueId().toString()));
                player.sendMessage(config.getLoginSuccessMessage());
            } else {
                player.sendMessage(config.getLoginFailedMessage());
            }

            return true;
        }

        return false;
    }
}
