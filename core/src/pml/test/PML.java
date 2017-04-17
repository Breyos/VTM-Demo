package pml.test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import org.oscim.core.GeoPoint;
import org.oscim.core.MapPosition;
import org.oscim.core.Point;
import org.oscim.core.Tile;
import org.oscim.layers.TileGridLayer;
import org.oscim.layers.tile.buildings.BuildingLayer;
import org.oscim.layers.tile.vector.VectorTileLayer;
import org.oscim.layers.tile.vector.labeling.LabelLayer;
import org.oscim.map.Layers;
import org.oscim.map.Map;
import org.oscim.renderer.GLState;
import org.oscim.renderer.MapRenderer;
import org.oscim.theme.IRenderTheme;
import org.oscim.theme.ThemeFile;
import org.oscim.theme.VtmThemes;
import org.oscim.theme.XmlRenderThemeMenuCallback;
import org.oscim.tiling.TileSource;
import org.oscim.tiling.source.oscimap4.OSciMap4TileSource;

import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pml.test.packets.C00PacketAuth;
import pml.test.packets.C01PacketDisconnect;
import pml.test.packets.C02PlayerDisconnected;
import pml.test.packets.C03PlayerConnected;
import pml.test.packets.C04PacketMove;
import pml.test.packets.C05PlayerMoved;
import pml.test.packets.C06KickPlayer;
import pml.test.packets.C07AuthSuccess;
import pml.test.packets.C08StatsUpdate;
import pml.test.packets.C09CollectAttraction;
import pml.test.packets.C10PacketTeleport;
import pml.test.packets.Packet;

/**
 *  <p>Main game class</p>
 *  @author glorantq
 */
public class PML extends ApplicationAdapter {
    /**
     * SpriteBatch used for rendering
     */
    private SpriteBatch batch;
    /**
     * ShapeRenderer used for rendering basic shapes
     */
    private ShapeRenderer shapeRenderer;
    /**
     * Textures for the player and waypoints
     */
    private Texture img, attractionTexture;
    /**
     * The Map object
     */
    private Map map;
    /**
     * MapRenderer that handles rendering the Map
     */
    private MapRenderer mapRenderer;
    /**
     * Camera
     */
    private Camera camera;
    /**
     * Viewport
     */
    private Viewport viewport;
    /**
     * Platform interface for platform specific stuff
     */
    private Platform platform;

    /**
     * Latest position from the device
     */
    private GeoPoint latestPoint;
    /**
     * Current point
     */
    private GeoPoint currentPoint;
    /**
     * Info for rendering the map
     */
    private int mapScale = 7, mapRotation = 0;
    /**
     * Counts the distance walked in meters
     */
    private double distanceWalked = 0d;
    /**
     * Last position where walking distance was measured
     */
    private GeoPoint lastWalkPos;

    /**
     * Money, temporary, just for testing
     */
    private long money = 0L;
    /**
     * List of attractions from the server
     */
    private List<Attraction> attractions = new ArrayList<Attraction>();

    /**
     * Scale of things
     */
    private float imgScale = 1, textScale = 1.5f;
    /**
     * (Confusing) The radius of the circle around the player
     */
    private float pulsingAnimation = 0;

    /**
     * UUID of the player
     */
    private String uuid = UUID.randomUUID().toString();
    /**
     * Online players
     */
    private HashMap<String, Player> onlinePlayers = new HashMap<String, Player>();
    /**
     * Just some flags
     */
    private boolean connected = false, runThread = false, packetDisconnect = false;
    /**
     * Status of the connection
     */
    private String serverStatus = "Not connected";
    /**
     * KryoNet client
     */
    private Client client;
    /**
     * Last position sent to the server
     */
    private GeoPoint lastPointSent;
    /**
     * Just for debug
     */
    private int themeIndex = 0;

    /**
     * Not used
     */
    private int wWidth = 1280, wHeight = 720;
    /**
     * Device DPI as a String
     */
    private String density = "NODPI";

    /**
     * PacketQueue, It needs cleanup
     */
    private List<Packet> packetQueue = new ArrayList<Packet>();

    /**
     * Renders the compass
     */
    private CompassRenderer compassRenderer;
    /**
     * Handles text rendering
     */
    private TextUtils textUtils;

    /**
     * Location info
     */
    private String country = "NoCountry", county = "NoCounty";

    public PML(Platform platform) {
        this.platform = platform;
    }

