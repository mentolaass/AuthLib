package ru.mentola.authlib.config.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mentola.authlib.config.api.ConfigurationModel;
import ru.mentola.authlib.config.api.ConfigurationPath;

@AllArgsConstructor @Getter
public final class ConfigurationPlugin extends ConfigurationModel {
    @ConfigurationPath(path = "messages.register.require")
    private final String registerRequireMessage;
    @ConfigurationPath(path = "messages.login.require")
    private final String loginRequireMessage;
    @ConfigurationPath(path = "messages.login.success")
    private final String loginSuccessMessage;
    @ConfigurationPath(path = "messages.login.failed")
    private final String loginFailedMessage;
    @ConfigurationPath(path = "messages.register.success")
    private final String registerSuccessMessage;
}