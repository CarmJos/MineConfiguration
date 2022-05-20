package cc.carm.lib.configuration.craft.builder.message;

import cc.carm.lib.configuration.minecraft.builder.message.MessageValueBuilder;
import cc.carm.lib.configuration.minecraft.utils.ParamsUtils;
import cc.carm.lib.configuration.craft.data.TextConfig;
import cc.carm.lib.configuration.craft.value.ConfiguredMessage;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiFunction;

public class CraftMessageValueBuilder<M>
        extends MessageValueBuilder<M, CommandSender, TextConfig, CraftMessageValueBuilder<M>> {

    public CraftMessageValueBuilder(@NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> parser) {
        super(CommandSender.class, TextConfig::new, parser);
    }

    @Override
    protected @NotNull CraftMessageValueBuilder<M> getThis() {
        return this;
    }

    @Override
    public @NotNull ConfiguredMessage<M> build() {
        return new ConfiguredMessage<>(
                this.provider, this.path, this.headerComments, this.inlineComment,
                Optional.ofNullable(this.defaultValue).orElse(TextConfig.of("")),
                ParamsUtils.formatParams(this.paramFormatter, this.params),
                this.messageParser, this.sendHandler
        );
    }


}
