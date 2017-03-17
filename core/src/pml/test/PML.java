package pml.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.oscim.backend.AssetAdapter;
import org.oscim.backend.GL;
import org.oscim.core.GeoPoint;
import org.oscim.core.MapPosition;
import org.oscim.core.Point;
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
import org.oscim.tiling.source.mvt.MapboxTileSource;
import org.oscim.tiling.source.oscimap4.OSciMap4TileSource;

import java.io.InputStream;

import static org.oscim.backend.GLAdapter.gl;

public class PML extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    private Map map;
    private MapRenderer mapRenderer;
    private Camera camera;
    private Viewport viewport;
    private Platform platform;

    private GeoPoint latestPoint;
    private GeoPoint currentPoint;

    private boolean mRenderWait;
    private boolean mRenderRequest;
    private boolean mUpdateRequest;

    private int mapScale = 7;

    private FrameBuffer mapFbo;

    public PML(Platform platform) {
        this.platform = platform;
    }

    @Override
    public void create() {
        Gdx.graphics.setContinuousRendering(false);
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        map = new MapAdapter();
        mapRenderer = new MapRenderer(map);
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        map.viewport().setScreenSize(w, h);
        mapRenderer.onSurfaceCreated();
        mapRenderer.onSurfaceChanged(w, h);

        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);

        TileSource tileSource = new OSciMap4TileSource();
        initDefaultLayers(tileSource, false, true, false, 1 << 2);

        latestPoint = platform.getPos();
        currentPoint = latestPoint;
        map.setMapPosition(new MapPosition(currentPoint.getLatitude(), currentPoint.getLongitude(), 2000 << mapScale));
        Gdx.graphics.requestRendering();

        GeoPoint point2 = new GeoPoint(47.19993, 18.4369883);
        GeoPoint point1 = new GeoPoint(47.19999, 18.4369883);
        Gdx.app.log("Distance", distance(point1, point2) + " m, " + distanceInKilometres(point1, point2) + " km");

        mapFbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    @Override
    public void render() {
        camera.position.x = Gdx.graphics.getWidth() / 2;
        camera.position.y = Gdx.graphics.getHeight() / 2;
        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ?
                GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

        GLState.enableVertexArrays(-1, -1);

        gl.viewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gl.frontFace(GL.CW);

        try {
            mapRenderer.onDrawFrame();
        } catch (Exception e) {
            e.printStackTrace();
        }

        gl.flush();
        GLState.bindVertexBuffer(0);
        GLState.bindElementBuffer(0);
        gl.frontFace(GL.CCW);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        Vector2 pos = toWorldPos(map); // Converts current map position into screen coordinates
        batch.draw(img, pos.x - 32, pos.y - 32, 64, 64);
        batch.end();

        latestPoint = platform.getPos();
        if(currentPoint == null || currentPoint.getLongitude() != latestPoint.getLongitude() || currentPoint.getLatitude() != latestPoint.getLatitude()) {
            if(jumpTooBig()) { // Checks if the jump is bigger than 15 metres
                Gdx.app.log("Map", "Jump too big, tried to move " + distance(latestPoint, currentPoint) + " metres, maximum is 15 metres.");
                currentPoint = latestPoint;
                map.setMapPosition(latestPoint.getLatitude(), latestPoint.getLongitude(), 2000 << mapScale);
                map.updateMap(true);
            } else {
                animateToNewPoint();
            }
        }

        platform.tick();
        if(Gdx.input.isTouched()) {
            map.updateMap(true);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {
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
            Gdx.app.log("Map", "Reloaded theme");
        }
    }

    private boolean jumpTooBig() {
        return distance(currentPoint, latestPoint) > 30;
    }

    private double distanceInKilometres(GeoPoint point1, GeoPoint point2) {
        return distance(point1, point2) / 1000;
    }

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

    private void animateToNewPoint() {
        double newLat = latestPoint.getLatitude();
        double newLon = latestPoint.getLongitude();
        double curLat = currentPoint.getLatitude();
        double curLon = currentPoint.getLongitude();
        if(newLat > curLat) curLat += 0.00001;
        if(newLat < curLat) curLat -= 0.00001;
        if(newLon > curLon) curLon += 0.00001;
        if(newLon < curLon) curLon -= 0.00001;
        currentPoint = new GeoPoint(curLat, curLon);
        map.setMapPosition(curLat, curLon, 2000 << mapScale);
        map.updateMap(true);
        if(newLat == curLat && newLon == curLon) {
            currentPoint = latestPoint;
            Gdx.app.log("Map", "Animated to point");
        }
    }

    private Vector2 toWorldPos(GeoPoint geoPoint, Map map) {
        Point point = new Point();
        map.viewport().toScreenPoint(geoPoint, false, point);
        return new Vector2((int)point.x, (int)point.y);
    }

    private Vector2 toWorldPos(Map map) {
        Point point = new Point();
        GeoPoint currentPoint = map.getMapPosition().getGeoPoint();
        map.viewport().toScreenPoint(currentPoint, false, point);
        return new Vector2((int)point.x, (int)point.y);
    }

    protected void initDefaultLayers(TileSource tileSource, boolean tileGrid, boolean labels,
                                     boolean buildings, float scale) {
        Layers layers = map.layers();

        if (tileSource != null) {
            VectorTileLayer mapLayer = map.setBaseMap(tileSource);
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
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }

    class MapAdapter extends Map {

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
                if (!mRenderRequest) {
                    mRenderRequest = true;
                    Gdx.app.postRunnable(mRedrawCb);
                } else {
                    mRenderWait = true;
                }
            }
        }

        @Override
        public void render() {
            synchronized (mRedrawCb) {
                mRenderRequest = true;
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
                mRenderRequest = false;
                if (animate || mRenderWait) {
                    mRenderWait = false;
                    updateMap(true);
                }
            }
        }
    }
}
