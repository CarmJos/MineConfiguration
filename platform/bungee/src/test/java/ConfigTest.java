import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.annotation.InlineComment;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import cc.carm.lib.mineconfiguration.bungee.MineConfiguration;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class ConfigTest {


    @Test
    public void test() {
        ConfigurationProvider<?> config = MineConfiguration.from(new File("target/config.yml"));
        config.initialize(Configuration.class);
    }


    @HeaderComment({
            "MineConfiguration for BungeeCord",
            "测试实例配置文件", ""
    })
    public static class Configuration extends ConfigurationRoot {

        @InlineComment("是否显示DEBUG消息")
        public static final ConfigValue<Boolean> DEBUG = ConfiguredValue.of(Boolean.class, false);

        @HeaderComment("启动时执行的命令")
        public static final class START_UP {

            @HeaderComment("延迟执行的时间(单位:秒)")
            public static final ConfigValue<Integer> DELAY = ConfiguredValue.of(Integer.class, 30);

            @HeaderComment("循环执行的间隔(单位:秒)")
            public static final ConfigValue<Integer> PERIOD = ConfiguredValue.of(Integer.class, 10);

            @HeaderComment("执行的指令列表")
            @InlineComment("建议以\"\"包裹")
            public static final ConfigValue<List<String>> COMMANDS = ConfiguredList.builder(String.class)
                    .fromString().defaults("alert Commands here!").build();
        }

    }


}
