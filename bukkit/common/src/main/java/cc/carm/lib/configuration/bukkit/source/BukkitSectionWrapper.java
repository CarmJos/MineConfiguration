package cc.carm.lib.configuration.bukkit.source;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import org.bukkit.configuration.ConfigurationSection;

public abstract class BukkitSectionWrapper implements ConfigurationWrapper {

    private final ConfigurationSection section;

    private BukkitSectionWrapper(ConfigurationSection section) {
        this.section = section;
    }

    public ConfigurationSection getSourceSection() {
        return section;
    }

}
