package cc.carm.lib.mineconfiguration.bukkit.value.notify;

import cc.carm.lib.mineconfiguration.bukkit.value.notify.type.SoundNotify;
import cc.carm.lib.mineconfiguration.bukkit.value.notify.type.StringNotify;
import cc.carm.lib.mineconfiguration.bukkit.value.notify.type.TitleNotify;
import com.cryptomorin.xseries.messages.ActionBar;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Optional;

public interface DefaultNotifyTypes {

    StringNotify MESSAGE = StringNotify.of("MESSAGE", Player::sendMessage, content -> Optional.ofNullable(content).orElse(" "));
    StringNotify MSG = StringNotify.of("MSG", Player::sendMessage);
    StringNotify ACTIONBAR = StringNotify.of("ACTIONBAR", ActionBar::sendActionBar);
    TitleNotify TITLE = new TitleNotify("TITLE");
    SoundNotify SOUND = new SoundNotify("SOUND");

    static NotifyType<?>[] values() {
        return new NotifyType<?>[]{MESSAGE, MSG, ACTIONBAR, TITLE, SOUND};
    }

    static NotifyType<?> valueOf(String name) {
        return Arrays.stream(values()).filter(type -> type.key.equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}


