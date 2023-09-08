import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredItem;
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
                "测试lore的倒数第一行"
        );

        List<String> replace = Arrays.asList("> 插入的点击行1", "> 插入的点击行2");
        Map<String, List<String>> inserted = new HashMap<>();
        inserted.put("click-lore", replace);

        System.out.println(ConfiguredItem.insertLore(original, inserted));
    }


    @Test
    public void parse() {
        System.out.println(parse("#click-lore#{1,0}"));
        System.out.println(parse("#click-lore#{1,2}"));
        System.out.println(parse("#click-lore#{1}"));
        System.out.println(parse("#click-lore#{我}"));
    }

    public static String parse(String line) {
        Matcher matcher = ConfiguredItem.LORE_INSERT_PATTERN.matcher(line);
        if (!matcher.matches()) {
            return line;
        } else {
            String path = matcher.group(1);
            String offset = matcher.group(2);
            return "Path -> " + path + " Offset-> " + offset;
        }
    }

    @Test
    public void offset() {

        System.out.println(ConfiguredItem.addLoreOffset(Arrays.asList("测试lore", "第二行"), "{1,-5}"));
        System.out.println(ConfiguredItem.addLoreOffset(Arrays.asList("测试lore", "第二行"), "{1,2}"));
        System.out.println(ConfiguredItem.addLoreOffset(Arrays.asList("测试lore", "第二行"), "{1,0}"));
        System.out.println(ConfiguredItem.addLoreOffset(Arrays.asList("测试lore", "第二行"), "{2}"));
        System.out.println(ConfiguredItem.addLoreOffset(Arrays.asList("测试lore", "第二行"), "{我}"));
        System.out.println(ConfiguredItem.addLoreOffset(Arrays.asList("测试lore", "第二行"), "{我，爱你}"));

    }


}
