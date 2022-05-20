package cc.carm.lib.configuration.minecraft.bukkit.source;

import cc.carm.lib.configuration.core.ConfigInitializer;
import cc.carm.lib.configuration.core.source.impl.FileConfigProvider;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public abstract class CraftConfigProvider extends FileConfigProvider<CraftSectionWrapper> {

    public static final char SEPARATOR = '.';

    protected ConfigInitializer<? extends CraftConfigProvider> initializer;
    protected YamlConfiguration configuration;

    public CraftConfigProvider(@NotNull File file) {
        super(file);
    }

    public abstract void initializeConfig();

    @Override
    public @NotNull CraftSectionWrapper getConfiguration() {
        return CraftSectionWrapper.of(this.configuration);
    }

    @Override
    protected void onReload() throws Exception {
        configuration.load(getFile());
    }

    @Override
    public void save() throws Exception {
        configuration.save(getFile());
    }

    @Override
    public @NotNull ConfigInitializer<? extends CraftConfigProvider> getInitializer() {
        return this.initializer;
    }

}
