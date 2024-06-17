package ru.mentola.authlib;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.mentola.authlib.command.LoginCommand;
import ru.mentola.authlib.command.RegisterCommand;
import ru.mentola.authlib.config.ConfigurationFactory;
import ru.mentola.authlib.config.impl.ConfigurationMySql;
import ru.mentola.authlib.config.impl.ConfigurationPlugin;
import ru.mentola.authlib.hikari.HikariManager;
import ru.mentola.authlib.listener.PlayerListener;
import ru.mentola.authlib.pool.impl.AuthPool;
import ru.mentola.authlib.pool.impl.RegisterPool;
import ru.mentola.authlib.storage.impl.UserStorage;
import ru.mentola.authlib.task.PoolUpdater;

@Getter
public final class AuthLib extends JavaPlugin {
    private ConfigurationPlugin configurationPlugin;
    private ConfigurationMySql configurationMySql;
    private HikariManager hikariManager;
    private UserStorage userStorage;
    private AuthPool authPool;
    private RegisterPool registerPool;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        plugin = this;

        this.configurationPlugin =
                ConfigurationFactory.build(this.getConfig(), ConfigurationPlugin.class);
        this.configurationMySql =
                ConfigurationFactory.build(this.getConfig(), ConfigurationMySql.class);

        if (this.configurationPlugin == null
            || this.configurationMySql == null) {
            this.getServer()
                    .getPluginManager()
                    .disablePlugin(this);
        }

        this.hikariManager = new HikariManager(this.configurationMySql);
        this.userStorage = new UserStorage();
        this.authPool = new AuthPool();
        this.registerPool = new RegisterPool();

        final PlayerListener playerListener = new PlayerListener();
        final PoolUpdater poolUpdater = new PoolUpdater();

        Bukkit.getPluginManager()
                .registerEvents(playerListener, this);

        Bukkit.getScheduler()
                .scheduleSyncRepeatingTask(this, poolUpdater, 20, 20);

        Bukkit.getPluginCommand("auth").setExecutor(new LoginCommand());
        Bukkit.getPluginCommand("register").setExecutor(new RegisterCommand());
    }

    @Override
    public void onDisable() {
        if (this.hikariManager != null) {
            try {
                this.hikariManager.close();
            } catch (Exception ignored) { }
        }
    }

    @Getter
    private static AuthLib plugin;
}
