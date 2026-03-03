package it.shulkered.CopperAX.BukkitEvents;

import it.shulkered.CopperAX.CopperAX;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class BlockRedstoneListener implements Listener {
    private final HashMap<Chunk, Integer> redstoneEvents = new HashMap<Chunk, Integer>();
    private final HashSet<Location> clock = new HashSet<>();
    private final CopperAX plugin;

    public BlockRedstoneListener(CopperAX copperAX) {
        this.plugin = copperAX;

        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                redstoneEvents.replaceAll(((chunk, integer) -> 0));
            }
        }, 20L, 20L);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onRedstone(BlockRedstoneEvent event) {

        Location loc = event.getBlock().getLocation();
        Material material = event.getBlock().getType();

        List<Material> list = List.of(
                Material.POWERED_RAIL,
                Material.ACTIVATOR_RAIL,
                Material.DETECTOR_RAIL
        );

        if(list.stream().anyMatch(material::equals)) {
            if(clock.contains(loc)) {
                new BukkitRunnable() {
                    public void run() {
                        event.getBlock().breakNaturally();
                    }
                }.runTask(plugin);
            } else {
                clock.add(loc);
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                    clock.remove(loc);
                }, 20L);
            }
        }

        try {
            Chunk chunk = event.getBlock().getChunk();
            int events = redstoneEvents.get(chunk);
            if(events >= 200) event.setNewCurrent(0); //troppi eventi redstone

            events++;
            redstoneEvents.replace(chunk, events);
        } catch(Exception ignored) {}
    }

    /*@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent event) {
        redstoneEvents.put(event.getChunk(), 0);

        if(!event.isNewChunk()) {
            int totale = 0;

            Chunk chunk = event.getChunk();
            int x = chunk.getX() << 4;
            int z = chunk.getZ() << 4;

            for(int newX = x; newX < x + 16; newX++) {
                for(int newZ = z; newZ < z + 16; newZ++) {
                    for(int y = -63; y < 128; y++) {
                        if(chunk.getBlock(newX, y, newZ).getType().equals(Material.REDSTONE_WIRE)) totale++;
                        if(totale >= 100) chunk.getBlock(newX, y, newZ).breakNaturally();
                    }
                }
            }
        }
    }*/

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        redstoneEvents.remove(event.getChunk());
    }
}
