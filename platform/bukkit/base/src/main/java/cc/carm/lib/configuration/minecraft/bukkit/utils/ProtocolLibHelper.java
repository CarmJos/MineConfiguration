package cc.carm.lib.configuration.minecraft.bukkit.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.entity.Player;

public class ProtocolLibHelper {

    @SuppressWarnings("deprecation")
    public static void sendTitle(Player player, long fadeIn, long stay, long fadeOut, String line1, String line2) throws Exception {
        ProtocolManager pm = ProtocolLibrary.getProtocolManager();

        if (line1 != null) {
            PacketContainer packet = pm.createPacket(PacketType.Play.Server.TITLE);
            packet.getTitleActions().write(0, EnumWrappers.TitleAction.TITLE);
            packet.getChatComponents().write(0, WrappedChatComponent.fromText(line1));
            pm.sendServerPacket(player, packet, false);
        }

        if (line2 != null) {
            PacketContainer packet = pm.createPacket(PacketType.Play.Server.TITLE);
            packet.getTitleActions().write(0, EnumWrappers.TitleAction.SUBTITLE);
            packet.getChatComponents().write(0, WrappedChatComponent.fromText(line2));
            pm.sendServerPacket(player, packet, false);
        }

        PacketContainer timePacket = pm.createPacket(PacketType.Play.Server.TITLE);
        timePacket.getTitleActions().write(0, EnumWrappers.TitleAction.TIMES);
        timePacket.getIntegers()
                .write(0, Math.toIntExact(fadeIn))
                .write(1, Math.toIntExact(stay))
                .write(2, Math.toIntExact(fadeOut));
        pm.sendServerPacket(player, timePacket, false);

    }

}
