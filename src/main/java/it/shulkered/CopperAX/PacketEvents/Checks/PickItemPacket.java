package it.shulkered.CopperAX.PacketEvents.Checks;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;
import it.shulkered.CopperAX.PacketEvents.PacketReceiveListener;
import it.shulkered.CopperAX.Utils.Exploits;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PickItemPacket {
    public PickItemPacket(PacketReceiveEvent event, Plugin plugin, User user, PacketReceiveListener listener) {
        try {
            WrapperPlayClientHeldItemChange wrapper = new WrapperPlayClientHeldItemChange(event);

            Player player = plugin.getServer().getPlayer(user.getUUID());
            if(player != null) {
                if(!(wrapper.getSlot() >= 0 && wrapper.getSlot() < player.getInventory().getContents().length)) listener.exploitManager.gestisciExploit(event, Exploits.INVENTORY_EXPLOIT);
            }
        } catch(Exception ignored) {}
    }
}
