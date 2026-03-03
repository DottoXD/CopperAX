package it.shulkered.CopperAX.BukkitEvents;

import it.shulkered.CopperAX.Utils.ExploitManager;
import it.shulkered.CopperAX.Utils.Exploits;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
    private final ExploitManager exploitManager;

    public PlayerMoveListener(ExploitManager exploitManager) {
        this.exploitManager = exploitManager;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if(player.isInsideVehicle() && player.getVehicle() != null &&  !player.getVehicle().isValid()) {
            exploitManager.reportExploit(player, Exploits.INVALID_VEHICLE_EXPLOIT);
            player.getVehicle().eject();
        }

        if(!player.isValid() && !player.isDead()) {
            exploitManager.reportExploit(player, Exploits.INVALID_MOVEMENT_EXPLOIT);
            event.setCancelled(true);
        }

        Location destination = event.getTo();
        Location starting = event.getFrom();
        World world = destination.getWorld();
        World startingWorld = starting.getWorld();
        Chunk chunk = destination.getChunk();

        if(chunk == null || !world.isChunkLoaded(chunk) || (starting.distance(destination) > 100000 && world == startingWorld)) {
            exploitManager.reportExploit(player, Exploits.INVALID_LOCATION_EXPLOIT);
            event.setCancelled(true);
        }
    }
}
