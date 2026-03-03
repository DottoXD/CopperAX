package it.shulkered.CopperAX.PacketEvents.Checks;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUpdateSign;
import it.shulkered.CopperAX.PacketEvents.PacketReceiveListener;
import it.shulkered.CopperAX.Utils.Exploits;

public class UpdateSignPacket {
    public UpdateSignPacket(PacketReceiveEvent event, PacketReceiveListener listener) {
        WrapperPlayClientUpdateSign wrapper = new WrapperPlayClientUpdateSign(event);

        for(String line : wrapper.getTextLines()) {
            if(line.length() > 18) listener.exploitManager.gestisciExploit(event, Exploits.SIGN_CRASH);
        }
    }
}
