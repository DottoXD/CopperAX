package it.shulkered.CopperAX.PacketEvents.Checks;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEditBook;
import it.shulkered.CopperAX.PacketEvents.PacketReceiveListener;
import it.shulkered.CopperAX.Utils.Exploits;

public class EditBookPacket {
    public EditBookPacket(PacketReceiveEvent event, PacketReceiveListener listener) {
        WrapperPlayClientEditBook edit = new WrapperPlayClientEditBook(event);
        String title = edit.getTitle();

        for(String page : edit.getPages()) {
            if(page.contains("run_command") || page.contains("translation.test.invalid")) {
                listener.exploitManager.gestisciExploit(event, Exploits.ILLEGAL_ITEM_EXPLOIT);
                return;
            }
        }
        
        if(title != null && edit.getTitle().length() > 32) listener.exploitManager.gestisciExploit(event, Exploits.BOOK_CRASH);
    }
}
