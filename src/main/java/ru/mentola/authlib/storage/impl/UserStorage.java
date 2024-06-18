package ru.mentola.authlib.storage.impl;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;
import ru.mentola.authlib.AuthLib;
import ru.mentola.authlib.hikari.HikariManager;
import ru.mentola.authlib.storage.IUserStorage;
import ru.mentola.authlib.model.LoginUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public final class UserStorage implements IUserStorage {
    private HikariManager hikariManager;

    public void connect() {
        final AuthLib plugin = AuthLib.getPlugin(AuthLib.class);
        this.hikariManager = new HikariManager(plugin.getConfigurationMySql());
    }

    public void createTable() {
        try (final Connection conn = this.hikariManager.getConnection()) {
            try (final PreparedStatement statement = conn.prepareStatement("""
                CREATE TABLE IF NOT EXISTS `users` (
                `username`   TEXT,
                `unique`     TEXT,
                `pass`       TEXT);
            """)) {
                statement.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override @Nullable
    public LoginUser getUser(UUID uuid) {
        try (final Connection conn = this.hikariManager.getConnection()) {
            try (final PreparedStatement statement = conn.prepareStatement("""
                SELECT * FROM users
                WHERE `unique` = ?
            """)) {
                statement.setString(1, uuid.toString());
                final ResultSet result = statement.executeQuery();

                if (result.next())
                    return new LoginUser(result.getString("username"), result.getString("unique"), result.getString("pass"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override @Nullable
    public LoginUser getUser(String username) {
        return this.getUser(Bukkit.getOfflinePlayer(username).getUniqueId());
    }

    @Override
    public void addUser(LoginUser user) {
        try (final Connection conn = this.hikariManager.getConnection()) {
            try (final PreparedStatement statement = conn.prepareStatement("""
                INSERT INTO users
                VALUES (?, ?, ?);
            """)) {
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getPass());
                statement.setString(3, user.getUuid());
                statement.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (this.hikariManager != null)
            this.hikariManager.close();
    }
}
