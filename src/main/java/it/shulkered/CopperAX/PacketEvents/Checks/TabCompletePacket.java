package it.shulkered.CopperAX.PacketEvents.Checks;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientTabComplete;
import it.shulkered.CopperAX.PacketEvents.PacketReceiveListener;
import it.shulkered.CopperAX.Utils.Exploits;

public class TabCompletePacket {
    public TabCompletePacket(PacketReceiveEvent event, PacketReceiveListener listener) {
        WrapperPlayClientTabComplete tab = new WrapperPlayClientTabComplete(event);
        String text = tab.getText();
        final int length = text.length();
        int index = 0;

        if(length > 256) listener.exploitManager.gestisciExploit(event, Exploits.SPAM_CRASH);
        else if(length > 64 && ((index = text.indexOf(' ')) == -1) || index >= 64) listener.exploitManager.gestisciExploit(event, Exploits.SPAM_CRASH);
        else if(text.contains("nbt")) {
            int brackets = 0, square = 0;
            for (char c : text.toCharArray()) {
                if (c == '{') square++;
                else if(c == '[') brackets++;
            }

            if(square > 15 || brackets > 25) listener.exploitManager.gestisciExploit(event, Exploits.SPAM_CRASH);
        }
    }
}
