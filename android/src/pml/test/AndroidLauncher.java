package pml.test;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.SharedLibraryLoader;

import org.oscim.android.canvas.AndroidGraphics;
import org.oscim.android.gl.AndroidGL;
import org.oscim.backend.GLAdapter;
import org.oscim.core.GeoPoint;
import org.oscim.core.Tile;

public class AndroidLauncher extends AndroidApplication implements Platform {
	private GeoPoint geoPoint = new GeoPoint(47.1997685, 18.43898893);
	protected LocationManager mLocationManager;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidGraphics.init();
		GdxAssets.init("");
		GLAdapter.init(new AndroidGL());
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		Tile.SIZE = Tile.calculateTileSize(metrics.scaledDensity);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useGyroscope = false;
		config.stencil = 8;
		config.numSamples = 2;
		new SharedLibraryLoader().load("vtm-jni");
		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
		if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0.1f, mLocationListener);
			Location lastLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			geoPoint = new GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude());
			Toast.makeText(this, "Registered for location updates, and queried last position. (" + lastLocation.getLatitude() + ", " + lastLocation.getLongitude() + ")", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "Failed to get GPS permission. Please restart your game.", Toast.LENGTH_LONG).show();
		}
		initialize(new PML(this), config);
	}

	@Override
	public GeoPoint getPos() {
		return geoPoint;
	}

	@Override
	public void tick() {

	}

	private final LocationListener mLocationListener = new LocationListener() {
		@Override
		public void onStatusChanged(String provider, final int status, Bundle extras) {
			runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(thisContext(), "LocationListener status changed. Status: " + status, Toast.LENGTH_SHORT).show();
                }
            });
		}

		@Override
		public void onProviderEnabled(String provider) {
            Toast.makeText(thisContext(), "LocationListener enabled.", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onProviderDisabled(String provider) {
            Toast.makeText(thisContext(), "LocationListener disabled.", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onLocationChanged(final Location location) {
            Toast.makeText(thisContext(), "LocationUpdate: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
			geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
		}
	};

    private Context thisContext() {
        return this;
    }
}
