package cc.carm.lib.configuration.bungee.builder.message;

import cc.carm.lib.configuration.bungee.data.MessageText;
import cc.carm.lib.configuration.bungee.value.ConfiguredMessage;
import cc.carm.lib.configuration.common.builder.message.MessageValueBuilder;
import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiFunction;

public class BungeeMessageValueBuilder<M>
        extends MessageValueBuilder<M, CommandSender, MessageText, BungeeMessageValueBuilder<M>> {

    public BungeeMessageValueBuilder(@NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> parser) {
        super(CommandSender.class, MessageText::new, parser);
    }

    @Override
    protected @NotNull BungeeMessageValueBuilder<M> getThis() {
        return this;
    }

    @Override
    public @NotNull ConfiguredMessage<M> build() {
        return new ConfiguredMessage<>(
                this.provider, this.path, buildComments(),
                Optional.ofNullable(this.defaultValue).orElse(MessageText.of("")),
                buildParams(), this.messageParser, this.sendHandler
        );
    }


}
