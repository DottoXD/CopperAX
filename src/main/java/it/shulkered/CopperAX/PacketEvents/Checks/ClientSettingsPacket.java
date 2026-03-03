package it.shulkered.CopperAX.PacketEvents.Checks;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSettings;
import it.shulkered.CopperAX.PacketEvents.PacketReceiveListener;

public class ClientSettingsPacket {
    public ClientSettingsPacket(PacketReceiveEvent event, PacketReceiveListener listener) {
        WrapperPlayClientSettings wrapper = new WrapperPlayClientSettings(event);
        wrapper.setViewDistance(Math.max(0, wrapper.getViewDistance()));
    }
}
