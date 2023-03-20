package cc.carm.lib.mineconfiguration.bukkit.source;

import cc.carm.lib.configuration.core.ConfigInitializer;
import cc.carm.lib.configuration.core.source.ConfigurationComments;
import cc.carm.lib.yamlcommentupdater.CommentedYAML;
import cc.carm.lib.yamlcommentupdater.CommentedYAMLWriter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Set;

public class BukkitConfigProvider extends CraftConfigProvider implements CommentedYAML {

    protected static final char SEPARATOR = '.';

    protected @NotNull ConfigurationComments comments = new ConfigurationComments();

    public BukkitConfigProvider(@NotNull File file) {
        super(file);
    }

    public void initializeConfig() {
        this.configuration = YamlConfiguration.loadConfiguration(file);
        this.initializer = new ConfigInitializer<>(this);
    }

    @Override
    public @NotNull CraftSectionWrapper getConfiguration() {
        return CraftSectionWrapper.of(this.configuration);
    }

    @Override
    public void save() throws Exception {
        try {
            CommentedYAMLWriter.writeWithComments(this, this.file);
        } catch (Exception ex) {
            configuration.save(file);
            throw ex;
        }
    }

    @Override
    public @NotNull ConfigurationComments getComments() {
        return this.comments;
    }

    @Override
    public String serializeValue(@NotNull String key, @NotNull Object value) {
        FileConfiguration temp = new YamlConfiguration();
        temp.set(key, value);
        return temp.saveToString();
    }

    @Override
    public Set<String> getKeys(@Nullable String sectionKey, boolean deep) {
        if (sectionKey == null) return configuration.getKeys(deep);

        ConfigurationSection section = configuration.getConfigurationSection(sectionKey);
        if (section == null) return null;

        return section.getKeys(deep);
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
