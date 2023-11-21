package cc.carm.lib.mineconfiguration.bukkit.value.item;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class LoreContent<S> {

    public static LoreContent<List<String>> of(@NotNull List<String> content) {
        return of(content, false);
    }

    public static LoreContent<List<String>> of(@NotNull List<String> content, boolean original) {
        return new LoreContent<List<String>>(content, original) {
            @Override
            public List<String> parse(CommandSender receiver) {
                return getSource();
            }
        };
    }

    protected final @NotNull S source;
    protected final boolean original;

    public LoreContent(@NotNull S source, boolean original) {
        this.source = source;
        this.original = original;
    }

    public @NotNull S getSource() {
        return source;
    }

    public abstract List<String> parse(CommandSender receiver);

    public boolean isOriginal() {
        return original;
    }


}
