package cc.carm.lib.mineconfiguration.bukkit.source;

import cc.carm.lib.configuration.commentable.Commentable;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.file.FileConfigFactory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class BukkitConfigFactory extends FileConfigFactory<BukkitSource, ConfigurationHolder<BukkitSource>, BukkitConfigFactory> {

    public static BukkitConfigFactory from(@NotNull String path) {
        return new BukkitConfigFactory(new File(path));
    }

    public static BukkitConfigFactory from(@NotNull File file) {
        return new BukkitConfigFactory(file);
    }

    public static BukkitConfigFactory from(@NotNull File parent, @NotNull String configName) {
        return new BukkitConfigFactory(new File(parent, configName));
    }

    public static BukkitConfigFactory from(@NotNull Plugin plugin, @NotNull String configName) {
        return from(plugin.getDataFolder(), configName);
    }

    public BukkitConfigFactory(@NotNull File file) {
        super(file);
    }

    @Override
    protected BukkitConfigFactory self() {
        return this;
    }

    @Override
    public @NotNull ConfigurationHolder<BukkitSource> build() {

        File configFile = this.file;
        String sourcePath = this.resourcePath;

        Commentable.registerMeta(this.initializer); // Register commentable meta types

        return new ConfigurationHolder<BukkitSource>(this.adapters, this.options, this.metadata, this.initializer) {
            final @NotNull BukkitSource source = new BukkitSource(this, configFile, sourcePath);

            @Override
            public @NotNull BukkitSource config() {
                return this.source;
            }
        };
    }


}
