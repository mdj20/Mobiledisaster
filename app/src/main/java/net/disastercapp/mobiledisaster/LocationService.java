
package net.disastercapp.mobiledisaster;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

/* Bound service that will run in the primary thread. Gets the users location via location manager.
 */
public class LocationService extends Service {

    private final IBinder localBinder = new LocalBinder(); // binder for bound tasks
    private LocationManager locationManager;
    private LocationProvider locationProvider;
    protected boolean ready = false;
    protected Location location = null;
    private boolean isBound = false;

    public LocationService() {
        super();
    }

    @Override
    public void onCreate(){
        Context context = getApplicationContext();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);  // prepares locationManager
    }

    @Override
    public IBinder onBind(Intent intent) { // returns LocalS
        isBound = true;
        return localBinder;
    }

    @Override
    public boolean onUnbind(Intent intent){
        isBound = false;
        return  isBound;
    }

    // Simple binder class
    public class LocalBinder extends Binder {
        LocationService getService(){        // method will return LocationServie as an object.
            return LocationService.this;
        }  // get service returns
    }

    // will create an AsyncTask that will get the
    public void findLocation(){
        new LocationAsyncTask().execute();
    }

    public boolean ready(){ return ready;}

    //returns location if ready, else returns null
    public Location getLocation(){
        if (ready) {return location;}
        else {return null;}
    }

    private class LocationAsyncTask extends AsyncTask<String,Boolean,Location> {

        protected Location doInBackground(String[] args){
            System.out.println("This");
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            locationProvider = locationManager.getProvider(locationManager.getBestProvider(criteria, true));

            try{
                location = locationManager.getLastKnownLocation(locationProvider.getName());
            }
            catch (SecurityException se){  // if security permission is not set, it will just display a message to user
                se.printStackTrace();
                Context context = getApplicationContext();
                Toast errorToast = Toast.makeText(context,"Bad Location Permissions", Toast.LENGTH_SHORT);
                errorToast.show();
            }
            System.out.println(location.getLatitude());

            return location;
        }

        protected void onProgressUpdate(Boolean b){
            // not called or implemented
        }

        protected void onPostExecute(Location result){

            if (result!=null) {
                LocationService.this.location = result;
                ready = true;
            }
        }
    }

}
