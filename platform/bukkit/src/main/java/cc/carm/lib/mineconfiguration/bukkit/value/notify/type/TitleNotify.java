package cc.carm.lib.mineconfiguration.bukkit.value.notify.type;

import cc.carm.lib.mineconfiguration.bukkit.data.TitleConfig;
import cc.carm.lib.mineconfiguration.bukkit.value.notify.NotifyType;
import com.cryptomorin.xseries.messages.Titles;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class TitleNotify extends NotifyType<TitleConfig> {

    //Title param format fadeIn,Stay,FadeOut
    public static final String PARAM_FORMAT = "(?<fadeIn>\\d+),(?<stay>\\d+),(?<fadeOut>\\d+)";

    public TitleNotify(String key) {
        super(key, TitleConfig.class);
    }

    @Override
    public @Nullable TitleConfig parseMeta(@Nullable String param, @Nullable String content) {
        if (content == null) return null;
        String[] lines = content.split("\\{n}");
        if (param == null) {
            return TitleConfig.of(content, null);
        } else {
            String[] params = param.split(",");
            if (params.length == 3) {
                try {
                    return TitleConfig.of(content, null,
                            Integer.parseInt(params[0]),
                            Integer.parseInt(params[1]),
                            Integer.parseInt(params[2]));
                } catch (Exception ex) {
                    return TitleConfig.of(content, null);
                }
            }
        }
        return null;
    }

    @Override
    public @NotNull String serializeConfig(@Nullable TitleConfig meta) {
        if (meta == null || (meta.getLine1() == null && meta.getLine2() == null)) return "[" + key + "] ";

        String line1 = meta.getLine1() == null ? "" : meta.getLine1().getMessage();
        String line2 = meta.getLine2() == null ? "" : meta.getLine2().getMessage();

        if (meta.isStandardTime()) {
            return "[" + key + "] " + line1 + "{n}" + line1;
        } else
            return "[" + key + "@" + meta.getFadeIn() + "," + meta.getStay() + "," + meta.getFadeOut() + "] " + line1 + "{n}" + line1;
    }

    @Override
    public void execute(@NotNull Player player, @Nullable TitleConfig meta, @NotNull Map<String, Object> placeholders) {
        if (meta != null) meta.send(player, placeholders, Titles::sendTitle);
    }

}
