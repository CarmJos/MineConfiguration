import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredItem;
import org.junit.Test;

import java.util.Arrays;

public class LoreOffsetTest {


    @Test
    public void lore() {

        System.out.println(ConfiguredItem.addLoreOffset(Arrays.asList("测试lore", "第二行"), "{1,-5}"));
        System.out.println(ConfiguredItem.addLoreOffset(Arrays.asList("测试lore", "第二行"), "{1,2}"));
        System.out.println(ConfiguredItem.addLoreOffset(Arrays.asList("测试lore", "第二行"), "{1,0}"));
        System.out.println(ConfiguredItem.addLoreOffset(Arrays.asList("测试lore", "第二行"), "{2}"));

    }


}
