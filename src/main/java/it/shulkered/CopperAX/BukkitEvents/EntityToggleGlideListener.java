package it.shulkered.CopperAX.BukkitEvents;

import it.shulkered.CopperAX.Utils.ExploitManager;
import it.shulkered.CopperAX.Utils.Exploits;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

public class EntityToggleGlideListener implements Listener {
    private final ExploitManager exploitManager;

    public EntityToggleGlideListener(ExploitManager exploitManager) {
        this.exploitManager = exploitManager;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityToggleGlide(EntityToggleGlideEvent event) {
        Entity entity = event.getEntity();

        if(!entity.isEmpty() && entity instanceof Player player) {
            if(player.getLocation().getY() >= 255) {
                player.setGliding(false);
                event.setCancelled(true);
                exploitManager.reportExploit(player, Exploits.ELYTRA_EXPLOIT);
            }
        }
    }
}
