package cc.carm.lib.configuration.bungee.builder.message;

import cc.carm.lib.configuration.bungee.data.MessageText;
import cc.carm.lib.configuration.bungee.value.ConfiguredMessageList;
import cc.carm.lib.configuration.common.builder.message.MessageListBuilder;
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
                this.provider, this.path, buildComments(),
                Optional.ofNullable(this.defaultValue).orElse(MessageText.of(new ArrayList<>())),
                buildParams(), this.messageParser, this.sendFunction
        );
    }
}
