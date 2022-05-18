package cc.carm.lib.configuration.spigot.source;

import cc.carm.lib.configuration.core.ConfigInitializer;
import cc.carm.lib.configuration.craft.source.CraftConfigProvider;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class SpigotConfigProvider extends CraftConfigProvider {

    public SpigotConfigProvider(@NotNull File file) {
        super(file);
    }

    @Override
    public void initializeConfig() {
        this.configuration = YamlConfiguration.loadConfiguration(file);
        this.initializer = new ConfigInitializer<>(this);
    }

    @Override
    public void setHeaderComment(@Nullable String path, @Nullable List<String> comments) {
        if (path == null) {
            this.configuration.options().setHeader(comments);
        } else {
            this.configuration.setComments(path, comments);
        }
    }

    @Override
    public void setInlineComment(@NotNull String path, @Nullable String comment) {
        if (comment == null) {
            this.configuration.setInlineComments(path, null);
        } else {
            this.configuration.setComments(path, Collections.singletonList(comment));
        }
    }

    @Override
    public @Nullable @Unmodifiable List<String> getHeaderComment(@Nullable String path) {
        if (path == null) return Collections.unmodifiableList(this.configuration.options().getHeader());
        else return this.configuration.getComments(path);
    }

    @Override
    public @Nullable String getInlineComment(@NotNull String path) {
        return String.join(" ", this.configuration.getInlineComments(path));
    }
}
