package it.shulkered.CopperAX.BukkitEvents;

import it.shulkered.CopperAX.Utils.ExploitManager;
import it.shulkered.CopperAX.Utils.Exploits;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class PlayerInteractListener implements Listener {
    private final ExploitManager exploitManager;

    public PlayerInteractListener(ExploitManager exploitManager) {
        this.exploitManager = exploitManager;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerCommand(PlayerInteractEvent event) {
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player player = event.getPlayer();
            PlayerInventory inventory = player.getInventory();

            ItemStack heldItem = inventory.getItem(inventory.getHeldItemSlot());
            if(heldItem != null) {
                if(heldItem.getType().name().endsWith("_BED") || heldItem.getType().name().equals("BED")) {
                    Block clickedBlock = event.getClickedBlock();
                    if(clickedBlock != null) {
                        BlockFace face = event.getBlockFace();

                        Collection<Block> nearBlocks = new HashSet<>();
                        Location loc = clickedBlock.getLocation().add(face.getModX(), face.getModY(), face.getModZ());

                        nearBlocks.add(loc.add(0, -1, 0).getBlock());
                        nearBlocks.add(loc.add(0, 2, 0).getBlock());
                        nearBlocks.add(loc.add(-1, -1, 0).getBlock());
                        nearBlocks.add(loc.add(2, 0, 0).getBlock());
                        nearBlocks.add(loc.add(-1, 0, 1).getBlock());
                        nearBlocks.add(loc.add(0, 0, -2).getBlock());
                        loc.add(0, 0, 1);

                        BlockFace playerFacing = player.getFacing();
                        loc.add(playerFacing.getModX(), playerFacing.getModY(), playerFacing.getModZ());

                        nearBlocks.add(loc.add(0, -1, 0).getBlock());
                        nearBlocks.add(loc.add(0, 2, 0).getBlock());
                        nearBlocks.add(loc.add(-1, -1, 0).getBlock());
                        nearBlocks.add(loc.add(2, 0, 0).getBlock());
                        nearBlocks.add(loc.add(-1, 0, 1).getBlock());
                        nearBlocks.add(loc.add(0, 0, -2).getBlock());
                        loc.add(0, 0, 1);

                        for(Block block : nearBlocks) {
                            List<Material> blacklistedTypes = Arrays.asList(
                                    Material.WHEAT,
                                    Material.POTATOES,
                                    Material.POTATO,
                                    Material.CARROTS,
                                    Material.NETHER_WART,
                                    Material.BEETROOT
                            );

                            if(blacklistedTypes.stream().anyMatch(block.getType()::equals)) {
                                exploitManager.reportExploit(player, Exploits.DUPE_EXPLOIT);
                                event.setCancelled(true);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
