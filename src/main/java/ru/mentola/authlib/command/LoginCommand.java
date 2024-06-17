package ru.mentola.authlib.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.mentola.authlib.AuthLib;
import ru.mentola.authlib.pool.auth.Login;

public final class LoginCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            final AuthLib plugin = AuthLib.getPlugin();
            final Login loginData = plugin.getAuthPool().get((data) -> data.getUser()
                    .getUuid()
                    .equals(player.getUniqueId().toString()));

            if (loginData == null) {
                player.sendMessage("Вам не требуется авторизация.");
                return true;
            }

            if (args.length != 1) {
                player.sendMessage("Использование: /auth <pass>");
                return true;
            }

            if (loginData.attempt(args[0])) {
                plugin.getAuthPool().remove((data) -> data.getUser()
                        .getUuid()
                        .equals(player.getUniqueId().toString()));
                player.sendMessage(plugin.getConfigurationPlugin().getLoginSuccessMessage());
            } else {
                player.sendMessage(plugin.getConfigurationPlugin().getLoginFailedMessage());
            }

            return true;
        }

        return false;
    }
}
