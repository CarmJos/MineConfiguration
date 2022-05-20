package cc.carm.lib.configuration.minecraft.bukkit.builder.title;

import cc.carm.lib.configuration.minecraft.bukkit.builder.AbstractCraftBuilder;
import cc.carm.lib.configuration.minecraft.bukkit.data.TitleConfig;
import cc.carm.lib.configuration.minecraft.bukkit.function.TitleSendConsumer;
import cc.carm.lib.configuration.minecraft.bukkit.utils.ProtocolLibHelper;
import cc.carm.lib.configuration.minecraft.bukkit.value.ConfiguredTitle;
import cc.carm.lib.configuration.minecraft.common.utils.ParamsUtils;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.List;
import java.util.function.Function;

public class TitleConfigBuilder extends AbstractCraftBuilder<TitleConfig, TitleConfigBuilder> {

    @SuppressWarnings("deprecation")
    protected static @NotNull TitleSendConsumer DEFAULT_TITLE_CONSUMER = (player, fadeIn, stay, fadeOut, line1, line2) -> {
        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            try {
                ProtocolLibHelper.sendTitle(player, fadeIn, stay, fadeOut, line1, line2);
            } catch (Exception ignored) {
            }
        } else {
            player.sendTitle(line1, line2);
        }
    };

    protected @NotNull String[] params;

    protected @Range(from = 0L, to = Integer.MAX_VALUE) int fadeIn = 10;
    protected @Range(from = 0L, to = Integer.MAX_VALUE) int stay = 60;
    protected @Range(from = 0L, to = Integer.MAX_VALUE) int fadeOut = 10;

    protected @NotNull TitleSendConsumer sendConsumer;
    protected @NotNull Function<@NotNull String, @NotNull String> paramFormatter;

    public TitleConfigBuilder() {
        this.sendConsumer = TitleConfigBuilder.DEFAULT_TITLE_CONSUMER;
        this.paramFormatter = ParamsUtils.DEFAULT_PARAM_FORMATTER;
    }

    public @NotNull TitleConfigBuilder defaults(@Nullable String line1,
                                                @Nullable String line2) {
        return defaults(TitleConfig.of(line1, line2));
    }

    public @NotNull TitleConfigBuilder whenSend(@NotNull TitleSendConsumer consumer) {
        this.sendConsumer = consumer;
        return this;
    }

    public TitleConfigBuilder params(String... params) {
        this.params = params;
        return this;
    }

    public TitleConfigBuilder params(@NotNull List<String> params) {
        return params(params.toArray(new String[0]));
    }

    public TitleConfigBuilder fadeIn(@Range(from = 0L, to = Integer.MAX_VALUE) int fadeIn) {
        this.fadeIn = fadeIn;
        return this;
    }

    public TitleConfigBuilder stay(@Range(from = 0L, to = Integer.MAX_VALUE) int stay) {
        this.stay = stay;
        return this;
    }

    public TitleConfigBuilder fadeOut(@Range(from = 0L, to = Integer.MAX_VALUE) int fadeOut) {
        this.fadeOut = fadeOut;
        return this;
    }

    public TitleConfigBuilder formatParam(Function<String, String> paramFormatter) {
        this.paramFormatter = paramFormatter;
        return this;
    }

    @Override
    protected @NotNull TitleConfigBuilder getThis() {
        return this;
    }

    @Override
    public @NotNull ConfiguredTitle build() {
        return new ConfiguredTitle(
                this.provider, this.path, this.headerComments, this.inlineComment,
                this.defaultValue, ParamsUtils.formatParams(this.paramFormatter, this.params),
                this.sendConsumer, this.fadeIn, this.stay, this.fadeOut
        );
    }
}
