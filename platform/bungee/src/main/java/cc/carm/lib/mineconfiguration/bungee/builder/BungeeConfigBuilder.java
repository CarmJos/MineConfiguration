package cc.carm.lib.mineconfiguration.bungee.builder;

import cc.carm.lib.mineconfiguration.bungee.builder.message.BungeeMessageBuilder;
import cc.carm.lib.configuration.core.builder.ConfigBuilder;
import org.jetbrains.annotations.NotNull;

public class BungeeConfigBuilder extends ConfigBuilder {

    public @NotNull BungeeMessageBuilder createMessage() {
        return new BungeeMessageBuilder();
    }


}
