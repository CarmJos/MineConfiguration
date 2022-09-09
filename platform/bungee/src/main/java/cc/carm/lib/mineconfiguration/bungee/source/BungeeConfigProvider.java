package cc.carm.lib.mineconfiguration.bungee.source;

import cc.carm.lib.configuration.core.ConfigInitializer;
import cc.carm.lib.configuration.core.source.ConfigurationComments;
import cc.carm.lib.configuration.core.source.impl.FileConfigProvider;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class BungeeConfigProvider extends FileConfigProvider<BungeeSectionWrapper> {

    protected static final char SEPARATOR = '.';

    protected ConfigurationProvider loader;
    protected Configuration configuration;
    protected ConfigInitializer<BungeeConfigProvider> initializer;

    protected BungeeYAMLComments comments = new BungeeYAMLComments();

    public BungeeConfigProvider(@NotNull File file, @NotNull ConfigurationProvider loader) {
        super(file);
        this.loader = loader;
    }

    public BungeeConfigProvider(@NotNull File file, @NotNull Class<? extends ConfigurationProvider> providerClass) {
        this(file, ConfigurationProvider.getProvider(providerClass));
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
    public @Nullable ConfigurationComments getComments() {
        return this.comments;
    }

    @Override
    public void save() throws Exception {
        getLoader().save(configuration, file);
        if (getLoader() instanceof YamlConfiguration) {
            StringWriter writer = new StringWriter();
            this.comments.writeComments(configuration, new BufferedWriter(writer));
            String value = writer.toString(); // config contents

            Path toUpdatePath = getFile().toPath();
            if (!value.equals(new String(Files.readAllBytes(toUpdatePath), StandardCharsets.UTF_8))) {
                Files.write(toUpdatePath, value.getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    @Override
    public @NotNull ConfigInitializer<BungeeConfigProvider> getInitializer() {
        return this.initializer;
    }

    public ConfigurationProvider getLoader() {
        return loader;
    }
}
