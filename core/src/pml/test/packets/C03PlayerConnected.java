package pml.test.packets;

/**
 * Created by Gerber Lóránt Viktor on 2017. 03. 23..
 */

/**
 * Notification from the server that a player has connected
 */
public class C03PlayerConnected extends Packet {
    /**
     * Information about the player
     */
    public String uuid, username;
    /**
     * The player's current position
     */
    public double initX, initY;
}
