package cc.carm.lib.configuration.bukkit.source;

import cc.carm.lib.configuration.core.ConfigInitializer;
import cc.carm.lib.configuration.core.source.impl.FileConfigProvider;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public abstract class BukkitConfigProvider<W extends BukkitSectionWrapper, C extends YamlConfiguration>
        extends FileConfigProvider<W> {

    protected ConfigInitializer<? extends BukkitConfigProvider<W, C>> initializer;
    protected C configuration;

    public BukkitConfigProvider(@NotNull File file) {
        super(file);
    }

    public abstract void initializeConfig();

    @Override
    public abstract @NotNull W getConfiguration();

    @Override
    public void reload() throws Exception {
        configuration.load(getFile());
    }

    @Override
    public void save() throws Exception {
        configuration.save(getFile());
    }

    @Override
    public @NotNull ConfigInitializer<? extends BukkitConfigProvider<W, C>> getInitializer() {
        return this.initializer;
    }

}
