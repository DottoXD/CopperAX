package it.shulkered.CopperAX.PacketEvents.Checks;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import it.shulkered.CopperAX.PacketEvents.PacketReceiveListener;
import it.shulkered.CopperAX.Utils.Exploits;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.NumberConversions;

import java.util.Arrays;
import java.util.List;

public class PluginMessagePacket {
    // Credits to xGinko for the beehives patch
    private static final int SIZE_BITS_X = 26;
    private static final int SIZE_BITS_Z = SIZE_BITS_X;
    private static final int SIZE_BITS_Y = 64 - SIZE_BITS_X - SIZE_BITS_Z;
    private static final int BIT_SHIFT_Z = SIZE_BITS_Y;
    private static final int BIT_SHIFT_X = SIZE_BITS_Y + SIZE_BITS_Z;

    public PluginMessagePacket(PacketReceiveEvent event, Plugin plugin, User user, PacketReceiveListener listener) {
        WrapperPlayClientPluginMessage message = new WrapperPlayClientPluginMessage(event);

        if(message.getChannelName().contains("wdl")) listener.exploitManager.gestisciExploit(event, Exploits.WORLD_DOWNLOADER);
        else if(message.getChannelName().equals("REGISTER")) {
            String payload = new String(message.getData(), Charsets.UTF_8);
            String[] channels = payload.split("\0");

            if(channels.length > 124) listener.exploitManager.gestisciExploit(event, Exploits.CONSOLESPAMMER_EXPLOIT);
        } else if(message.getChannelName().contains("beehive_c2s")) {
            if(distanceSquared(ByteStreams.newDataInput(message.getData()).readLong(), ((Entity) event.getPlayer()).getLocation()) > NumberConversions.square(24)) {
                listener.exploitManager.gestisciExploit(event, Exploits.BEEHIVE_CRASH);
            }
        } else {
                List<String> blacklistedNames = Arrays.asList(
                        "MC|AdvCdm",
                        "MC:AdvCdm"
                );

                if(blacklistedNames.stream().anyMatch(message.getChannelName()::equalsIgnoreCase)) {
                    listener.exploitManager.gestisciExploit(event, Exploits.BOOK_CRASH);
                    return;
                }

                blacklistedNames = Arrays.asList(
                        "MC|BEdit",
                        "MC:BEdit",
                        "MC|BSign",
                        "MC:BSign",
                        "MC|BOpen",
                        "MC:BOpen"
                );

                if(blacklistedNames.stream().anyMatch(message.getChannelName()::equalsIgnoreCase)) {
                    Player player = plugin.getServer().getPlayer(user.getName());
                    if(player == null || !player.isValid()) return;
                    PlayerInventory inventory = player.getInventory();
                    ItemStack itemInHand = inventory.getItem(inventory.getHeldItemSlot());

                    if(itemInHand != null && itemInHand.getType() != Material.BOOK) listener.exploitManager.gestisciExploit(event, Exploits.BOOK_CRASH);
                }
        }
    }

    private double distanceSquared(long packedBlockPos, final Location playerPos) {
        return  NumberConversions.square(unpackLongX(packedBlockPos) - playerPos.getX()) +
                NumberConversions.square(unpackLongY(packedBlockPos) - playerPos.getY()) +
                NumberConversions.square(unpackLongZ(packedBlockPos) - playerPos.getZ());
    }

    private int unpackLongX(long packedPos) {
        return (int)(packedPos << 64 - BIT_SHIFT_X - SIZE_BITS_X >> 64 - SIZE_BITS_X);
    }

    private int unpackLongY(long packedPos) {
        return (int)(packedPos << 64 - SIZE_BITS_Y >> 64 - SIZE_BITS_Y);
    }

    private int unpackLongZ(long packedPos) {
        return (int)(packedPos << 64 - BIT_SHIFT_Z - SIZE_BITS_Z >> 64 - SIZE_BITS_Z);
    }
}
