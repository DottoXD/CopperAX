package it.shulkered.CopperAX.PacketEvents.Checks;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import it.shulkered.CopperAX.PacketEvents.PacketReceiveListener;
import it.shulkered.CopperAX.Utils.Exploits;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.bukkit.Bukkit.getPlayer;

public class PlayerBlockPlacementPacket {
    public PlayerBlockPlacementPacket(PacketReceiveEvent event, Plugin plugin, User user, PacketReceiveListener listener) {
        WrapperPlayClientPlayerBlockPlacement wrapper = new WrapperPlayClientPlayerBlockPlacement(event);

        if(wrapper.getItemStack().isPresent()) {
            Material packetMaterial = SpigotReflectionUtil.encodeBukkitItemStack(wrapper.getItemStack().get()).getType();

            boolean modern = PacketEvents.getAPI().getServerManager().getVersion().isNewerThan(ServerVersion.V_1_8_8);
            Player player = getPlayer(user.getName());
            if(player == null) return;

            List<Material> possibleItemStacks = modern ?
                    Arrays.asList(player.getInventory().getItemInMainHand().getType(), player.getInventory().getItemInOffHand().getType()) :
                    Collections.singletonList(player.getItemInHand().getType());

            if (!possibleItemStacks.contains(packetMaterial)) {
                wrapper.getItemStack().get().setNBT(new NBTCompound());
                listener.exploitManager.gestisciExploit(event, Exploits.ILLEGAL_ITEM_EXPLOIT);
            }
        }
    }
}
