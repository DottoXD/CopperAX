package it.shulkered.CopperAX.BukkitEvents;

import it.shulkered.CopperAX.Utils.ExploitManager;
import it.shulkered.CopperAX.Utils.Exploits;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class PluginMessageListener implements org.bukkit.plugin.messaging.PluginMessageListener {
    private final ExploitManager exploitManager;

    public PluginMessageListener(ExploitManager exploitManager) {
        this.exploitManager = exploitManager;
    }

    @Override
    public void onPluginMessageReceived(@NotNull String s, @NotNull Player player, @NotNull byte[] bytes) {
        List<String> blacklistedNames = Arrays.asList(
                "wdl:init",
                "wdl|init"
        );

        if(blacklistedNames.stream().anyMatch(s::equalsIgnoreCase)) exploitManager.reportExploit(player, Exploits.WORLD_DOWNLOADER);
    }
}
