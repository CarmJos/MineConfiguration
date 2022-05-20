package cc.carm.lib.mineconfiguration.bukkit.source;

import cc.carm.lib.configuration.core.ConfigInitializer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class BukkitConfigProvider extends CraftConfigProvider {

    protected static final char SEPARATOR = '.';

    protected BukkitYAMLComments bukkitComments = new BukkitYAMLComments();

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
    public void reload() throws Exception {
        configuration.load(getFile());
    }

    @Override
    public void save() throws Exception {
        configuration.save(getFile());

        StringWriter writer = new StringWriter();
        this.bukkitComments.writeComments(configuration, new BufferedWriter(writer));
        String value = writer.toString(); // config contents

        Path toUpdatePath = getFile().toPath();
        if (!value.equals(new String(Files.readAllBytes(toUpdatePath), StandardCharsets.UTF_8))) {
            Files.write(toUpdatePath, value.getBytes(StandardCharsets.UTF_8));
        }
    }

    @Override
    public void setHeaderComment(@Nullable String path, @Nullable List<String> comments) {
        this.bukkitComments.setHeaderComments(path, comments);
    }

    @Override
    public void setInlineComment(@NotNull String path, @Nullable String comment) {
        this.bukkitComments.setInlineComment(path, comment);
    }

    @Override
    @Nullable
    @Unmodifiable
    public List<String> getHeaderComment(@Nullable String path) {
        return this.bukkitComments.getHeaderComment(path);
    }

    @Override
    public @Nullable String getInlineComment(@NotNull String path) {
        return this.bukkitComments.getInlineComment(path);
    }
}
