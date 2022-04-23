package cc.carm.lib.configuration.spigot.source;

import cc.carm.lib.configuration.core.ConfigInitializer;
import cc.carm.lib.configuration.core.source.ConfigCommentInfo;
import cc.carm.lib.configuration.craft.source.CraftConfigProvider;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpigotConfigProvider extends CraftConfigProvider {

    public SpigotConfigProvider(@NotNull File file) {
        super(file);
    }

    @Override
    public void initializeConfig() {
        this.configuration = YamlConfiguration.loadConfiguration(file);
        this.initializer = new ConfigInitializer<>(this);
    }

    @Override
    public void setComment(@Nullable String path, @Nullable ConfigCommentInfo commentInfo) {
        if (path == null) {
            if (commentInfo == null) this.configuration.options().setFooter(null);
            else if (!String.join("", commentInfo.getComments()).isEmpty()) {
                this.configuration.options().setFooter(Arrays.asList(commentInfo.getComments()));
            }
        } else {
            if (commentInfo == null) this.configuration.setComments(path, null);
            else {
                List<String> comments = new ArrayList<>();

                if (!String.join("", commentInfo.getComments()).isEmpty()) {
                    if (commentInfo.startWrap()) comments.add("");
                    comments.addAll(Arrays.asList(commentInfo.getComments()));
                    if (commentInfo.endWrap()) comments.add("");
                } else if (commentInfo.startWrap() || commentInfo.endWrap()) {
                    comments.add("");
                }

                this.configuration.setComments(path, comments);
            }
        }
    }

    @Override
    public @Nullable ConfigCommentInfo getComment(@Nullable String path) {
        return null;
    }

}
