package it.shulkered.CopperAX.BukkitEvents;

import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

public class ChunkUnloadListener implements Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        Entity[] entities = event.getChunk().getEntities();

        for(Entity entity : entities) {
            InventoryHolder inventoryHolder = (InventoryHolder) entity;
            Inventory inventory = inventoryHolder.getInventory();

            List<HumanEntity> viewers = inventory.getViewers();
            for(HumanEntity viewer : viewers) {
                viewer.closeInventory();
            }
        }
    }
}
