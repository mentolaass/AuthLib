package ru.mentola.authlib.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.mentola.authlib.AuthLib;
import ru.mentola.authlib.pool.auth.Register;

public final class RegisterCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            final AuthLib plugin = AuthLib.getPlugin();
            final Register registerData = plugin.getRegisterPool().get((data) -> data.getPlayer()
                    .getUniqueId()
                    .toString()
                    .equals(player.getUniqueId().toString()));

            if (registerData == null) {
                player.sendMessage("Вам не требуется регистрация.");
                return true;
            }

            if (args.length != 1) {
                player.sendMessage("Использование: /register <pass>");
                return true;
            }

            if (registerData.attempt(args[0])) {
                plugin.getRegisterPool().remove((data) -> data.getPlayer()
                        .getUniqueId()
                        .toString()
                        .equals(player.getUniqueId().toString()));
                player.sendMessage(plugin.getConfigurationPlugin().getRegisterSuccessMessage());
            }

            return true;
        }

        return false;
    }
}
