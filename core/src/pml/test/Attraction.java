package pml.test;

/**
 * Created by Gerber Lóránt on 2017. 03. 26..
 */

/**
 * Holds an Attraction returned from the server
 */
public class Attraction {
    private long reward;
    private String name;
    private double lat, lon;
    private boolean collected;
    private String id;

    public Attraction(long reward, String name, double lat, double lon, String id) {
        this.reward = reward;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.id = id;
    }

    public Attraction() {
    }

    public String getId() {
        return id;
    }

    public long getReward() {
        return reward;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }
}

