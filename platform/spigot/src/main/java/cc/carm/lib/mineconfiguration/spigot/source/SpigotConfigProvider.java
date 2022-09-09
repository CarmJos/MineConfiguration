package cc.carm.lib.mineconfiguration.spigot.source;

import cc.carm.lib.configuration.core.ConfigInitializer;
import cc.carm.lib.configuration.core.source.ConfigurationComments;
import cc.carm.lib.mineconfiguration.bukkit.source.CraftConfigProvider;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class SpigotConfigProvider extends CraftConfigProvider {

    public SpigotConfigProvider(@NotNull File file) {
        super(file);
    }

    protected SpigotYAMLComments comments = null;

    @Override
    public void initializeConfig() {
        this.configuration = YamlConfiguration.loadConfiguration(file);
        this.comments = new SpigotYAMLComments(configuration);
        this.initializer = new ConfigInitializer<>(this);
    }

    @Override
    public @Nullable ConfigurationComments getComments() {
        return this.comments;
    }

}
