package it.shulkered.CopperAX.PacketEvents.Checks;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientVehicleMove;
import it.shulkered.CopperAX.PacketEvents.PacketReceiveListener;
import it.shulkered.CopperAX.Utils.Exploits;

public class VehicleMovePacket {
    public VehicleMovePacket(PacketReceiveEvent event, PacketReceiveListener listener) {
        WrapperPlayClientVehicleMove wrapper = new WrapperPlayClientVehicleMove(event);

        Vector3d location = wrapper.getPosition();
        if(Double.isNaN(location.getX()) || Double.isNaN(location.getY()) || Double.isNaN(location.getZ())) listener.exploitManager.gestisciExploit(event, Exploits.INVALID_VEHICLE_EXPLOIT);
        else if(!Float.isFinite(wrapper.getPitch()) || !Float.isFinite(wrapper.getYaw())) listener.exploitManager.gestisciExploit(event, Exploits.INVALID_VEHICLE_EXPLOIT);
    }
}
