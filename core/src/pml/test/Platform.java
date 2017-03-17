package pml.test;

import org.oscim.core.GeoPoint;

/**
 * Created by Gerber Lóránt on 2017. 03. 15..
 */

public interface Platform {
    GeoPoint getPos();
    void tick();
}
