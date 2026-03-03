package it.shulkered.CopperAX.BukkitEvents;

import it.shulkered.CopperAX.Utils.ExploitManager;
import it.shulkered.CopperAX.Utils.Exploits;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Arrays;
import java.util.List;

public class BlockPlaceListener implements Listener {
    private final ExploitManager exploitManager;

    public BlockPlaceListener(ExploitManager exploitManager) {
        this.exploitManager = exploitManager;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material blockType = block.getType();
        Material blockAgainst = event.getBlockAgainst().getType();

        if(blockType == Material.REDSTONE || blockType == Material.REDSTONE_WIRE) {
            Block below = block.getRelative(BlockFace.DOWN);

            if(below.getType().name().contains("TRAPDOOR")) {
                exploitManager.reportExploit(player, Exploits.TRAPDOOR_CRASH);
                event.setCancelled(true);
            }
        }

        List<Material> blacklistedTypes = Arrays.asList(
                Material.AIR,
                Material.WATER,
                Material.LAVA
        );

        if(blacklistedTypes.stream().anyMatch(blockAgainst::equals)) {
            exploitManager.reportExploit(player, Exploits.ILLEGAL_ITEM_EXPLOIT);
            event.setCancelled(true);
        }

        blacklistedTypes = Arrays.asList(
                Material.LIGHT,
                Material.BEDROCK,
                Material.BARRIER
        );

        if(player.getGameMode() != GameMode.CREATIVE && blacklistedTypes.stream().anyMatch(block.getType()::equals)) {
            exploitManager.reportExploit(player, Exploits.ILLEGAL_ITEM_EXPLOIT);
            event.setCancelled(true);
        }
    }
}
