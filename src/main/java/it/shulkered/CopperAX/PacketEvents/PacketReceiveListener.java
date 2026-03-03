package it.shulkered.CopperAX.PacketEvents;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import it.shulkered.CopperAX.PacketEvents.Checks.*;
import it.shulkered.CopperAX.CopperAX;
import it.shulkered.CopperAX.Utils.ExploitManager;
import it.shulkered.CopperAX.Utils.Exploits;
import org.bukkit.FireworkEffect;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PacketReceiveListener implements PacketListener {
    private final CopperAX plugin;
    public final ExploitManager exploitManager;
    int limit = 98304;

    public PacketReceiveListener(CopperAX copperAX) {
        this.plugin = copperAX;
        this.exploitManager = plugin.getExploitManager();

        ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
        if(version.isOlderThan(ServerVersion.V_1_12)) limit = 8448;
        else if(version.isOlderThan(ServerVersion.V_1_13)) limit = 24576;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        User user = event.getUser();
        if(user == null) return;

        if(ByteBufHelper.capacity(event.getByteBuf()) >= limit) exploitManager.gestisciExploit(event, Exploits.PACKETS);

        if(event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW) new ClickWindowPacket(event, plugin, user, this);
        else if(event.getPacketType() == PacketType.Play.Client.CLIENT_SETTINGS) new ClientSettingsPacket(event, this);
        else if(event.getPacketType() == PacketType.Play.Client.EDIT_BOOK) new EditBookPacket(event, this);
        else if(event.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE) new HeldItemChange(event, this);
        else if(event.getPacketType() == PacketType.Play.Client.PICK_ITEM) new PickItemPacket(event, plugin, user, this);
        else if(event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) new PlayerBlockPlacementPacket(event, plugin, user, this);
        else if(event.getPacketType() == PacketType.Play.Client.PLAYER_FLYING) new PlayerFlyingPacket(event, this);
        else if(event.getPacketType() == PacketType.Play.Client.PLUGIN_MESSAGE) new PluginMessagePacket(event, plugin, user, this);
        else if(event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) new TabCompletePacket(event, this);
        else if(event.getPacketType() == PacketType.Play.Client.VEHICLE_MOVE) new VehicleMovePacket(event, this);
    }

    public void itemChecks(PacketReceiveEvent event, Player player, ItemStack item) {
        if(item != null) {
            if(item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();

                if(meta != null) {
                    String name = meta.getDisplayName();
                    List<String> lore = meta.getLore();

                    if((name != null && name.length() > 1536) || (lore != null && lore.size() > 64)) exploitManager.gestisciExploit(event, Exploits.ILLEGAL_ITEM_EXPLOIT);
                    else {
                        if(meta instanceof BookMeta) {
                            String title = ((BookMeta) meta).getTitle();
                            String author = ((BookMeta) meta).getAuthor();

                            if((title != null && title.length() > 32) || (author != null && author.length() > 16) || ((BookMeta) meta).getPageCount() > 100) exploitManager.gestisciExploit(event, Exploits.BOOK_CRASH);
                            else {
                                for(String page : ((BookMeta) meta).getPages()) {
                                    int bytes = page.getBytes(StandardCharsets.UTF_8).length;

                                    if(bytes > 798) exploitManager.gestisciExploit(event, Exploits.BOOK_CRASH);
                                }
                            }
                        } else if(meta instanceof BlockStateMeta) {
                            if(((BlockStateMeta) meta).hasBlockState()) {
                                BlockState state = ((BlockStateMeta) meta).getBlockState();

                                if(state instanceof InventoryHolder) {
                                    for(ItemStack invItem : ((InventoryHolder) state).getInventory().getContents()) {
                                        itemChecks(event, player, invItem);
                                    }
                                }
                            }
                        } else if(meta instanceof FireworkMeta fireworkMeta) {

                            try {
                                if(fireworkMeta.getPower() > 3) exploitManager.gestisciExploit(event, Exploits.ILLEGAL_ITEM_EXPLOIT);
                            } catch(Exception ignored) {}

                            if(fireworkMeta.hasEffects()) {
                                int maxEffects = 12;
                                if(fireworkMeta.getEffectsSize() > maxEffects) exploitManager.gestisciExploit(event, Exploits.ILLEGAL_ITEM_EXPLOIT);
                                else {
                                    for(FireworkEffect effect : fireworkMeta.getEffects()) {
                                        if(effect.getColors().size() > maxEffects) exploitManager.gestisciExploit(event, Exploits.ILLEGAL_ITEM_EXPLOIT);
                                        else if(effect.getFadeColors().size() > maxEffects) exploitManager.gestisciExploit(event, Exploits.ILLEGAL_ITEM_EXPLOIT);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public Inventory getTopInventory(Player player) {
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21)) {
            return player.getOpenInventory().getTopInventory();
        } else {
            try {
                Object inv = player.getOpenInventory();
                Method getTopInventory = inv.getClass().getMethod("getTopInventory");
                getTopInventory.setAccessible(true);
                return (Inventory) getTopInventory.invoke(inv);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
