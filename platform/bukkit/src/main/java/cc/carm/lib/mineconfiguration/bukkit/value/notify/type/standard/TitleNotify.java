package cc.carm.lib.mineconfiguration.bukkit.value.notify.type.standard;

import cc.carm.lib.mineconfiguration.bukkit.data.TitleConfig;
import cc.carm.lib.mineconfiguration.bukkit.value.notify.type.NotifyType;
import com.cryptomorin.xseries.messages.Titles;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitleNotify extends NotifyType<TitleConfig> {

    //Param format fadeIn,stay,fadeOut
    public static final Pattern PARAM_FORMAT = Pattern.compile("(?<fadeIn>\\d+),(?<stay>\\d+),(?<fadeOut>\\d+)");

    public TitleNotify(String key) {
        super(key, TitleConfig.class);
    }

    @Override
    public @Nullable TitleConfig parseMeta(@Nullable String param, @Nullable String content) {
        if (content == null) return null;

        String[] lines = content.split("\\{n}");
        if (lines.length == 0) return null;

        String line1 = lines[0];
        String line2 = lines.length > 1 ? lines[1] : null;

        if (param == null) {
            return TitleConfig.of(line1, line2);
        }

        Matcher paramMatcher = PARAM_FORMAT.matcher(param);
        if (!paramMatcher.matches()) {
            return TitleConfig.of(line1, line2);
        }

        return TitleConfig.of(
                line1, line2,
                Integer.parseInt(paramMatcher.group("fadeIn")),
                Integer.parseInt(paramMatcher.group("stay")),
                Integer.parseInt(paramMatcher.group("fadeOut"))
        );
    }

    @Override
    public @NotNull String serializeConfig(@Nullable TitleConfig meta) {
        if (meta == null || (meta.line1() == null && meta.line2() == null)) return "[" + key + "] ";

        String line1 = meta.line1() == null ? "" : meta.line1();
        String line2 = meta.line2() == null ? "" : meta.line2();

        if (meta.isStandardTime()) {
            return "[" + key + "] " + line1 + "{n}" + line2;
        } else
            return "[" + key + "@" + meta.fadeIn() + "," + meta.stay() + "," + meta.fadeOut() + "] " + line1 + "{n}" + line2;
    }

    @Override
    public void execute(@NotNull Player player, @Nullable TitleConfig meta, @NotNull Map<String, Object> placeholders) {
        if (meta != null) meta.send(player, placeholders, Titles::sendTitle);
    }

}
