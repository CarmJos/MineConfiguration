package cc.carm.lib.configuration.craft.builder.message;

import cc.carm.lib.configuration.common.builder.message.MessageValueBuilder;
import cc.carm.lib.configuration.craft.data.MessageText;
import cc.carm.lib.configuration.craft.value.ConfiguredMessage;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiFunction;

public class CraftMessageValueBuilder<M>
        extends MessageValueBuilder<M, CommandSender, MessageText, CraftMessageValueBuilder<M>> {

    public CraftMessageValueBuilder(@NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> parser) {
        super(CommandSender.class, MessageText::new, parser);
    }

    @Override
    protected @NotNull CraftMessageValueBuilder<M> getThis() {
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
