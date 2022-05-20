package cc.carm.lib.configuration.bungee.data;

import cc.carm.lib.configuration.minecraft.data.AbstractText;
import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MessageText extends AbstractText<CommandSender> {

    public MessageText(@NotNull String message) {
        super(CommandSender.class, message);
    }

    @Contract("!null,-> !null")
    public static @Nullable MessageText of(@Nullable String message) {
        if (message == null) return null;
        else return new MessageText(message);
    }

    public static @NotNull List<MessageText> of(@Nullable List<String> messages) {
        if (messages == null || messages.isEmpty()) return new ArrayList<>();
        else return messages.stream().map(MessageText::of).collect(Collectors.toList());
    }

    public static @NotNull List<MessageText> of(@NotNull String... messages) {
        return of(Arrays.asList(messages));
    }

}
