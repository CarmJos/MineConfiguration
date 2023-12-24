package cc.carm.lib.mineconfiguration.bukkit.value.notify.type;

import cc.carm.lib.mineconfiguration.bukkit.data.TitleConfig;
import cc.carm.lib.mineconfiguration.bukkit.value.notify.NotifyType;
import com.cryptomorin.xseries.messages.Titles;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitleNotify extends NotifyType<TitleConfig> {

    //Content format line1{N}line2
    public static final Pattern CONTENT_FORMAT = Pattern.compile("(?<line1>.+?)(?:\\{n}(?<line2>.+))?");
    //Param format fadeIn,stay,fadeOut
    public static final Pattern PARAM_FORMAT = Pattern.compile("(?<fadeIn>\\d+),(?<stay>\\d+),(?<fadeOut>\\d+)");

    public TitleNotify(String key) {
        super(key, TitleConfig.class);
    }

    @Override
    public @Nullable TitleConfig parseMeta(@Nullable String param, @Nullable String content) {
        if (content == null) return null;

        Matcher contentMatcher = CONTENT_FORMAT.matcher(content);
        if (!contentMatcher.matches()) return null;

        if (param == null) {
            return TitleConfig.of(contentMatcher.group("line1"), contentMatcher.group("line2"));
        }

        Matcher paramMatcher = PARAM_FORMAT.matcher(param);
        if (!paramMatcher.matches()) {
            return TitleConfig.of(contentMatcher.group("line1"), contentMatcher.group("line2"));
        }

        return TitleConfig.of(
                contentMatcher.group("line1"), contentMatcher.group("line2"),
                Integer.parseInt(paramMatcher.group("fadeIn")),
                Integer.parseInt(paramMatcher.group("stay")),
                Integer.parseInt(paramMatcher.group("fadeOut"))
        );
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
