package pml.test.packets;

/**
 * Created by Gerber Lóránt Viktor on 2017. 03. 23..
 */

/**
 * Requests the server to kick a player
 */
public class C06KickPlayer extends Packet {
    /**
     * UUID of the player
     */
    public String uuid;
    /**
     * The reason for the kick
     */
    public String reason = "No reason provided";
}
