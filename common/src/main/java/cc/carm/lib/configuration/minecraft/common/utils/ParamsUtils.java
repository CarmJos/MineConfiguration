package cc.carm.lib.configuration.minecraft.common.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ParamsUtils {

    /**
     * 默认的变量格式为 {@code %(变量名)}。
     */
    public static Function<@NotNull String, @NotNull String> DEFAULT_PARAM_FORMATTER = (s) -> "%(" + s + ")";

    public static String[] formatParams(@NotNull Function<String, String> formatter, @NotNull String[] params) {
        return Arrays.stream(params).map(formatter).toArray(String[]::new);
    }

    public static Map<String, Object> buildParams(@Nullable String[] params, @Nullable Object[] values) {
        Map<String, Object> map = new HashMap<>();
        if (params == null || params.length == 0) return map;
        for (int i = 0; i < params.length; i++) {
            map.put(params[i], values.length > i ? values[i] : "?");
        }
        return map;
    }

    public static String setPlaceholders(@NotNull String messages, @NotNull Map<String, Object> placeholders) {
        if (messages.isEmpty()) return messages;

        String parsed = messages;
        for (Map.Entry<String, Object> entry : placeholders.entrySet()) {
            Object value = entry.getValue();
            parsed = parsed.replace(entry.getKey(), value == null ? "" : value.toString());
        }

        return parsed;
    }

}
