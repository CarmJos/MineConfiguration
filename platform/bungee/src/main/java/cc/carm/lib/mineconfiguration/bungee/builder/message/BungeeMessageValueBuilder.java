package cc.carm.lib.mineconfiguration.bungee.builder.message;

import cc.carm.lib.mineconfiguration.bungee.data.TextConfig;
import cc.carm.lib.mineconfiguration.bungee.value.ConfiguredMessage;
import cc.carm.lib.mineconfiguration.common.builder.message.MessageValueBuilder;
import cc.carm.lib.mineconfiguration.common.utils.ParamsUtils;
import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class BungeeMessageValueBuilder<M>
        extends MessageValueBuilder<M, CommandSender, TextConfig, BungeeMessageValueBuilder<M>> {

    public BungeeMessageValueBuilder(@NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> parser) {
        super(CommandSender.class, TextConfig::new, parser);
    }

    @Override
    protected @NotNull BungeeMessageValueBuilder<M> getThis() {
        return this;
    }

    @Override
    public @NotNull ConfiguredMessage<M> build() {
        return new ConfiguredMessage<>(
                buildManifest(TextConfig.of("")),
                ParamsUtils.formatParams(this.paramFormatter, this.params),
                this.messageParser, this.sendHandler
        );
    }


}
