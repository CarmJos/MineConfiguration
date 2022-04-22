package cc.carm.lib.configuration.bukkit.data;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SoundConfig {

    protected Sound type;
    protected float volume;
    protected float pitch;

    public SoundConfig(Sound type) {
        this(type, 1, 1);
    }

    public SoundConfig(Sound type, float volume) {
        this(type, volume, 1);
    }

    public SoundConfig(Sound type, float volume, float pitch) {
        this.type = type;
        this.volume = volume;
        this.pitch = pitch;
    }

    public void playTo(Player player) {
        player.playSound(player.getLocation(), type, volume, pitch);
    }

    public void playToAll() {
        Bukkit.getOnlinePlayers().forEach(this::playTo);
    }

    public @NotNull String serialize() {
        if (pitch != 1) {
            return type.name() + ":" + volume + ":" + pitch;
        } else if (volume != 1) {
            return type.name() + ":" + volume;
        } else {
            return type.name();
        }
    }

    @Contract("null -> null")
    public static @Nullable SoundConfig deserialize(@Nullable String string) throws Exception {
        if (string == null) return null;

        String[] args = string.contains(":") ? string.split(":") : new String[]{string};
        if (args.length < 1) return null;

        try {
            return new SoundConfig(
                    Sound.valueOf(args[0]),
                    (args.length >= 2) ? Float.parseFloat(args[1]) : 1,
                    (args.length >= 3) ? Float.parseFloat(args[2]) : 1
            );
        } catch (Exception exception) {
            throw new Exception("Sound " + string + " wasn't configured correctly.", exception);
        }
    }


}
