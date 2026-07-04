package it.shulkered.CopperAX;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import it.shulkered.CopperAX.BukkitEvents.*;
import it.shulkered.CopperAX.PacketEvents.PacketLogger;
import it.shulkered.CopperAX.PacketEvents.PacketReceiveListener;
import it.shulkered.CopperAX.Utils.ExploitManager;
import it.shulkered.CopperAX.Utils.NBTDecoder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class CopperAX extends JavaPlugin {
    private PacketEventsAPI<?> packetEventsInstance;
    private ExploitManager exploitManager;
    private PacketLogger logger;
    private FileConfiguration config;

    private final String WDLNew = "wdl:init";
    private final String WDLOld = "wdl|init";

    private final String BeehiveNew = "purpur:beehive_c2s";
    private final String BeehiveOld = "purpur|beehive_c2s";

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        packetEventsInstance = PacketEvents.getAPI();
        this.saveDefaultConfig();

        packetEventsInstance.getSettings()
                .checkForUpdates(false)
                .kickOnPacketException(true);
        packetEventsInstance.load();

        //logger = new PacketLogger(this);

        File logsFolder = new File(getDataFolder(), "logs");
        if(!logsFolder.exists()) logsFolder.mkdir();

        try {
            exploitManager = new ExploitManager(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //ByteToMessageDecoder - decodifica NBT
        //channel pipeline listener

        //packetEventsInstance.getEventManager().registerListener(logger, PacketListenerPriority.MONITOR);
        packetEventsInstance.getEventManager().registerListener(new PacketReceiveListener(this), PacketListenerPriority.HIGH);
    }

    @Override
    public void onEnable() {
        config = getConfig();

        File logsFolder = new File(getDataFolder(), "logs");
        if(!logsFolder.exists()) logsFolder.mkdir();

        packetEventsInstance.init();
        registerListeners();

        PluginMessageListener pluginMessageListener = new PluginMessageListener(exploitManager);

        Bukkit.getMessenger().registerIncomingPluginChannel(this, WDLNew, pluginMessageListener);
        Bukkit.getMessenger().registerIncomingPluginChannel(this, BeehiveNew, pluginMessageListener);
        try {
            Bukkit.getMessenger().registerIncomingPluginChannel(this, WDLOld, pluginMessageListener);
            Bukkit.getMessenger().registerIncomingPluginChannel(this, BeehiveOld, pluginMessageListener);
        } catch(Exception ignore) {}

        getLogger().info(this.getName() + " has been " + ChatColor.GREEN + " enabled!");
    }

    @Override
    public void onDisable() {
        packetEventsInstance.terminate();

        Bukkit.getMessenger().unregisterIncomingPluginChannel(this, WDLNew);
        Bukkit.getMessenger().unregisterIncomingPluginChannel(this, BeehiveNew);
        try {
            Bukkit.getMessenger().unregisterIncomingPluginChannel(this, WDLOld);
            Bukkit.getMessenger().unregisterIncomingPluginChannel(this, BeehiveOld);
        } catch(Exception ignore) {}

        getLogger().info(this.getName() + " has been " + ChatColor.RED + " disabled!");
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new BlockBreakListener(exploitManager), this);
        pluginManager.registerEvents(new BlockDispenseListener(exploitManager), this);
        pluginManager.registerEvents(new BlockPlaceListener(exploitManager), this);
        pluginManager.registerEvents(new BlockRedstoneListener(this), this);
        pluginManager.registerEvents(new BucketEmptyListener(exploitManager), this);
        pluginManager.registerEvents(new ChunkUnloadListener(), this);
        pluginManager.registerEvents(new EntityDamageByEntityListener(exploitManager), this);
        pluginManager.registerEvents(new EntityMountListener(exploitManager), this);
        pluginManager.registerEvents(new EntityPortalListener(this), this);
        pluginManager.registerEvents(new EntityTeleportListener(exploitManager, this), this);
        pluginManager.registerEvents(new EntityToggleGlideListener(exploitManager), this);
        pluginManager.registerEvents(new InventoryClickListener(exploitManager), this);
        pluginManager.registerEvents(new InventoryOpenListener(exploitManager), this);
        pluginManager.registerEvents(new PlayerCommandsListener(exploitManager), this);
        pluginManager.registerEvents(new PlayerInteractListener(exploitManager), this);
        pluginManager.registerEvents(new PlayerJoinListener(exploitManager), this);
        pluginManager.registerEvents(new PlayerMoveListener(exploitManager), this);
        pluginManager.registerEvents(new PlayerQuitListener(exploitManager), this);
        pluginManager.registerEvents(new PlayerShearEntityListener(exploitManager), this);
        pluginManager.registerEvents(new ProjectileLaunchListener(exploitManager), this);
        pluginManager.registerEvents(new SignChangeListener(exploitManager), this);
        pluginManager.registerEvents(new StructureGrowListener(), this);

        if(PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21)) pluginManager.registerEvents(new PlayerTeleportListener(), this);
    }

    public ExploitManager getExploitManager() {
        return exploitManager;
    }

    public PacketLogger getPacketLogger() {
        return logger;
    }
}
