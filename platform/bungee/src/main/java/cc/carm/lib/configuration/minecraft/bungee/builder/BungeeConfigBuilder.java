package cc.carm.lib.configuration.minecraft.bungee.builder;

import cc.carm.lib.configuration.minecraft.bungee.builder.message.BungeeMessageBuilder;
import cc.carm.lib.configuration.core.builder.ConfigBuilder;
import org.jetbrains.annotations.NotNull;

public class BungeeConfigBuilder extends ConfigBuilder {

    public @NotNull BungeeMessageBuilder createMessage() {
        return new BungeeMessageBuilder();
    }


}
