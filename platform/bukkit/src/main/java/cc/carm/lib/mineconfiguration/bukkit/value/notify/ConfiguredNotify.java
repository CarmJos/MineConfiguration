package cc.carm.lib.mineconfiguration.bukkit.value.notify;

import cc.carm.lib.configuration.core.value.ValueManifest;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import cc.carm.lib.mineconfiguration.bukkit.data.NotifyConfig;
import cc.carm.lib.mineconfiguration.bukkit.value.notify.type.SoundNotify;
import cc.carm.lib.mineconfiguration.bukkit.value.notify.type.StringNotify;
import cc.carm.lib.mineconfiguration.bukkit.value.notify.type.TitleNotify;
import cc.carm.lib.mineconfiguration.common.utils.ParamsUtils;
import com.cryptomorin.xseries.messages.ActionBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings("rawtypes")
public class ConfiguredNotify extends ConfiguredList<NotifyConfig> {

    public static final Set<NotifyType<?>> TYPES = new HashSet<>();

    static {
        TYPES.add(StringNotify.of("MESSAGE", Player::sendMessage, content -> Optional.ofNullable(content).orElse(" ")));
        TYPES.add(StringNotify.of("MSG", Player::sendMessage));
        TYPES.add(StringNotify.of("ACTIONBAR", ActionBar::sendActionBar));
        TYPES.add(new TitleNotify("TITLE"));
        TYPES.add(new SoundNotify("SOUND"));
    }

    public static NotifyType<?> getType(@NotNull String key) {
        return TYPES.stream().filter(type -> type.key.equals(key)).findFirst().orElse(null);
    }

    protected final @NotNull String[] params;

    public ConfiguredNotify(@NotNull ValueManifest<List<NotifyConfig>> manifest, @NotNull String[] params) {
        super(manifest, NotifyConfig.class, obj -> Objects.requireNonNull(NotifyConfig.deserialize(String.valueOf(obj))), NotifyConfig::serialize);
        this.params = params;
    }

    public String[] getParams() {
        return params;
    }

    public @NotNull PreparedNotify prepare(@NotNull String... values) {
        return new PreparedNotify(getNotNull(), ParamsUtils.buildParams(getParams(), values));
    }

    public void send(@NotNull Player player, @NotNull String... values) {
        prepare(values).to(player);
    }

    public void send(@NotNull Iterable<? extends Player> players, @NotNull String... values) {
        prepare(values).to(players);
    }

    public void sendAll(@NotNull String... values) {
        prepare(values).toAll();
    }

}
