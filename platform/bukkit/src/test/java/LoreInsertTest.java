import cc.carm.lib.mineconfiguration.bukkit.value.item.LoreContent;
import cc.carm.lib.mineconfiguration.bukkit.value.item.PreparedItem;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class LoreInsertTest {


    @Test
    public void insert() {
        List<String> original = Arrays.asList(
                "测试lore的第一行",
                "测试lore的第二行",
                "#click-lore#{1,2}",
                "测试lore的倒数第二行",
                "{--> }#click-lore#{2}",
                "测试lore的倒数第一行"
        );

        List<String> replace = Arrays.asList("> 插入的点击行1", "> 插入的点击行2");
        Map<String, LoreContent<?>> inserted = new HashMap<>();
        inserted.put("click-lore", LoreContent.of(replace));
        PreparedItem.parseLore(null, original, inserted, new HashMap<>()).forEach(System.out::println);
    }


    @Test
    public void parse() {
        System.out.println(parse("{LOVE}#click-lore#{1,0}"));
        System.out.println(parse("#click-lore#{1,2}"));
        System.out.println(parse("#click-lore#{1}"));
        System.out.println(parse("#click-lore#{我}"));
    }

    public static String parse(String line) {
        Matcher matcher = PreparedItem.LORE_INSERT_PATTERN.matcher(line);
        if (!matcher.matches()) {
            return "Failed -> [" + line + "]";
        } else {
            String prefix = matcher.group(1);
            String path = matcher.group(2);

            String offset1 = matcher.group(3);
            String offset2 = matcher.group(4);
            return "Prefix -> [" + prefix + "] Path -> [" + path + "] Offset-> [" + offset1 + "/" + offset2 + "]";
        }
    }


}
