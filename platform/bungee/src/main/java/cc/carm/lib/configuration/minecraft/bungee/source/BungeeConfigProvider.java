package cc.carm.lib.configuration.minecraft.bungee.source;

import cc.carm.lib.configuration.core.ConfigInitializer;
import cc.carm.lib.configuration.core.source.impl.FileConfigProvider;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BungeeConfigProvider extends FileConfigProvider<BungeeSectionWrapper> {

    protected ConfigurationProvider loader;
    protected Configuration configuration;
    protected ConfigInitializer<BungeeConfigProvider> initializer;

    public BungeeConfigProvider(@NotNull File file, ConfigurationProvider loader) {
        super(file);
        this.loader = loader;
    }

    public void initializeConfig() throws IOException {
        this.configuration = getLoader().load(file);
        this.initializer = new ConfigInitializer<>(this);
    }

    @Override
    public @NotNull BungeeSectionWrapper getConfiguration() {
        return BungeeSectionWrapper.of(configuration);
    }

    @Override
    protected void onReload() throws Exception {
        this.configuration = getLoader().load(file);
    }

    @Override
    public void save() throws Exception {
        getLoader().save(configuration, file);
    }

    @Override
    public void setHeaderComment(@Nullable String path, @Nullable List<String> comments) {

    }

    @Override
    public void setInlineComment(@NotNull String path, @Nullable String comment) {

    }

    @Override
    public @Nullable @Unmodifiable List<String> getHeaderComment(@Nullable String path) {
        return null;
    }

    @Override
    public @Nullable String getInlineComment(@NotNull String path) {
        return null;
    }

    @Override
    public @NotNull ConfigInitializer<BungeeConfigProvider> getInitializer() {
        return this.initializer;
    }

    public ConfigurationProvider getLoader() {
        return loader;
    }
}
