package it.shulkered.CopperAX.BukkitEvents;

import it.shulkered.CopperAX.Utils.ExploitManager;
import it.shulkered.CopperAX.Utils.Exploits;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class BucketEmptyListener implements Listener {
    private final ExploitManager exploitManager;

    public BucketEmptyListener(ExploitManager exploitManager) {
        this.exploitManager = exploitManager;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        Block relative = event.getBlockClicked().getRelative(event.getBlockFace());

        if(relative.getType() == Material.END_PORTAL || relative.getType() == Material.END_GATEWAY) {
            exploitManager.reportExploit(player, Exploits.ENDER_PORTAL_EXPLOIT);
            event.setCancelled(true);
        }
    }
}
