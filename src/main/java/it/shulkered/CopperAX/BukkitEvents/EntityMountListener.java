package it.shulkered.CopperAX.BukkitEvents;

import it.shulkered.CopperAX.Utils.ExploitManager;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityMountEvent;

public class EntityMountListener implements Listener {
    private final ExploitManager exploitManager;

    public EntityMountListener(ExploitManager exploitManager) {
        this.exploitManager = exploitManager;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityMount(EntityMountEvent event) {
        Entity entity = event.getEntity();
        Entity mount = event.getMount();

        if(entity == null || mount == null) return;

        if(entity instanceof Minecart && mount instanceof Boat) event.setCancelled(true);
    }
}
