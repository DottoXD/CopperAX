package it.shulkered.CopperAX.BukkitEvents;

import it.shulkered.CopperAX.Utils.ExploitManager;
import it.shulkered.CopperAX.Utils.Exploits;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class ProjectileLaunchListener implements Listener {
    private final ExploitManager exploitManager;

    public ProjectileLaunchListener(ExploitManager exploitManager) {
        this.exploitManager = exploitManager;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Entity entity = event.getEntity();

        if(event.getEntity().getVelocity().lengthSquared() > 15) {
            if(entity instanceof Player) exploitManager.reportExploit((Player) entity, Exploits.FAST_PROJECTILE_EXPLOIT);
            event.setCancelled(true);
        }
    }
}
