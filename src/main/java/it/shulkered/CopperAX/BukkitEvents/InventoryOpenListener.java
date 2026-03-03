package it.shulkered.CopperAX.BukkitEvents;

import it.shulkered.CopperAX.Utils.ExploitManager;
import it.shulkered.CopperAX.Utils.Exploits;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class InventoryOpenListener implements Listener {
    private final ExploitManager exploitManager;

    public InventoryOpenListener(ExploitManager exploitManager) {
        this.exploitManager = exploitManager;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if(event.getPlayer() instanceof Player player) {
            if(player.isSleeping()) {
                event.setCancelled(true);
                return;
            }

            try {
                if(event.getInventory().getType().equals(InventoryType.SHULKER_BOX)) {
                    for(ItemStack item : event.getInventory().getContents()) {
                        if(item != null && item.getType().name().endsWith("SHULKER_BOX")) {
                            exploitManager.reportExploit(player, Exploits.INVENTORY_EXPLOIT);
                            event.getInventory().remove(item);
                        }
                    }
                }
            } catch(Exception ignored) {}
        }
    }
}
