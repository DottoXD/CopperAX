package it.shulkered.CopperAX.BukkitEvents;

import it.shulkered.CopperAX.Utils.ExploitManager;
import it.shulkered.CopperAX.Utils.Exploits;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {
    private final ExploitManager exploitManager;

    public EntityDamageByEntityListener(ExploitManager exploitManager) {
        this.exploitManager = exploitManager;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();

        if(entity instanceof Player) {
            Entity damager = event.getDamager();

            if(damager instanceof Player && entity == damager) {
                exploitManager.reportExploit((Player) damager, Exploits.SELF_DAMAGE_EXPLOIT);
                event.setCancelled(true);
            }
        }
    }
}
