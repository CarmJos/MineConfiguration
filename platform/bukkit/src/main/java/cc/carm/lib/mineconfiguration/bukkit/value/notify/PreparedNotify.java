package cc.carm.lib.mineconfiguration.bukkit.value.notify;

import cc.carm.lib.mineconfiguration.bukkit.data.NotifyConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PreparedNotify {

    protected final @NotNull List<NotifyConfig> notifications;
    protected final @NotNull Map<String, Object> placeholders;

    protected PreparedNotify(@NotNull List<NotifyConfig> notifications, @NotNull Map<String, Object> placeholders) {
        this.notifications = notifications;
        this.placeholders = placeholders;
    }


    /**
     * 向某位接收者发送消息
     *
     * @param receiver 消息的接收者
     */
    public void to(@NotNull Player receiver) {
        notifications.forEach(config -> config.execute(receiver, placeholders));
    }

    /**
     * 向某位接收者发送消息
     *
     * @param receivers 消息的接收者们
     */
    public void to(@NotNull Iterable<? extends Player> receivers) {
        receivers.forEach(this::to);
    }

    public void toAll() {
        to(Bukkit.getOnlinePlayers());
    }


}
