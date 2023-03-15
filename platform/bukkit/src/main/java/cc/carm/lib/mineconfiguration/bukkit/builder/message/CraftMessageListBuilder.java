package cc.carm.lib.mineconfiguration.bukkit.builder.message;

import cc.carm.lib.mineconfiguration.bukkit.data.TextConfig;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import cc.carm.lib.mineconfiguration.common.builder.message.MessageListBuilder;
import cc.carm.lib.mineconfiguration.common.utils.ParamsUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class CraftMessageListBuilder<M>
        extends MessageListBuilder<M, CommandSender, TextConfig, CraftMessageListBuilder<M>> {

    public CraftMessageListBuilder(@NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> parser) {
        super(CommandSender.class, TextConfig::of, parser);
    }

    @Override
    protected @NotNull CraftMessageListBuilder<M> getThis() {
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
