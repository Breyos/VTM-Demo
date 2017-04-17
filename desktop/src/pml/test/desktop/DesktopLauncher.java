package pml.test.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.SharedLibraryLoader;

import org.oscim.awt.AwtGraphics;
import org.oscim.backend.GLAdapter;
import org.oscim.core.GeoPoint;
import org.oscim.core.Tile;
import org.oscim.utils.FastMath;

import pml.test.GdxAssets;
import pml.test.GdxGL;
import pml.test.PML;
import pml.test.Platform;

public class DesktopLauncher implements Platform {
    private double lat = 47.191365;
    private double lon = 18.409809;

    public static void main(String[] arg) {
        new SharedLibraryLoader().load("vtm-jni");
        AwtGraphics.init();
        GdxAssets.init("assets/");
        GLAdapter.init(new GdxGL());
        Tile.SIZE = FastMath.clamp(Tile.SIZE, 128, 512);
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = (int) (720 / 1.3f);
        config.height = (int) (1280 / 1.3f);
        config.stencil = 16;
        config.samples = 2;
        config.resizable = true;
        config.allowSoftwareMode = true;
        new LwjglApplication(new PML(new DesktopLauncher()), config);
    }

    @Override
    public GeoPoint getPos() {
        return new GeoPoint(lat, lon);
    }

    @Override
    public void tick() {
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            lat += 0.00001;
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            lat -= 0.00001;
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            lon += 0.00001;
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            lon -= 0.00001;
    }

    @Override
    public void updateCache(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
}
