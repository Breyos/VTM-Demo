package pml.test;

import org.oscim.core.GeoPoint;

/**
 * Created by Gerber Lóránt on 2017. 03. 15..
 */

/**
 * Platform dependent methods
 */
public interface Platform {
    GeoPoint getPos();
    void tick();
    void updateCache(double lat, double lon);
}
