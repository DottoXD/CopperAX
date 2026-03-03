package it.shulkered.CopperAX.BukkitEvents;

import it.shulkered.CopperAX.CopperAX;
import it.shulkered.CopperAX.Utils.ExploitManager;
import it.shulkered.CopperAX.Utils.Exploits;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTeleportEvent;

import java.util.List;
import java.util.UUID;

public class EntityTeleportListener implements Listener {
    private final ExploitManager exploitManager;
    private final CopperAX plugin;
    private List<UUID> teleportPlayers = List.of();

    public EntityTeleportListener(ExploitManager exploitManager, CopperAX copperAX) {
        this.exploitManager = exploitManager;
        this.plugin = copperAX;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityTeleport(EntityTeleportEvent event) {
        Entity entity = event.getEntity();

        if(event.getTo() == null) return;

        if(!entity.isEmpty() && entity.getWorld().getEnvironment().equals(World.Environment.THE_END)) {
            if(entity instanceof Player) exploitManager.reportExploit((Player) entity, Exploits.GATEWAY_CRASH);
            event.setCancelled(true);
            return;
        }

        if(event.getFrom() .getWorld() == null || event.getTo().getWorld() == null) return;

        if(!(event.getFrom().getWorld().getUID().equals(event.getTo().getWorld().getUID())) && entity instanceof Player player) {
            if(teleportPlayers.contains(player.getUniqueId())) {
                exploitManager.reportExploit(player, Exploits.INVALID_MOVEMENT_EXPLOIT);
            } else {
                UUID uuid = player.getUniqueId();
                teleportPlayers.add(uuid);

                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                   @Override
                   public void run() {
                       teleportPlayers.remove(uuid);
                   }
                }, 20L);
            }
        }
    }
}
