package cc.carm.lib.mineconfiguration.bukkit.source;

import cc.carm.lib.configuration.commentable.Commentable;
import cc.carm.lib.configuration.commentable.CommentableOptions;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.file.FileConfigSource;
import cc.carm.lib.configuration.source.section.ConfigureSection;
import cc.carm.lib.yamlcommentupdater.CommentedSection;
import cc.carm.lib.yamlcommentupdater.CommentedYAMLWriter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class BukkitSource extends FileConfigSource<BukkitSection, YamlConfiguration, BukkitSource>
        implements CommentedSection {

    protected @Nullable BukkitSection rootSection;

    public BukkitSource(@NotNull ConfigurationHolder<? extends BukkitSource> holder,
                        @NotNull File file, @Nullable String resourcePath) {
        super(holder, System.currentTimeMillis(), file, resourcePath);
        initialize();
    }

    public void initialize() {
        try {
            initializeFile();
            onReload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected BukkitSource self() {
        return this;
    }

    @Override
    public @NotNull YamlConfiguration original() {
        return (YamlConfiguration) section().data(); // #data() of Root Section always returns YamlConfiguration
    }

    @Override
    public @NotNull BukkitSection section() {
        return Objects.requireNonNull(rootSection, "Root section has not been initialized");
    }

    @Override
    public void save() throws Exception {
        CommentedYAMLWriter writer = new CommentedYAMLWriter(
                String.valueOf(this.pathSeparator()), 2,
                holder.options().get(CommentableOptions.COMMENT_EMPTY_VALUE)
        );
        try {
            fileWriter(w -> w.write(writer.saveToString(this)));
        } catch (Exception ex) {
            fileWriter(w -> w.write(original().saveToString()));
        }
    }

    @Override
    protected void onReload() throws Exception {
        YamlConfiguration configuration = fileReader(YamlConfiguration::loadConfiguration);
        this.rootSection = new BukkitSection(this, null, configuration);
    }

    @Override
    public String serializeValue(@NotNull String key, @NotNull Object value) {
        FileConfiguration temp = new YamlConfiguration();
        temp.set(key, value);
        return temp.saveToString();
    }

    @Override
    public @Nullable Set<String> getKeys(@Nullable String sectionKey, boolean deep) {
        if (sectionKey == null) return section().getKeys(deep);
        ConfigureSection sub = section().getSection(sectionKey);
        if (sub == null) return null;
        return sub.getKeys(deep);
    }

    @Override
    public @Nullable Object getValue(@NotNull String key) {
        return get(key);
    }

    @Override
    public @Nullable String getInlineComment(@NotNull String key) {
        return Commentable.getInlineComment(holder(), key);
    }

    @Override
    public @Nullable List<String> getHeaderComments(@Nullable String key) {
        return Commentable.getHeaderComments(holder(), key);
    }

    @Override
    public @Nullable List<String> getFooterComments(@Nullable String key) {
        return Commentable.getFooterComments(holder(), key);
    }

}
