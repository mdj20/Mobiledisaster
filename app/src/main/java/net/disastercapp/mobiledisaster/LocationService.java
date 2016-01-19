
package net.disastercapp.mobiledisaster;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

/* Bound service that will run in the primary thread. Gets the users location via location manager.
 */

public class LocationService extends Service {

    private final IBinder lBinder = new LocalBinder(); // binder for bound tasks
    private LocationManager locationManager;
    private LocationProvider locationProvider;

    public LocationService() {
        super();
    }

    @Override
    public void onCreate(){
        Context context = getApplicationContext();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        locationProvider = locationManager.getProvider(locationManager.getBestProvider(criteria, false));
    }

    // will attempt to get the location from the LocationManager
    public Location getLocation(){
        Location location = null;
        try{
            location = locationManager.getLastKnownLocation(locationProvider.getName());
        }
        catch (SecurityException se){  // if security permission is not set, it will just display a message to user
            Context context = getApplicationContext();
            Toast errorToast = Toast.makeText(context,"Bad Location Permissions", Toast.LENGTH_SHORT);
            errorToast.show();
        }
        return location;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return lBinder;
    }

    // binder class
    public class LocalBinder extends Binder {
        LocationService getService(){        // method will return LocationServie as an object.
            return LocationService.this;
        }
    }
}
