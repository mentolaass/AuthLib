package ru.mentola.authlib.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.mentola.authlib.config.impl.ConfigurationMySql;

import java.sql.Connection;
import java.sql.SQLException;

public final class HikariManager {
    private final HikariDataSource source;

    public HikariManager(final ConfigurationMySql configuration) {
        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(this.getUrl(configuration));
        config.setUsername(configuration.getUser());
        config.setPassword(configuration.getPass());
        config.setMaximumPoolSize(3);
        this.source = new HikariDataSource(config);
    }

    private String getUrl(final ConfigurationMySql configuration) {
        return String.format("jdbc:mysql://%s:%s/%s", configuration.getHost(), configuration.getPort(), configuration.getName());
    }

    public Connection getConnection() throws SQLException {
        return this.source.getConnection();
    }

    public void close() {
        this.source.close();
    }
}
