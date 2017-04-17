package pml.test.packets;

/**
 * Created by Gerber Lóránt on 2017. 03. 27..
 */

/**
 * Tells the server that an {@link pml.test.Attraction} has been collected
 */
public class C09CollectAttraction extends Packet {
    /**
     * The ID of the Attraction
     */
    public String id;

    public String country, county;
}
