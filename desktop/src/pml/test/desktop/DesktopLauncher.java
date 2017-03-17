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
import org.oscim.gdx.LwjglGL20;
import org.oscim.utils.FastMath;

import pml.test.GdxAssets;
import pml.test.PML;
import pml.test.Platform;

public class DesktopLauncher implements Platform {
	private double lat = 47.2001152;
	private double lon = 18.4388147;

	public static void main (String[] arg) {
		new SharedLibraryLoader().load("vtm-jni");
		AwtGraphics.init();
		GdxAssets.init("assets/");
		GLAdapter.init(new LwjglGL20());
		Tile.SIZE = FastMath.clamp(Tile.SIZE, 128, 512);
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int)(720 / 1.3f);
		config.height = (int)(1280 / 1.3f);
		config.stencil = 8;
		config.samples = 2;
		new LwjglApplication(new PML(new DesktopLauncher()), config);
	}

	@Override
	public GeoPoint getPos() {
		return new GeoPoint(lat, lon);
	}

	@Override
	public void tick() {
		if(Gdx.input.isKeyJustPressed(Input.Keys.W))
			lat += 0.001;
		if(Gdx.input.isKeyJustPressed(Input.Keys.S))
			lat -= 0.001;
		if(Gdx.input.isKeyJustPressed(Input.Keys.D))
			lon += 0.001;
		if(Gdx.input.isKeyJustPressed(Input.Keys.A))
			lon -= 0.001;
	}
}
