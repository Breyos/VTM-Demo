package pml.test.packets;

/**
 * Created by Gerber Lóránt Viktor on 2017. 03. 23..
 */

/**
 * Authentication request
 */
public class C00PacketAuth extends Packet {
    /**
     * Information about the player
     */
    public String uuid, customUsername, country, county;
    /**
     * The player's current position
     */
    public double initX, initY;
    /**
     * The player's game version
     */
    public int protocolVersion;
}
