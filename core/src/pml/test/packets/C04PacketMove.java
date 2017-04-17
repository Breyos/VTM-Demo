package pml.test.packets;

/**
 * Created by Gerber Lóránt Viktor on 2017. 03. 23..
 */

/**
 * Tells the server that the player has moved
 */
public class C04PacketMove extends Packet {
    /**
     * The player's new coordinates
     */
    public double newX, newY;
}
