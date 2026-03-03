package it.shulkered.CopperAX.PacketEvents.Checks;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;
import it.shulkered.CopperAX.PacketEvents.PacketReceiveListener;
import it.shulkered.CopperAX.Utils.Exploits;

public class HeldItemChange {
    public HeldItemChange(PacketReceiveEvent event, PacketReceiveListener listener) {
        WrapperPlayClientHeldItemChange wrapper = new WrapperPlayClientHeldItemChange(event);

        if(wrapper.getSlot() >= 9 || wrapper.getSlot() < 0) listener.exploitManager.gestisciExploit(event, Exploits.INVENTORY_EXPLOIT);
    }
}
