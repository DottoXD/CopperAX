package it.shulkered.CopperAX.BukkitEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Arrays;
import java.util.List;

public class PlayerTeleportListener implements Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        List<PlayerTeleportEvent.TeleportCause> teleportCauses = Arrays.asList(
                PlayerTeleportEvent.TeleportCause.END_GATEWAY,
                PlayerTeleportEvent.TeleportCause.END_PORTAL,
                PlayerTeleportEvent.TeleportCause.NETHER_PORTAL
        );

        if(teleportCauses.stream().anyMatch(event.getCause()::equals)) {
            Player player = event.getPlayer();
            //TODO make list for player teleports and their last use
        }
    }
}