    /**
     * Adjusts the scale of things, so it looks good on all devices.
     */
    private void adjustGraphicsScale() {
        float width = Gdx.graphics.getWidth();
        if(width <= 540) {
            // LDPI
            textScale = .75f;
            imgScale = .75f;
            mapScale = 6;
            density = "LDPI";
        } else if(width <= 720) {
            // MDPI (Baseline)
            textScale = 1f;
            imgScale = 1f;
            mapScale = 7;
            density = "MDPI";
        } else if(width <= 1080) {
            // HDPI
            textScale = 1.75f;
            imgScale = 1.75f;
            mapScale = 8;
            density = "HDPI";
        } else {
            // XHDPI
            textScale = 2.5f;
            imgScale = 2.5f;
            mapScale = 8;
            density = "XHDPI";
        }
    }

    /**
     * Sets up initial variables, and connects to the server
     */
    @Override
    public void create() {
        // Debug Code
        platform.updateCache(55.757202, 37.620117);
        // Debug Code

        Gdx.graphics.setContinuousRendering(false);
        batch = new SpriteBatch();
        textUtils = new TextUtils(batch);
        img = new Texture("badlogic.jpg");
        attractionTexture = new Texture("attraction.png");
        map = new MapAdapter();
        mapRenderer = new MapRenderer(map);
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        shapeRenderer = new ShapeRenderer();

        mapRenderer.onSurfaceCreated();
        mapRenderer.onSurfaceChanged(w, h);
        map.viewport().setScreenSize(w, h);

        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);

        compassRenderer = new CompassRenderer(5, 5);

        Gdx.graphics.requestRendering();

        TileSource tileSource = new OSciMap4TileSource();
        initDefaultLayers(tileSource, false, true, false, Gdx.graphics.getDensity());

        // Gets the latest position from the device
        latestPoint = platform.getPos();
        currentPoint = latestPoint;
        lastWalkPos = currentPoint;
        lastPointSent = currentPoint;
        moveMap(currentPoint.getLatitude(), currentPoint.getLongitude());

