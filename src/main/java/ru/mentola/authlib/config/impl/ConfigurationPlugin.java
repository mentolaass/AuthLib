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
    @ConfigurationPath(path = "messages.register.failed")
    private final String registerFailedMessage;
    @ConfigurationPath(path = "messages.register.no-require")
    private final String registerNoRequireMessage;
    @ConfigurationPath(path = "messages.login.no-require")
    private final String loginNoRequireMessage;
    @ConfigurationPath(path = "messages.register.usage")
    private final String registerUsageMessage;
    @ConfigurationPath(path = "messages.login.usage")
    private final String loginUsageMessage;
    @ConfigurationPath(path = "messages.register.invalid-password-length")
    private final String registerInvalidPasswordLengthMessage;
    @ConfigurationPath(path = "core.max-length-pass")
    private final int maxLengthPass;
    @ConfigurationPath(path = "core.min-length-pass")
    private final int minLengthPass;
}