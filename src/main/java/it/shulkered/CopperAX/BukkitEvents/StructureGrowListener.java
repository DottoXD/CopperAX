package it.shulkered.CopperAX.BukkitEvents;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

import java.util.Arrays;
import java.util.List;

public class StructureGrowListener implements Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onStructureGrow(StructureGrowEvent event) {
        World world = event.getWorld();

        for(BlockState blockBefore : event.getBlocks()) {
            Block block = world.getBlockAt(blockBefore.getLocation());

            List<Material> blacklistedMaterials = Arrays.asList(
                    Material.END_GATEWAY,
                    Material.END_PORTAL
            );

            if(blacklistedMaterials.stream().anyMatch(block.getType()::equals)) event.setCancelled(true);
        }
    }
}
