package pml.test;


/**
 * Created by Gerber Lóránt Viktor on 2017. 03. 23..
 */

/**
 * Holds a Player returned from the server
 */
public class Player {
    public double x, y;
    public String uuid;
    public String username;

    public Player(String uuid, double x, double y) {
        this.uuid = uuid;
        this.x = x;
        this.y = y;
        this.username = uuid;
    }

    public Player() {
        this.x = 0;
        this.y = 0;
        this.uuid = "";
        this.username = "";
    }
}
