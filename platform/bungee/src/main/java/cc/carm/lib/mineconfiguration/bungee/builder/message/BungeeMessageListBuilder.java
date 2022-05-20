package cc.carm.lib.mineconfiguration.bungee.builder.message;

import cc.carm.lib.mineconfiguration.bungee.data.MessageText;
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
        extends MessageListBuilder<M, CommandSender, MessageText, BungeeMessageListBuilder<M>> {

    public BungeeMessageListBuilder(@NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> parser) {
        super(CommandSender.class, MessageText::of, parser);
    }

    @Override
    protected @NotNull BungeeMessageListBuilder<M> getThis() {
        return this;
    }

    @Override
    public @NotNull ConfiguredMessageList<M> build() {
        return new ConfiguredMessageList<>(
                this.provider, this.path, this.headerComments, this.inlineComment,
                Optional.ofNullable(this.defaultValue).orElse(MessageText.of(new ArrayList<>())),
                ParamsUtils.formatParams(this.paramFormatter, this.params),
                this.messageParser, this.sendFunction
        );
    }
}
