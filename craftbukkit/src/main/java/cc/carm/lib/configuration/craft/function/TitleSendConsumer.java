package cc.carm.lib.configuration.craft.function;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

@FunctionalInterface
public interface TitleSendConsumer {

    /**
     * 向目标玩家发送标题文字
     *
     * @param player  目标玩家
     * @param fadeIn  淡入时间 (ticks)
     * @param stay    保留时间 (ticks)
     * @param fadeOut 淡出时间 (ticks)
     * @param line1   第一行文字
     * @param line2   第二行文字
     */
    void send(@NotNull Player player,
              @Range(from = 0L, to = Integer.MAX_VALUE) int fadeIn,
              @Range(from = 0L, to = Integer.MAX_VALUE) int stay,
              @Range(from = 0L, to = Integer.MAX_VALUE) int fadeOut,
              @NotNull String line1, @NotNull String line2);

}
