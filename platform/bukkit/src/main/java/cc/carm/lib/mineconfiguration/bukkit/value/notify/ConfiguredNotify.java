package cc.carm.lib.mineconfiguration.bukkit.value.notify;

import cc.carm.lib.configuration.core.value.ValueManifest;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import cc.carm.lib.mineconfiguration.bukkit.builder.notify.NotifyConfigBuilder;
import cc.carm.lib.mineconfiguration.bukkit.data.NotifyConfig;
import cc.carm.lib.mineconfiguration.common.utils.ParamsUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ConfiguredNotify extends ConfiguredList<NotifyConfig> {

    public static NotifyConfigBuilder create() {
        return new NotifyConfigBuilder();
    }

    public static final Set<NotifyType<?>> TYPES = new HashSet<>(Arrays.asList(DefaultNotifyTypes.values()));

    public static NotifyType<?> getType(@NotNull String key) {
        return TYPES.stream().filter(type -> type.key.equalsIgnoreCase(key)).findFirst().orElse(null);
    }

    protected final @NotNull String[] params;

    public ConfiguredNotify(@NotNull ValueManifest<List<NotifyConfig>> manifest, @NotNull String[] params) {
        super(manifest, NotifyConfig.class, obj -> Objects.requireNonNull(NotifyConfig.deserialize(String.valueOf(obj))), NotifyConfig::serialize);
        this.params = params;
    }

    public String[] getParams() {
        return params;
    }

    public @NotNull PreparedNotify prepare(@NotNull Object... values) {
        return new PreparedNotify(getNotNull(), ParamsUtils.buildParams(getParams(), values));
    }

    public void send(@NotNull Player player, @NotNull Object... values) {
        prepare(values).to(player);
    }

    public void send(@NotNull Iterable<? extends Player> players, @NotNull Object... values) {
        prepare(values).to(players);
    }

    public void sendAll(@NotNull String... values) {
        prepare(values).toAll();
    }

}
