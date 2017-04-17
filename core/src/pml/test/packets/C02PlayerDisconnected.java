package pml.test.packets;

/**
 * Created by Gerber Lóránt Viktor on 2017. 03. 23..
 */

/**
 * Notification from the server that a player has disconnected
 */
public class C02PlayerDisconnected extends Packet {
    /**
     * UUID of the player
     */
    public String uuid;
}
