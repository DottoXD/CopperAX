package it.shulkered.CopperAX.BukkitEvents;

import it.shulkered.CopperAX.Utils.ExploitManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;

public class BlockDispenseListener implements Listener {
    private final ExploitManager exploitManager;

    public BlockDispenseListener(ExploitManager exploitManager) {
        this.exploitManager = exploitManager;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockDispense(BlockDispenseEvent event) {
        Block block = event.getBlock();
        if(block == null) return;

        if((block.getLocation().getY() >= 255 || block.getLocation().getY() <= -64) && block.getType().name().endsWith("SHULKER_BOX")) {
            event.setCancelled(true);
            return;
        }

        if(event.getItem().getType().equals(Material.FLINT_AND_STEEL)) {
            if(event.getBlock().getState().getBlockData() instanceof Directional d) {
                if(d.getFacing().equals(BlockFace.DOWN)) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}
