package it.shulkered.CopperAX.PacketEvents;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.*;
import it.shulkered.CopperAX.CopperAX;
import it.shulkered.CopperAX.Utils.ExploitManager;
import org.bukkit.entity.Player;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class PacketLogger implements PacketListener {
    private final CopperAX plugin;
    public final ExploitManager exploitManager;

    public PacketLogger(CopperAX copperAX) {
        this.plugin = copperAX;
        this.exploitManager = plugin.getExploitManager();
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        User user = event.getUser();
        PacketTypeCommon type = event.getPacketType();
        if(user == null || user.getName() == null || type.equals(PacketType.Play.Client.PONG) || type.equals(PacketType.Play.Client.ANIMATION) || type.equals(PacketType.Play.Client.PLAYER_ROTATION) || type.equals(PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION) || type.equals(PacketType.Play.Client.PLAYER_POSITION)) return;

        Player player = plugin.getServer().getPlayer(user.getName());

        // todo: log logic
    }

    public void logPacket(Player player, FileWriter writer, PacketReceiveEvent event) {
        String date = "[" + event.getTimestamp() + "-" + player.getName() + "]";
        StringBuilder data = new StringBuilder("None");

        PacketTypeCommon packetType = event.getPacketType();
        if(packetType == PacketType.Play.Client.CLICK_WINDOW) {
            WrapperPlayClientClickWindow wrapper = new WrapperPlayClientClickWindow(event);
            data = new StringBuilder("(Btn:" + wrapper.getButton() + ",Slot:" + wrapper.getSlot() + ",Item:" + wrapper.getCarriedItemStack().toString() + ",Type:" + wrapper.getWindowClickType().toString() + ")");
        } else if(packetType == PacketType.Play.Client.EDIT_BOOK) {
            WrapperPlayClientEditBook wrapper = new WrapperPlayClientEditBook(event);
            data = new StringBuilder("(Pages:" + wrapper.getPages().size() + ",Slot:" + wrapper.getSlot() + ",Title:" + wrapper.getTitle() + ")");
        } else if(packetType == PacketType.Play.Client.HELD_ITEM_CHANGE) {
            WrapperPlayClientHeldItemChange wrapper = new WrapperPlayClientHeldItemChange(event);
            data = new StringBuilder("(Slot:" + wrapper.getSlot() + ")");
        } else if(packetType == PacketType.Play.Client.PICK_ITEM) {
            WrapperPlayClientPickItem wrapper = new WrapperPlayClientPickItem(event);
            data = new StringBuilder("(Slot:" + wrapper.getSlot() + ")");
        } else if(packetType == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {
            WrapperPlayClientPlayerBlockPlacement wrapper = new WrapperPlayClientPlayerBlockPlacement(event);
            Optional<ItemStack> item = wrapper.getItemStack();
            if(item.isPresent()) data = new StringBuilder("(Amount:" + item.get().getAmount() + ",Type:" + item.get().getType().toString() + ")");
        } else if(packetType == PacketType.Play.Client.PLAYER_FLYING) {
            WrapperPlayClientPlayerFlying wrapper = new WrapperPlayClientPlayerFlying(event);
            Location loc = wrapper.getLocation();
            data = new StringBuilder("(X:" + loc.getX() + ",Y:" + loc.getY() + ",Z:" + loc.getZ() + ",Pitch:" + loc.getPitch() + ",Yaw:" + loc.getYaw() + ")");
        } else if(packetType == PacketType.Play.Client.PLUGIN_MESSAGE) {
            WrapperPlayClientPluginMessage wrapper = new WrapperPlayClientPluginMessage(event);
            data = new StringBuilder("(Name:" + wrapper.getChannelName() + ")");
        } else if(packetType == PacketType.Play.Client.TAB_COMPLETE) {
            WrapperPlayClientTabComplete wrapper = new WrapperPlayClientTabComplete(event);
            data = new StringBuilder("(Length:" + wrapper.getText().length() + ")");
        } else if(packetType == PacketType.Play.Client.UPDATE_SIGN) {
            WrapperPlayClientUpdateSign wrapper = new WrapperPlayClientUpdateSign(event);
            String[] lines = wrapper.getTextLines();
            data = new StringBuilder("(Lines:" + lines.length + ",Length:");

            for(String string : lines) {
                data.append(string.length()).append(",");
            }

            data.append(")");
        } else if(packetType == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION) {
            WrapperPlayClientPlayerPositionAndRotation wrapper = new WrapperPlayClientPlayerPositionAndRotation(event);
            Vector3d pos = wrapper.getPosition();
            data = new StringBuilder("(X:" + pos.x + ",Y:" + pos.y + ",Z:" + pos.z + ",Pitch:" + wrapper.getPitch() + ",Yaw:" + wrapper.getYaw() + ")");
        } else if(packetType == PacketType.Play.Client.PLAYER_POSITION) {
            WrapperPlayClientPlayerPosition wrapper = new WrapperPlayClientPlayerPosition(event);
            Vector3d pos = wrapper.getPosition();
            data = new StringBuilder("(X:" + pos.x + ",Y:" + pos.y + ",Z:" + pos.z + ")");
        } else if(packetType == PacketType.Play.Client.PLAYER_ROTATION) {
            WrapperPlayClientPlayerRotation wrapper = new WrapperPlayClientPlayerRotation(event);
            data = new StringBuilder("Pitch:" + wrapper.getPitch() + ",Yaw:" + wrapper.getYaw());
        } else if(packetType == PacketType.Play.Client.PLAYER_DIGGING) {
            WrapperPlayClientPlayerDigging wrapper = new WrapperPlayClientPlayerDigging(event);
            Vector3i pos = wrapper.getBlockPosition();
            data = new StringBuilder("(X:" + pos.x + ",Y:" + pos.y + ",Z:" + pos.z + ")");
        } else if(packetType == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity(event);
            data = new StringBuilder("(A:" + wrapper.getAction().toString() + ")");
        }

        try {
            writer.write(date + " - " + event.getPacketType().getName() + " - " + data + "\n");
        } catch(IOException e) {
            plugin.getLogger().severe(String.valueOf(e));
            plugin.getLogger().info(date + " - " + event.getPacketType().getName() + " - " + data);
        }
    }
}
