package pml.test.packets;

/**
 * Created by Gerber Lóránt Viktor on 2017. 03. 23..
 */

/**
 * Notification from the server that a player has moved
 */
public class C05PlayerMoved extends Packet {
    /**
     * UUID of the player
     */
    public String uuid;
    /**
     * New coordinates of the player
     */
    public double newX, newY;
}
