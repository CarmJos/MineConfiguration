package cc.carm.lib.mineconfiguration.bungee.builder.message;

import cc.carm.lib.configuration.core.value.ValueManifest;
import cc.carm.lib.mineconfiguration.bungee.data.TextConfig;
import cc.carm.lib.mineconfiguration.bungee.value.ConfiguredMessageList;
import cc.carm.lib.mineconfiguration.common.builder.message.MessageListBuilder;
import cc.carm.lib.mineconfiguration.common.utils.ParamsUtils;
import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiFunction;

public class BungeeMessageListBuilder<M>
        extends MessageListBuilder<M, CommandSender, TextConfig, BungeeMessageListBuilder<M>> {

    public BungeeMessageListBuilder(@NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> parser) {
        super(CommandSender.class, TextConfig::of, parser);
    }

    @Override
    protected @NotNull BungeeMessageListBuilder<M> getThis() {
        return this;
    }

    @Override
    public @NotNull ConfiguredMessageList<M> build() {
        return new ConfiguredMessageList<>(
                buildManifest(TextConfig.of(new ArrayList<>())),
                ParamsUtils.formatParams(this.paramFormatter, this.params),
                this.messageParser, this.sendFunction
        );
    }
}
