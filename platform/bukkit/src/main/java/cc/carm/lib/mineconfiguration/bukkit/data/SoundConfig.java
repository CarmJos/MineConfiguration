package cc.carm.lib.mineconfiguration.bukkit.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class SoundConfig {

    protected @NotNull String typeName;
    protected @Nullable Sound type;
    protected float volume;
    protected float pitch;

    public SoundConfig(@NotNull String typeName) {
        this(typeName, 1, 1);
    }

    public SoundConfig(@NotNull String typeName, float volume) {
        this(typeName, volume, 1);
    }

    public SoundConfig(@NotNull String typeName, float volume, float pitch) {
        this(typeName, Arrays.stream(Sound.values()).filter(s -> s.name().equalsIgnoreCase(typeName)).findFirst().orElse(null), volume, pitch);
    }

    public SoundConfig(@NotNull String typeName, @Nullable Sound type, float volume, float pitch) {
        this.typeName = typeName;
        this.type = type;
        this.volume = volume;
        this.pitch = pitch;
    }

    public void playTo(Player player) {
        if (type == null) return;
        player.playSound(player.getLocation(), type, volume, pitch);
    }

    public void playAt(Location location) {
        if (type == null) return;
        if (location.getWorld() == null) return;
        location.getWorld().playSound(location, type, volume, pitch);
    }

    public void playToAll() {
        Bukkit.getOnlinePlayers().forEach(this::playTo);
    }

    public @NotNull String getTypeName() {
        return typeName;
    }

    public @Nullable Sound getType() {
        return type;
    }

    public float getPitch() {
        return pitch;
    }

    public float getVolume() {
        return volume;
    }

    public void setType(@NotNull Sound type) {
        this.typeName = type.name();
        this.type = type;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public @NotNull String serialize() {
        if (pitch != 1) {
            return typeName + ":" + volume + ":" + pitch;
        } else if (volume != 1) {
            return typeName + ":" + volume;
        } else {
            return typeName;
        }
    }

    @Contract("null -> null")
    public static @Nullable SoundConfig deserialize(@Nullable String string) throws Exception {
        if (string == null || string.isEmpty()) return null;

        String[] args = string.contains(":") ? string.split(":") : new String[]{string};
        if (args.length < 1) return null;

        try {
            return new SoundConfig(
                    args[0],
                    Sound.valueOf(args[0]),
                    (args.length >= 2) ? Float.parseFloat(args[1]) : 1,
                    (args.length >= 3) ? Float.parseFloat(args[2]) : 1
            );
        } catch (Exception exception) {
            throw new Exception("Sound " + string + " wasn't configured correctly.", exception);
        }
    }


}
