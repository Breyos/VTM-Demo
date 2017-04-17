package pml.test.packets;


import java.util.List;

import pml.test.Attraction;
import pml.test.Player;

/**
 * Created by Gerber Lóránt Viktor on 2017. 03. 23..
 */

/**
 * Response from the server sent on a successful authentication.
 */
public class C07AuthSuccess extends Packet {
    /**
     * List of already online players
     */
    public List<Player> onlinePlayers;
    /**
     * Attractions known by the server
     */
    public List<Attraction> attractions;
}
