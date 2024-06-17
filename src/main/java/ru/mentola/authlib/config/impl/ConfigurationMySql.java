package ru.mentola.authlib.config.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mentola.authlib.config.api.ConfigurationModel;
import ru.mentola.authlib.config.api.ConfigurationPath;

@AllArgsConstructor @Getter
public final class ConfigurationMySql extends ConfigurationModel {
    @ConfigurationPath(path = "mysql.host")
    private final String host;
    @ConfigurationPath(path = "mysql.port")
    private final int port;
    @ConfigurationPath(path = "mysql.user")
    private final String user;
    @ConfigurationPath(path = "mysql.pass")
    private final String pass;
    @ConfigurationPath(path = "mysql.name")
    private final String name;
}
