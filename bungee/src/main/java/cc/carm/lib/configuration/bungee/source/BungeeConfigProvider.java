package cc.carm.lib.configuration.bungee.source;

import cc.carm.lib.configuration.core.ConfigInitializer;
import cc.carm.lib.configuration.core.source.ConfigCommentInfo;
import cc.carm.lib.configuration.core.source.impl.FileConfigProvider;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

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
    public void reload() throws Exception {
        this.configuration = getLoader().load(file);
    }

    @Override
    public void save() throws Exception {
        getLoader().save(configuration, file);
    }

    @Override
    public void setComment(@Nullable String path, @Nullable ConfigCommentInfo comment) {
        // BungeeCord version doesn't support comments
    }

    @Override
    public @Nullable ConfigCommentInfo getComment(@Nullable String path) {
        return null;  // BungeeCord version doesn't support comments
    }

    @Override
    public @NotNull ConfigInitializer<BungeeConfigProvider> getInitializer() {
        return this.initializer;
    }

    public ConfigurationProvider getLoader() {
        return loader;
    }
}
