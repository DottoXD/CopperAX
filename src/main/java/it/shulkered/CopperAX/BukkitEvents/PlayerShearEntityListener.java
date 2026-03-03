package it.shulkered.CopperAX.BukkitEvents;

import it.shulkered.CopperAX.Utils.ExploitManager;
import it.shulkered.CopperAX.Utils.Exploits;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;

public class PlayerShearEntityListener implements Listener {
    private final ExploitManager exploitManager;

    public PlayerShearEntityListener(ExploitManager exploitManager) {
        this.exploitManager = exploitManager;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerShear(PlayerShearEntityEvent event) {
        Player player = event.getPlayer();

        if(player.isDead()) {
            exploitManager.reportExploit(player, Exploits.SHEAR_EXPLOIT);
            event.setCancelled(true);
        }
    }
}
