package it.shulkered.CopperAX.PacketEvents.Checks;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import it.shulkered.CopperAX.PacketEvents.PacketReceiveListener;
import it.shulkered.CopperAX.Utils.Exploits;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ClickWindowPacket {
    public ClickWindowPacket(PacketReceiveEvent event, Plugin plugin, User user, PacketReceiveListener listener) {
        WrapperPlayClientClickWindow click = new WrapperPlayClientClickWindow(event);
        Player player = plugin.getServer().getPlayer(user.getUUID());
        if(player == null || click.getCarriedItemStack() == null) return;

        int clickType = click.getWindowClickType().ordinal();
        int windowId = click.getWindowId();
        int button = click.getButton();
        int slot = click.getSlot();

        if((clickType == 1 || clickType == 2) && windowId >= 0 && button < 0) {
            listener.exploitManager.gestisciExploit(event, Exploits.INVENTORY_EXPLOIT);
            return;
        }
        else if(windowId >= 0 && clickType == 2 && slot < 0) {
            listener.exploitManager.gestisciExploit(event, Exploits.INVENTORY_EXPLOIT);
            return;
        }
        else if(button < 0 || button > 40) {
            listener.exploitManager.gestisciExploit(event, Exploits.INVENTORY_EXPLOIT);
            return;
        }

        if(PacketEvents.getAPI().getServerManager().getVersion().isNewerThan(ServerVersion.V_1_10)) {
            Inventory top = listener.getTopInventory(player);
            if(top.getType() == InventoryType.LECTERN) {
                listener.exploitManager.gestisciExploit(event, Exploits.LECTERN_CRASH);
                return;
            }
        }

        if(slot != -999 && slot != -1) {
            if(slot < 0 || slot >= player.getOpenInventory().countSlots()) {
                listener.exploitManager.gestisciExploit(event, Exploits.INVENTORY_EXPLOIT);
                return;
            }
        }

        try {
            ItemStack item = player.getOpenInventory().getItem(slot);
            listener.itemChecks(event, player, item);
        } catch(Exception ignored) {}
    }
}
