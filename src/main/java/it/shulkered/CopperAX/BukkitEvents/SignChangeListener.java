package it.shulkered.CopperAX.BukkitEvents;

import it.shulkered.CopperAX.Utils.ExploitManager;
import it.shulkered.CopperAX.Utils.Exploits;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener implements Listener {
    private final ExploitManager exploitManager;

    public SignChangeListener(ExploitManager exploitManager) {
        this.exploitManager = exploitManager;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onSignChange(SignChangeEvent event) {
        for(String line : event.getLines()) {
            if(line.contains("run_command") || line.contains("translation.test.invalid")) {
                exploitManager.reportExploit(event.getPlayer(), Exploits.ILLEGAL_ITEM_EXPLOIT);
                event.setCancelled(true);
                return;
            } else if(line.length() >= 80) {
                exploitManager.reportExploit(event.getPlayer(), Exploits.SIGN_CRASH);
                event.setCancelled(true);
                return;
            }
        }
    }
}
