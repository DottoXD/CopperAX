package it.shulkered.CopperAX.BukkitEvents;

import it.shulkered.CopperAX.CopperAX;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntityPortalListener implements Listener {
    private final CopperAX plugin;
    private List<UUID> portalPlayers = new ArrayList<>();

    public EntityPortalListener(CopperAX copperAX) {
        this.plugin = copperAX;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityPortalEvent(EntityPortalEvent event) {
        Entity entity = event.getEntity();

        if(entity instanceof ChestedHorse horse) {
            if(horse.isCarryingChest()) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPortalEnterEvent(EntityPortalEnterEvent event) {
        Entity entity = event.getEntity();

        if(entity instanceof Player player) {
            if(!portalPlayers.contains(player.getUniqueId())) portalPlayers.add(player.getUniqueId());

            Location loc = player.getLocation();

            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    if(portalPlayers.contains(player.getUniqueId())) {
                        if(
                                player.getLocation().getBlock().getType().equals(Material.NETHER_PORTAL) &&
                                player.getLocation().getBlockX() == loc.getBlockX() &&
                                player.getLocation().getBlockY() == loc.getBlockY() &&
                                player.getLocation().getBlockZ() == loc.getBlockZ()
                        ) player.getLocation().getBlock().setType(Material.AIR);
                    }
                }
            }, 100L);

        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPortalExitEvent(EntityPortalExitEvent event) {
        Entity entity = event.getEntity();

        if(entity instanceof Player player) {
            portalPlayers.remove(player.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onTeleportEvent(PlayerTeleportEvent event) {
        if(
                event.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL &&
                event.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL &&
                event.getCause() != PlayerTeleportEvent.TeleportCause.END_GATEWAY
        ) return;

        Player player = event.getPlayer();
        portalPlayers.remove(player.getUniqueId());
    }
}