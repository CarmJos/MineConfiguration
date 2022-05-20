package cc.carm.lib.configuration.minecraft.bukkit.builder.message;

import cc.carm.lib.configuration.minecraft.bukkit.data.TextConfig;
import cc.carm.lib.configuration.minecraft.bukkit.value.ConfiguredMessage;
import cc.carm.lib.configuration.minecraft.common.builder.message.MessageValueBuilder;
import cc.carm.lib.configuration.minecraft.common.utils.ParamsUtils;
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
