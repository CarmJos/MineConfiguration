package cc.carm.lib.mineconfiguration.bungee.source;

import cc.carm.lib.configuration.core.ConfigInitializer;
import cc.carm.lib.configuration.core.source.ConfigurationComments;
import cc.carm.lib.configuration.core.source.impl.FileConfigProvider;
import cc.carm.lib.yamlcommentupdater.CommentedYAML;
import cc.carm.lib.yamlcommentupdater.CommentedYAMLWriter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BungeeConfigProvider extends FileConfigProvider<BungeeSectionWrapper> implements CommentedYAML {

    protected static final char SEPARATOR = '.';

    protected ConfigurationProvider loader;
    protected Configuration configuration;
    protected ConfigInitializer<BungeeConfigProvider> initializer;

    protected ConfigurationComments comments = new ConfigurationComments();

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
    public @NotNull ConfigurationComments getComments() {
        return this.comments;
    }

    @Override
    public void save() throws Exception {
        try {
            CommentedYAMLWriter.writeWithComments(this, this.file);
        } catch (Exception ex) {
            getLoader().save(configuration, file);
            throw ex;
        }
    }

    @Override
    public @NotNull ConfigInitializer<BungeeConfigProvider> getInitializer() {
        return this.initializer;
    }

    public ConfigurationProvider getLoader() {
        return loader;
    }

    @Override
    public String serializeValue(@NotNull String key, @NotNull Object value) {
        Configuration tmp = new Configuration();// 该对象用于临时记录配置内容
        tmp.set(key, value);
        StringWriter tmpStr = new StringWriter();
        loader.save(tmp, tmpStr);
        return tmpStr.toString();
    }

    @Override
    public Set<String> getKeys(@Nullable String sectionKey, boolean deep) {
        if (sectionKey == null) return BungeeSectionWrapper.getAllKeys(this.configuration);

        Configuration section = configuration.getSection(sectionKey);
        if (section == null) return null;

        return new HashSet<>(section.getKeys());
    }

    @Override
    public @Nullable Object getValue(@NotNull String key) {
        return configuration.get(key);
    }

    @Override
    public @Nullable List<String> getHeaderComments(@Nullable String key) {
        return comments.getHeaderComment(key);
    }

}
