package cc.carm.lib.mineconfiguration.spigot.source;

import cc.carm.lib.configuration.core.source.ConfigurationComments;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;

public class SpigotYAMLComments extends ConfigurationComments {

    protected final YamlConfiguration configuration;

    public SpigotYAMLComments(YamlConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void setHeaderComments(@Nullable String path, @Nullable List<String> comments) {
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
