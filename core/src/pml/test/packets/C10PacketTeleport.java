package pml.test.packets;

/**
 * Created by Gerber Lóránt on 2017. 04. 09..
 */

/**
 * Requests the server to teleport to the specified player
 */
public class C10PacketTeleport {
    /**
     * The player's UUID
     */
    public String toUuid;
}
