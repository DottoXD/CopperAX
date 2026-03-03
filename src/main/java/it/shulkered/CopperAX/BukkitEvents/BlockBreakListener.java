package it.shulkered.CopperAX.BukkitEvents;

import it.shulkered.CopperAX.Utils.ExploitManager;
import it.shulkered.CopperAX.Utils.Exploits;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.Arrays;
import java.util.List;

public class BlockBreakListener implements Listener {
    private final ExploitManager exploitManager;

    public BlockBreakListener(ExploitManager exploitManager) {
        this.exploitManager = exploitManager;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        InventoryType type = player.getOpenInventory().getType();
        Material targetBlockType = event.getBlock().getType();

        if(type != InventoryType.PLAYER && type != InventoryType.CREATIVE) player.closeInventory();
        else {
            List<Material> blacklistedTypes = Arrays.asList(
                    Material.WATER,
                    Material.LAVA
            );

            if (blacklistedTypes.stream().anyMatch(targetBlockType::equals)) {
                exploitManager.reportExploit(player, Exploits.ILLEGAL_ITEM_EXPLOIT);
                event.setCancelled(true);
            }
        }
    }
}
