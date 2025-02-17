package cc.carm.lib.mineconfiguration.bukkit.value.notify.type.standard;

import cc.carm.lib.mineconfiguration.bukkit.data.SoundConfig;
import cc.carm.lib.mineconfiguration.bukkit.value.notify.type.NotifyType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class SoundNotify extends NotifyType<SoundConfig> {


    public SoundNotify(@NotNull String key) {
        super(key, SoundConfig.class);
    }

    @Override
    public @Nullable SoundConfig parseMeta(@Nullable String param, @Nullable String content) {
        if (content == null) return null;

        String[] args = param == null ? new String[0] : param.split(",");
        try {
            return new SoundConfig(content.trim(),
                    (args.length >= 1) ? Float.parseFloat(args[0]) : 1,
                    (args.length >= 2) ? Float.parseFloat(args[1]) : 1
            );
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public @NotNull String serializeConfig(@Nullable SoundConfig meta) {
        if (meta == null) return "[" + key + "] ";

        if (meta.getVolume() == 1 && meta.getPitch() == 1) {
            return "[" + key + "] " + meta.getTypeName();
        } else {
            return "[" + key + "@" + +meta.getVolume() + "," + meta.getPitch() + "] " + meta.getTypeName();
        }
    }

    @Override
    public void execute(@NotNull Player player, @Nullable SoundConfig meta, @NotNull Map<String, Object> placeholders) {
        if (meta != null) meta.playTo(player);
    }

}
