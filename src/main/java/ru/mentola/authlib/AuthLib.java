package ru.mentola.authlib;

import com.github.retrooper.packetevents.PacketEvents;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import ru.mentola.authlib.command.LoginCommand;
import ru.mentola.authlib.command.RegisterCommand;
import ru.mentola.authlib.config.ConfigurationFactory;
import ru.mentola.authlib.config.impl.ConfigurationMySql;
import ru.mentola.authlib.config.impl.ConfigurationPlugin;
import ru.mentola.authlib.listener.PacketListener;
import ru.mentola.authlib.listener.PlayerListener;
import ru.mentola.authlib.pool.impl.LoginPool;
import ru.mentola.authlib.pool.impl.PreAuthPool;
import ru.mentola.authlib.pool.impl.RegisterPool;
import ru.mentola.authlib.storage.impl.UserStorage;
import ru.mentola.authlib.task.PoolUpdater;

@Getter
public final class AuthLib extends JavaPlugin {
    private ConfigurationPlugin configurationPlugin;
    private ConfigurationMySql configurationMySql;
    private PacketListener packetsListener;
    private UserStorage userStorage;
    private LoginPool loginPool;
    private PreAuthPool preAuthPool;
    private RegisterPool registerPool;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        this.configurationPlugin = ConfigurationFactory.build(this.getConfig(), ConfigurationPlugin.class);
        this.configurationMySql = ConfigurationFactory.build(this.getConfig(), ConfigurationMySql.class);

        if (this.configurationPlugin == null
            || this.configurationMySql == null) {
            this.getServer()
                    .getPluginManager()
                    .disablePlugin(this);
            return;
        }

        this.userStorage = new UserStorage();
        this.loginPool = new LoginPool();
        this.preAuthPool = new PreAuthPool();
        this.registerPool = new RegisterPool();
        this.packetsListener = new PacketListener();

        this.userStorage.connect();
        this.userStorage.createTable();

        PacketEvents.getAPI().getEventManager().registerListener(this.packetsListener);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new PoolUpdater(), 0, 20);
        Bukkit.getPluginCommand("auth").setExecutor(new LoginCommand());
        Bukkit.getPluginCommand("register").setExecutor(new RegisterCommand());
    }

    @Override
    public void onDisable() {
        if (this.userStorage != null) {
            try {
                this.userStorage.close();
            } catch (Exception ignored) { }
        }

        try {
            PacketEvents.getAPI().getEventManager().unregisterListener(packetsListener);
            Bukkit.getScheduler().cancelTasks(this);
        } catch (Exception ignored) { }

        if (this.loginPool != null)
            this.loginPool.clear();

        if (this.registerPool != null)
            this.registerPool.clear();

        if (this.preAuthPool != null)
            this.preAuthPool.clear();
    }

    @Nullable
    public static AuthLib getInstance() {
        final Plugin plugin =  Bukkit.getPluginManager().getPlugin("AuthLib");
        if (plugin instanceof AuthLib authLib)
            return authLib;
        return null;
    }
}
