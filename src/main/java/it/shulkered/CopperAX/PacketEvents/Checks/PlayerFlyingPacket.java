package it.shulkered.CopperAX.PacketEvents.Checks;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import it.shulkered.CopperAX.PacketEvents.PacketReceiveListener;
import it.shulkered.CopperAX.Utils.Exploits;

public class PlayerFlyingPacket {
    public PlayerFlyingPacket(PacketReceiveEvent event, PacketReceiveListener listener) {
        WrapperPlayClientPlayerFlying wrapper = new WrapperPlayClientPlayerFlying(event);

        if(wrapper.hasPositionChanged()) {
            Location location = wrapper.getLocation();
            if(Double.isNaN(location.getX()) || Double.isNaN(location.getY()) || Double.isNaN(location.getZ())) listener.exploitManager.gestisciExploit(event, Exploits.INVALID_MOVEMENT_EXPLOIT);
            else if(!Float.isFinite(location.getPitch()) || !Float.isFinite(location.getYaw())) listener.exploitManager.gestisciExploit(event, Exploits.INVALID_MOVEMENT_EXPLOIT);
        }
    }
}
