package pml.test;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import org.oscim.backend.GLAdapter;
import org.oscim.core.GeoPoint;
import org.oscim.core.Tile;
import java.util.List;

public class AndroidLauncher extends AndroidApplication implements Platform {
    private GeoPoint geoPoint;
    protected LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidGraphics.init();
        GdxAssets.init("");
        GLAdapter.init(new GdxGL());
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Tile.SIZE = Tile.calculateTileSize(metrics.scaledDensity);
        final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useGyroscope = false;
        config.stencil = 8;
        config.numSamples = 2;
        config.useImmersiveMode = true;
        new SharedLibraryLoader().load("vtm-jni");
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationListener mLocationListener = new LocationListener() {
                @Override
                public void onStatusChanged(String provider, final int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }

                @Override
                public void onLocationChanged(final Location location) {
                    geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                }
            };
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, .5f, mLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, .5f, mLocationListener);
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            List<String> providers = mLocationManager.getProviders(true);
            Location bestLocation = null;
            for (String provider : providers) {
                Location l = mLocationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    bestLocation = l;
                }
            }
            Log.i("Geo", (bestLocation == null) + "");
            geoPoint = new GeoPoint(bestLocation.getLatitude(), bestLocation.getLongitude());
            Toast.makeText(this, "Registered for location updates, and queried last position. (" + bestLocation.getLatitude() + ", " + bestLocation.getLongitude() + ")", Toast.LENGTH_LONG).show();
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

    private Context thisContext() {
        return this;
    }

    @Override
    public void updateCache(double lat, double lon) {

    }
}
