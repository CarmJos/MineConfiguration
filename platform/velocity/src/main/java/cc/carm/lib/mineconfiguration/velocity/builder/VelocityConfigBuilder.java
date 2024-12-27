package cc.carm.lib.mineconfiguration.velocity.builder;

import cc.carm.lib.mineconfiguration.velocity.builder.message.BungeeMessageBuilder;
import cc.carm.lib.configuration.core.builder.ConfigBuilder;
import org.jetbrains.annotations.NotNull;

public class VelocityConfigBuilder extends ConfigBuilder {

    public @NotNull BungeeMessageBuilder createMessage() {
        return new BungeeMessageBuilder();
    }


}
