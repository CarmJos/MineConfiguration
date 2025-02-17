package cc.carm.lib.mineconfiguration.velocity;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.yaml.YAMLConfigFactory;
import cc.carm.lib.configuration.source.yaml.YAMLSource;
import cc.carm.lib.mineconfiguration.common.AbstractConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class MineConfiguration extends AbstractConfiguration<ConfigurationHolder<YAMLSource>> {

    public MineConfiguration(@NotNull File pluginDataFolder) {
        super(
                YAMLConfigFactory.from(pluginDataFolder, "config.yml").resourcePath("config.yml").build(),
                YAMLConfigFactory.from(pluginDataFolder, "messages.yml").resourcePath("messages.yml").build()
        );
    }

    public MineConfiguration(@NotNull File pluginDataFolder,
                             @NotNull Configuration configRoot,
                             @NotNull Configuration messageRoot) {
        this(pluginDataFolder);
        initializeConfig(configRoot);
        initializeMessage(messageRoot);
    }

    public MineConfiguration(@NotNull File pluginDataFolder,
                             @NotNull Class<? extends Configuration> configRoot,
                             @NotNull Class<? extends Configuration> messageRoot) {
        this(pluginDataFolder);
        initializeConfig(configRoot);
        initializeMessage(messageRoot);
    }


}
