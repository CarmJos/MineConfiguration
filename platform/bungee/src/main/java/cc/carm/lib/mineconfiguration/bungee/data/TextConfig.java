package cc.carm.lib.mineconfiguration.bungee.data;

import cc.carm.lib.mineconfiguration.common.data.AbstractText;
import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TextConfig extends AbstractText<CommandSender> {

    public TextConfig(@NotNull String message) {
        super(CommandSender.class, message);
    }

    @Contract("!null,-> !null")
    public static @Nullable TextConfig of(@Nullable String message) {
        if (message == null) return null;
        else return new TextConfig(message);
    }

    public static @NotNull List<TextConfig> of(@Nullable List<String> messages) {
        if (messages == null || messages.isEmpty()) return new ArrayList<>();
        else return messages.stream().map(TextConfig::of).collect(Collectors.toList());
    }

    public static @NotNull List<TextConfig> of(@NotNull String... messages) {
        return of(Arrays.asList(messages));
    }

}