        // Gets the country and the county of the player
        OkHttpClient okHttpClient = new OkHttpClient();
        String requestUrl = String.format(
                "http://nominatim.openstreetmap.org/reverse?format=json&lat=%s&lon=%s&zoom=9&addressdetails=1&accept-language=en",
                String.valueOf(currentPoint.getLatitude()),
                String.valueOf(currentPoint.getLongitude()));
        Request request = new Request.Builder()
                .url(requestUrl)
                .addHeader("User-Agent", "PML/1.0")
                .build();
        JsonValue root = null;
        try {
            Response response = okHttpClient.newCall(request).execute();
            root = new JsonReader().parse(response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (root != null) {
                if (!root.has("address")) {
                    System.out.println(" doesn't have address information, skipping...");
                    throw new Exception();
                }
                JsonValue address = root.get("address");
                if (!address.has("country_code")) {
                    System.out.println(" doesn't have country information, skipping...");
                    throw new Exception();
                }

                String country = address.getString("country_code");
                String county;
                if(!address.has("state")) {
                    System.out.println( " doesn't have state information, trying county...");
                    if(!address.has("county")) {
                        System.out.println(" doesn't have county information, trying skipping...");
                        throw new Exception();
                    }
                    county = address.getString("county");
                } else {
                    county = address.getString("state");
                }
                county = Normalizer.normalize(county, Normalizer.Form.NFD);
                county = county.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
                county = county.replaceAll("\\(", "");
                county = county.replaceAll("\\)", "");
                String[] countyParts = county.split(" ");
                county = "";
                for (String s : countyParts) {
                    char c = s.charAt(0);
                    s = String.valueOf(c).toUpperCase() + s.substring(1);
                    county += "-" + s;
                }
                county = county.substring(1);
                this.country = country;
                this.county = county;

                Gdx.app.log("Geocode", country + "/" + county);
            }
        } catch (Exception e) { e.printStackTrace(); }

        // Just some testing
        GeoPoint point2 = new GeoPoint(47.19993, 18.4369883);
        GeoPoint point1 = new GeoPoint(47.19999, 18.4369883);
        Gdx.app.log("Distance", distance(point1, point2) + " m, " + distanceInKilometres(point1, point2) + " km");
        adjustGraphicsScale();
        moveMap(map.getMapPosition().getGeoPoint().getLatitude(), map.getMapPosition().getGeoPoint().getLongitude());

        // Registers all packets
        client = new Client(3000, 65535);
        Kryo kryo = client.getKryo();
        kryo.register(Packet.class);
        kryo.register(C00PacketAuth.class);
        kryo.register(C01PacketDisconnect.class);
        kryo.register(C02PlayerDisconnected.class);
        kryo.register(C03PlayerConnected.class);
        kryo.register(C04PacketMove.class);
        kryo.register(C05PlayerMoved.class);
        kryo.register(C06KickPlayer.class);
        kryo.register(List.class);
        kryo.register(ArrayList.class);
        kryo.register(Player.class);
        kryo.register(C07AuthSuccess.class);
        kryo.register(C08StatsUpdate.class);
        kryo.register(Attraction.class);
        kryo.register(C09CollectAttraction.class);
        kryo.register(C10PacketTeleport.class);
        client.start();
        try {
            client.connect(5000, "localhost", 54555, 54666);
            serverStatus = "Authenticating";
            // Sets up an authentication packet
            C00PacketAuth packetAuth = new C00PacketAuth();
            packetAuth.uuid = uuid;
            String nanoTime = String.valueOf(System.nanoTime());
            nanoTime = nanoTime.substring(nanoTime.length() - 4, nanoTime.length());
            packetAuth.customUsername = "PML_Player" + nanoTime;
            packetAuth.initX = currentPoint.getLatitude();
            packetAuth.initY = currentPoint.getLongitude();
            packetAuth.protocolVersion = 1;
            packetAuth.country = this.country;
            packetAuth.county = this.county;
            client.sendTCP(packetAuth);
            connected = true;
            // If on desktop, allow the use of commands for debugging
            if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                Scanner scanner = new Scanner(System.in);
                                while (connected) {
                                    String command = scanner.nextLine();
                                    String[] commandParts = command.split(" ");
                                    String baseCommand = commandParts[0];
                                    String[] args = new String[commandParts.length - 1];
                                    if (args.length > 0) {
                                        System.arraycopy(commandParts, 1, args, 0, commandParts.length - 1);
                                    }
                                    if (baseCommand.equalsIgnoreCase("tp")) {
                                        if (args.length == 2) {
                                            String toPlayer = args[0];
                                            C10PacketTeleport packetTeleport = new C10PacketTeleport();
                                            if (args[1].equalsIgnoreCase("name")) {
                                                for (java.util.Map.Entry<String, Player> entry : onlinePlayers.entrySet()) {
                                                    Player player = entry.getValue();
                                                    if (!player.username.equalsIgnoreCase(toPlayer))
                                                        continue;
                                                    toPlayer = player.uuid;
                                                    break;
                                                }
                                            }
                                            packetTeleport.toUuid = toPlayer;
                                            client.sendTCP(packetTeleport);
                                        } else {
                                            System.out.println("Usage: tp [to] [name / uuid]");
                                        }
                                    } else if(baseCommand.equalsIgnoreCase("kick")) {
                                        if(args.length > 2) {
                                            String kickPlayer = args[0];
                                            String reason = "";
                                            for(int i = 2; i < args.length; i++) {
                                                reason += args[i] + " ";
                                            }
                                            if (args[1].equalsIgnoreCase("name")) {
                                                for (java.util.Map.Entry<String, Player> entry : onlinePlayers.entrySet()) {
                                                    Player player = entry.getValue();
                                                    if (!player.username.equalsIgnoreCase(kickPlayer))
                                                        continue;
                                                    kickPlayer = player.uuid;
                                                    break;
                                                }
                                            }
                                            C06KickPlayer packet = new C06KickPlayer();
                                            packet.reason = reason;
                                            packet.uuid = kickPlayer;
                                            client.sendTCP(packet);
                                        } else {
                                            System.out.println("Usage: kick [player] [name / uuid] [reason]");
                                        }
                                    }
                                    else {
                                        System.out.println("Invalid command " + args[0]);
                                    }
                                }
                            }
                        }
                ).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            serverStatus = "Failed to connect! " + e.getMessage();
        }
        client.addListener(new Listener() {
            @Override
            public void disconnected(Connection connection) {
                if(packetDisconnect) {
                    packetDisconnect = false;
                    return;
                }
                runThread = false;
                connected = false;
                serverStatus = "Disconnected by server.";
            }

            @Override
            public void received(Connection connection, Object o) {
                if(o instanceof C03PlayerConnected) {
                    // A player connected, add them to the list
                    C03PlayerConnected playerConnected = (C03PlayerConnected) o;
                    Player player = new Player(playerConnected.uuid, playerConnected.initX, playerConnected.initY);
                    player.username = playerConnected.username;
                    onlinePlayers.put(playerConnected.uuid, player);
                    System.out.println(playerConnected.uuid + " joined!");
                } else if(o instanceof C02PlayerDisconnected) {
                    // Remove a player from the online list
                    C02PlayerDisconnected playerDisconnected = (C02PlayerDisconnected) o;
                    onlinePlayers.remove(playerDisconnected.uuid);
                    System.out.println(playerDisconnected.uuid + " left!");
                } else if(o instanceof C01PacketDisconnect) {
                    // Sent by the server requesting a disconnect
                    packetDisconnect = true;
                    C01PacketDisconnect packetDisconnect = (C01PacketDisconnect) o;
                    System.out.println("Disconnected: " + packetDisconnect.reason);
                    serverStatus = "Disconnected: " + packetDisconnect.reason;
                    connected = false;
                    runThread = false;
                    client.stop();
                } else if(o instanceof C05PlayerMoved) {
                    // Received when a player moves
                    C05PlayerMoved playerMoved = (C05PlayerMoved) o;
                    Player player = onlinePlayers.get(playerMoved.uuid);
                    player.x = playerMoved.newX;
                    player.y = playerMoved.newY;
                } else if(o instanceof C07AuthSuccess) {
                    // Sent by the server when authentication was successful
                    C07AuthSuccess authSuccess = (C07AuthSuccess) o;
                    for(Player player : authSuccess.onlinePlayers) {
                        onlinePlayers.put(player.uuid, player);
                    }
                    attractions = authSuccess.attractions;
                    Gdx.app.log("AuthSuccess", "Got " + attractions.size() + " attractions");
                    connected = true;
                    runThread = true;
                    new Thread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    while (runThread) {
                                        // Sends position updates to the server
                                        if(distance(currentPoint, lastPointSent) > 1) {
                                            C04PacketMove packetMove = new C04PacketMove();
                                            packetMove.newX = currentPoint.getLatitude();
                                            packetMove.newY = currentPoint.getLongitude();
                                            packetQueue.add(packetMove);
                                            lastPointSent = currentPoint;
                                            try {
                                                Thread.sleep(25);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                    ).start();
                    new Thread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    // Counts the distance walked
                                    while(connected) {
                                        distanceWalked += distance(currentPoint, lastWalkPos);
                                        lastWalkPos = currentPoint;
                                        try {
                                            Thread.sleep(5000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                    ).start();
                } else if(o instanceof C08StatsUpdate) {
                    // Sent by the server when our stats change
                    C08StatsUpdate statsUpdate = (C08StatsUpdate) o;
                    money = statsUpdate.money;
                    System.out.println("Updated money to " + statsUpdate.money);
                } else if(o instanceof C04PacketMove) {
                    // Sent by the server, requesting us to move
                    // This is for debug, just like C10PacketTeleport
                    final C04PacketMove packetMove = (C04PacketMove) o;
                    platform.updateCache(packetMove.newX, packetMove.newY);
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            moveMap(packetMove.newX, packetMove.newY);
                        }
                    });
                }
            }
        });
        if(Gdx.app.getType() == Application.ApplicationType.Android)
            theme();
    }

    /**
     * Renders the game
     */
    @Override
    public void render() {
        // Clears the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Centers the camera
        camera.position.x = Gdx.graphics.getWidth() / 2;
        camera.position.y = Gdx.graphics.getHeight() / 2;
        camera.update();

        // If we're not connected, show the reason
        if(!connected) {
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            batch.setColor(1, 1, 1, 1);
            viewport.apply();
            textUtils.drawWrappedCenter(serverStatus, viewport.getWorldWidth() / 2, viewport.getWorldHeight() - 5, viewport.getWorldWidth() - 5, (int)(20 * textScale), Color.WHITE);
            batch.end();
            return;
        }

        // Get the position of the device
        latestPoint = platform.getPos();
        if(currentPoint == null || currentPoint.getLongitude() != latestPoint.getLongitude() || currentPoint.getLatitude() != latestPoint.getLatitude()) {
            if(jumpTooBig()) {
                Gdx.app.log("Map", "Jump too big, tried to move " + distance(latestPoint, currentPoint) + " metres, maximum is 30 metres.");
                currentPoint = latestPoint;
                moveMap(latestPoint.getLatitude(), latestPoint.getLongitude());
            } else {
                animateToNewPoint();
            }
        }

        platform.tick();
        // Just some debug code
        if(Gdx.input.isTouched()) {
            map.updateMap(true);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            theme();
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                wWidth += 10;
            } else {
                wWidth -= 10;
            }
            Gdx.graphics.setWindowedMode(wWidth, wHeight);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                wHeight += 10;
            } else {
                wHeight -= 10;
            }
            Gdx.graphics.setWindowedMode(wWidth, wHeight);
        }
        // Debug end

        // Render the VTM map
        Gdx.graphics.requestRendering();
        GLState.enableVertexArrays(-1, -1);
        mapRenderer.onDrawFrame();
        map.render();
        GLState.bindVertexBuffer(0);
        GLState.bindElementBuffer(0);

        Vector2 pos = toWorldPos(map);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        viewport.apply();

        if(attractions != null) {
            // Loops through the attractions sent by the server
            for (Attraction attraction : attractions) {
                GeoPoint atPoint = new GeoPoint(attraction.getLat(), attraction.getLon());
                // Checks if it is in a 500 meter radius of the player
                if (distance(atPoint, currentPoint) > (mapScale == 3 ? 1500 : 500)) continue;
                Vector2 atWorldPos = toWorldPos(atPoint, map);
                // Check if it is visible on the screen
                if(!camera.frustum.pointInFrustum(atWorldPos.x, atWorldPos.y, 0)) continue;
                textUtils.drawWrappedCenterBottomLeft(attraction.getName(), atWorldPos.x, atWorldPos.y + 25 * imgScale, viewport.getWorldWidth() / 2, (int)(18 * textScale), Color.BLACK);
                if (distance(currentPoint, atPoint) > 15 && !attraction.isCollected()) {
                    batch.setColor(.8f, .3f, .3f, 1);
                    textUtils.drawCenteredText("Túl messze van", atWorldPos.x, atWorldPos.y - 25 * imgScale, (int)(18 * textScale), Color.BLACK);
                } else {
                    if (attraction.isCollected()) {
                        batch.setColor(.3f, .8f, .3f, 1);
                        textUtils.drawCenteredText("Megszerezve", atWorldPos.x, atWorldPos.y - 25 * imgScale, (int)(18 * textScale), Color.BLACK);
                    } else {
                        batch.setColor(.9f, .9f, 0, 1);
                        textUtils.drawCenteredText(attraction.getReward() + " Ft Jutalom", atWorldPos.x, atWorldPos.y - 25, (int)(18 * textScale), Color.BLACK);
                    }
                }
                batch.draw(attractionTexture, atWorldPos.x - 20 * imgScale, atWorldPos.y - 20 * imgScale, 40 * imgScale, 40 * imgScale);
                batch.setColor(1, 1, 1, 1);
            }
            if (Gdx.input.justTouched()) {
                Vector2 tPos = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
                for (Attraction attraction : attractions) {
                    GeoPoint atPos = new GeoPoint(attraction.getLat(), attraction.getLon());
                    Vector2 atWorldPos = toWorldPos(atPos, map);
                    Rectangle bb = new Rectangle(atWorldPos.x - 20 * imgScale, atWorldPos.y - 20 * imgScale, 80 * imgScale, 80 * imgScale);
                    if (bb.contains(tPos) && distance(currentPoint, atPos) <= 15 && !attraction.isCollected()) {
                        // Sends a packet to the server, telling it that we collected something
                        C09CollectAttraction collectAttraction = new C09CollectAttraction();
                        collectAttraction.id = attraction.getId();
                        collectAttraction.country = this.country;
                        collectAttraction.county = this.county;
                        packetQueue.add(collectAttraction);
                        attraction.setCollected(true);
                        break;
                    }
                }
                // Some more debug code, used to zoom out the map
                Rectangle playerBb = new Rectangle(pos.x - 32 * imgScale, pos.y - 32 * imgScale, 64 * imgScale, 64 * imgScale);
                if(playerBb.contains(tPos)) {
                    if(mapScale == 3)
                        adjustGraphicsScale();
                    else
                        mapScale = 3;

                    moveMap(currentPoint.getLatitude(), currentPoint.getLongitude());
                }
            }
        }

        // Debug code, of coure this wouldn't be in a final game
        if(Gdx.input.justTouched()) {
            Vector2 touchPos = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            for(java.util.Map.Entry<String, Player> entry : onlinePlayers.entrySet()) {
                Player player = entry.getValue();
                GeoPoint playerPos = new GeoPoint(player.x, player.y);
                Vector2 worldPos = toWorldPos(playerPos, map);
                Rectangle playerBb = new Rectangle(worldPos.x - 32 * imgScale, worldPos.y - 32 * imgScale, 64 * imgScale, 64 * imgScale);
                if(playerBb.contains(touchPos)) {
                    C06KickPlayer kickPlayer = new C06KickPlayer();
                    kickPlayer.uuid = player.uuid;
                    kickPlayer.reason = "You have been kicked by another player";
                    client.sendTCP(kickPlayer);
                }
            }
            if(compassRenderer.getBb().contains(touchPos))
                mapRotation = 0;
        }

        // Renders all online players that are in a 130 meter radius of us
        Iterator<java.util.Map.Entry<String, Player>> iterator = onlinePlayers.entrySet().iterator();
        while (iterator.hasNext()){
            java.util.Map.Entry<String, Player> entry = iterator.next();
            Player player = entry.getValue();
            GeoPoint atPoint = new GeoPoint(player.x, player.y);
            if(distance(currentPoint, atPoint) > 130) continue;
            Vector2 worldPos = toWorldPos(atPoint, map);
            batch.draw(img, worldPos.x - 32 * imgScale, worldPos.y - 32 * imgScale, 64 * imgScale, 64 * imgScale);
            String playerName = (player.username != null && player.username.length() > 0 ? player.username : player.uuid);
            textUtils.drawWrappedCenterBottomLeft(playerName, worldPos.x, worldPos.y + 38 * imgScale, viewport.getWorldWidth() / 2, (int)(18 * textScale), Color.BLACK);
        }

        // Draws the player
        batch.draw(img, pos.x - 32 * imgScale, pos.y - 32 * imgScale, 64 * imgScale, 64 * imgScale);
        // Draws the HUD
        textUtils.drawText("Distance Walked: " + distanceWalked, 5, viewport.getWorldHeight() - 5, (int)(18 * textScale), Color.BLACK);
        String density = "Density: " + Gdx.graphics.getDensity() + " (" + this.density + ")";
        textUtils.drawTextRightBottomLeft(density, viewport.getWorldWidth() - 5, 5, (int)(18 * textScale), Color.BLACK);
        textUtils.drawTextRight("Pénz: " + money + " Ft", viewport.getWorldWidth() - 5, viewport.getWorldHeight() - 5, (int)(18 * textScale), Color.BLACK);
        // Renders the compass
        compassRenderer.render(batch);

        batch.end();

        // Draws the circle around the player
        Gdx.gl.glLineWidth(3);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        viewport.apply();
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.circle(pos.x, pos.y, pulsingAnimation);
        shapeRenderer.end();

        float mtp = metersToPixels(15, map.getMapPosition());
        float o = (64 * imgScale) / 2;
        pulsingAnimation += Gdx.graphics.getDeltaTime() * (mtp - o);
        if(pulsingAnimation > mtp)
            pulsingAnimation = o;

        if(Gdx.input.isKeyPressed(Input.Keys.M))
            money += 1;

        if(Gdx.input.isTouched()) {
            mapRotation += (Gdx.input.getDeltaX() + Gdx.input.getDeltaY()) / 2;
            map.viewport().setRotation(mapRotation);
            compassRenderer.setRotation(mapRotation);
        }

        List<Packet> toRemove = new ArrayList<Packet>();
        for(Packet packet : packetQueue) {
            client.sendTCP(packet);
            toRemove.add(packet);
        }
        packetQueue.removeAll(toRemove);
    }

    /**
     * Changes VTM's theme
     */
    private void theme() {
        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            map.setTheme(VtmThemes.values()[themeIndex]);
            themeIndex++;
            if(themeIndex > VtmThemes.values().length - 1)
                themeIndex = 0;
        } else {
            map.setTheme(new ThemeFile() {
                @Override
                public XmlRenderThemeMenuCallback getMenuCallback() {
                    return null;
                }

                @Override
                public String getRelativePathPrefix() {
                    return "";
                }

                @Override
                public InputStream getRenderThemeAsStream() throws IRenderTheme.ThemeException {
                    return Gdx.files.internal("testTheme.xml").read();
                }
            });
        }
        Gdx.app.log("Map", "Reloaded theme");
    }

    /**
     * Checks if the location change is too big to animate
     * @return true if the jump is bigger than 30 metres, false otherwise
     */
    private boolean jumpTooBig() {
        return distance(currentPoint, latestPoint) > 30;
    }

    /**
     * Returns the distance between two {@link org.oscim.core.GeoPoint}s in kilometres,
     * @param point1 The first {@link org.oscim.core.GeoPoint}
     * @param point2 The second {@link org.oscim.core.GeoPoint}
     * @return The distance in kilometres
     */
    private double distanceInKilometres(GeoPoint point1, GeoPoint point2) {
        return distance(point1, point2) / 1000;
    }

    /**
     * Converts pixels to meters
     * @param pixels The amount of pixels to convert
     * @param mapPosition The MapPosition
     * @return The amount of meters
     */
    private float pixelsToMeters(int pixels, MapPosition mapPosition) {
        float x = groundResolution(mapPosition);
        return pixels * x;
    }

    /**
     * Converts meters to pixels
     * @param meters The amount of meters to convert
     * @param mapPosition The MapPosition
     * @return The amount of pixels
     */
    private float metersToPixels(int meters, MapPosition mapPosition) {
        float x = groundResolution(mapPosition);
        return meters / x;
    }

    /**
     * Gets how many meters a pixel covers
     * @param mapPosition The MapPosition
     * @return The amount of meters per one pixel
     */
    private float groundResolution(MapPosition mapPosition) {
        double lat = 90 - 360 * Math.atan(Math.exp((mapPosition.y - 0.5) * (2 * Math.PI))) / Math.PI;
        return (float) (Math.cos(lat * (Math.PI / 180)) * 40075016.686 / (Tile.SIZE * mapPosition.scale));
    }

    /**
     * Returns the distance between two {@link org.oscim.core.GeoPoint}s in metres.
     * @param point1 The first {@link org.oscim.core.GeoPoint}
     * @param point2 The second {@link org.oscim.core.GeoPoint}
     * @return The distance in metres
     */
    private double distance(GeoPoint point1, GeoPoint point2) {
        final int R = 6371000;

        double phi1 = Math.toRadians(point1.getLatitude());
        double phi2 = Math.toRadians(point2.getLatitude());

        double deltaPhi = Math.toRadians(point2.getLatitude() - point1.getLatitude());
        double deltaLambda = Math.toRadians(point2.getLongitude() - point1.getLongitude());

        double a =      Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2) +
                        Math.cos(phi1) * Math.cos(phi2) *
                        Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    /**
     * Moves the map
     * @param lat Latitude
     * @param lon Longitude
     */
    private void moveMap(double lat, double lon) {
        map.setMapPosition(lat, lon, 2000 << mapScale);
        map.viewport().setRotation(mapRotation);
        compassRenderer.setRotation(mapRotation);
        map.updateMap(true);
    }

    /**
     * Animates the map to the latest point
     */
    private void animateToNewPoint() {
        double newLat = latestPoint.getLatitude();
        double newLon = latestPoint.getLongitude();
        double curLat = currentPoint.getLatitude();
        double curLon = currentPoint.getLongitude();
        if(newLat > curLat) curLat += 0.000003;
        if(newLat < curLat) curLat -= 0.000003;
        if(newLon > curLon) curLon += 0.000003;
        if(newLon < curLon) curLon -= 0.000003;
        currentPoint = new GeoPoint(curLat, curLon);
        moveMap(curLat, curLon);
        if(newLat == curLat && newLon == curLon)
            currentPoint = latestPoint;
    }

    /**
     * Converts a {@link org.oscim.core.GeoPoint} to a world coordinate
     * @param geoPoint The point to convert
     * @param map The map
     * @return A {@link com.badlogic.gdx.math.Vector2} containing world coordinates
     */
    private Vector2 toWorldPos(GeoPoint geoPoint, Map map) {
        Point point = new Point();
        map.viewport().toScreenPoint(geoPoint, false, point);
        return viewport.unproject(new Vector2((int)point.x, (int)point.y));
    }

    /**
     * Converts the current map position to world coorinates
     * @param map The map
     * @return A {@link com.badlogic.gdx.math.Vector2} containing world coordinates
     */
    private Vector2 toWorldPos(Map map) {
        Point point = new Point();
        GeoPoint currentPoint = map.getMapPosition().getGeoPoint();
        map.viewport().toScreenPoint(currentPoint, false, point);
        return viewport.unproject(new Vector2((int)point.x, (int)point.y));
    }

    /**
     * Inits default layers
     * @param tileSource The tile source for the map
     * @param tileGrid Whether or not to show the tile grid
     * @param labels Whether or not to show labels
     * @param buildings Whether or not draw 3D buildings
     * @param scale Device DPI
     */
    private void initDefaultLayers(TileSource tileSource, boolean tileGrid, boolean labels,
                                     boolean buildings, float scale) {
        Layers layers = map.layers();

        if (tileSource != null) {
            VectorTileLayer mapLayer = map.setBaseMap(tileSource);
            map.setTheme(VtmThemes.DEFAULT);

            if (buildings)
                layers.add(new BuildingLayer(map, mapLayer));

            if (labels)
                layers.add(new LabelLayer(map, mapLayer));
        }

        if (tileGrid)
            layers.add(new TileGridLayer(map, scale));
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        map.viewport().setScreenSize(width, height);
        mapRenderer.onSurfaceChanged(width, height);
        adjustGraphicsScale();
        compassRenderer.adjustSize();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }

    /**
     * Renders a compass
     * @author glorantq
     */
    private class CompassRenderer {
        private Texture frame, direction;
        private int rotation = 0;
        private float x = 0, y = 0;

        private float frameWidth, frameHeight, dirWidth, dirHeight;

        private Rectangle bb;

        /**
         * Creates a new compass
         * @param x The x position of the compass
         * @param y The y position of the compass
         */
        public CompassRenderer(float x, float y) {
            this.x = x;
            this.y = y;
            this.frame = new Texture("compassFrame.png");
            this.direction = new Texture("compassDirection.png");
            adjustSize();
        }

        /**
         * Renders the compass
         * @param batch The {@link com.badlogic.gdx.graphics.g2d.SpriteBatch} to draw with
         */
        public void render(SpriteBatch batch) {
            batch.draw(frame, x, y, frameWidth, frameHeight);
            batch.draw(direction,
                    x + frameWidth / 2 - dirWidth / 2,
                    y + frameHeight / 2 - dirHeight / 2,
                    dirWidth / 2, dirHeight / 2,
                    dirWidth,
                    dirHeight,
                    1, 1, rotation, 0, 0,
                    direction.getWidth(), direction.getHeight(),
                    false, false);
        }

        /**
         * The bounding box of the compass
         * @return The bounding box of the compass
         */
        public Rectangle getBb() {
            return this.bb;
        }

        /**
         * Sets the rotation of the compass
         * @param degrees The rotation of the compass
         */
        public void setRotation(int degrees) {
            this.rotation = degrees;
        }

        /**
         * Returns the width of the compass
         * @return The width of the compass
         */
        public float getWidth() {
            return this.frameWidth;
        }

        /**
         * Returns the height of the compass
         * @return The height of the compass
         */
        public float getHeight() {
            return this.frameHeight;
        }

        /**
         * Adjusts the size of the compass
         */
        public void adjustSize() {
            this.frameWidth = frame.getWidth() * imgScale;
            this.frameHeight = frame.getHeight() * imgScale;
            this.dirWidth = direction.getWidth() * imgScale;
            this.dirHeight = direction.getHeight() * imgScale;
            this.bb = new Rectangle(x, y, frameWidth, frameHeight);
        }
    }

    /**
     * Map class
     * @author glorantq
     */
    private class MapAdapter extends Map {

        @Override
        public int getWidth() {
            return Gdx.graphics.getWidth();
        }

        @Override
        public int getHeight() {
            return Gdx.graphics.getHeight();
        }

        private final Runnable mRedrawCb = new Runnable() {
            @Override
            public void run() {
                prepareFrame();
                Gdx.graphics.requestRendering();
            }
        };

        @Override
        public void updateMap(boolean forceRender) {
            synchronized (mRedrawCb) {
                Gdx.app.postRunnable(mRedrawCb);
            }
        }

        @Override
        public void render() {
            synchronized (mRedrawCb) {
                if (mClearMap)
                    updateMap(false);
                else {
                    Gdx.graphics.requestRendering();
                }
            }
        }

        @Override
        public boolean post(Runnable runnable) {
            Gdx.app.postRunnable(runnable);
            return true;
        }

        @Override
        public boolean postDelayed(final Runnable action, long delay) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    action.run();
                }
            }, delay / 1000f);
            return true;
        }

        @Override
        public void beginFrame() {
        }

        @Override
        public void doneFrame(boolean animate) {
            synchronized (mRedrawCb) {
                updateMap(true);
            }
        }
    }
}
