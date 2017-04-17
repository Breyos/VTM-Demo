package pml.test.packets;

/**
 * Created by Gerber Lóránt Viktor on 2017. 03. 23..
 */

/**
 * Request to disconnect from the server
 */
public class C01PacketDisconnect extends Packet {
    /**
     * The reason for the disconnect
     */
    public String reason;
}
