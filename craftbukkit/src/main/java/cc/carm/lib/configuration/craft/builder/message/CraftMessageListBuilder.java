package cc.carm.lib.configuration.craft.builder.message;

import cc.carm.lib.configuration.common.builder.message.MessageListBuilder;
import cc.carm.lib.configuration.craft.data.MessageText;
import cc.carm.lib.configuration.craft.value.ConfiguredMessageList;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiFunction;

public class CraftMessageListBuilder<M>
        extends MessageListBuilder<M, CommandSender, MessageText, CraftMessageListBuilder<M>> {

    public CraftMessageListBuilder(@NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> parser) {
        super(CommandSender.class, MessageText::of, parser);
    }

    @Override
    protected @NotNull CraftMessageListBuilder<M> getThis() {
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
