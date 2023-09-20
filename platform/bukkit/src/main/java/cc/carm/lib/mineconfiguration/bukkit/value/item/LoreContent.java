package cc.carm.lib.mineconfiguration.bukkit.value.item;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LoreContent {

    public static LoreContent of(@NotNull List<String> content) {
        return of(content, false);
    }

    public static LoreContent of(@NotNull List<String> content, boolean original) {
        return new LoreContent(content, original);
    }

    protected final @NotNull List<String> content;
    protected final boolean original;

    public LoreContent(@NotNull List<String> content, boolean original) {
        this.content = content;
        this.original = original;
    }


    public @NotNull List<String> getContent() {
        return content;
    }

    public boolean isOriginal() {
        return original;
    }


}
