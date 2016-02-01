package net.disastercapp.mobiledisaster;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ServiceConfigurationError;

public class MainActivity extends AppCompatActivity {

    Location location;
    LocationService locationService;
    boolean locationServiceConnected = false;
    private ServiceConnection locationServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                locationServiceConnected = true;
                LocationService.LocalBinder localBinder = (LocationService.LocalBinder) service;
                locationService = localBinder.getService();
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                locationServiceConnected = false;
            }
        };
        connectLocationService();
    }

    // connects and binds to instance of LocationService.class
    private void connectLocationService(){
        if (!locationServiceConnected) {
            Intent locationServiceIntent = new Intent(this, LocationService.class);
            bindService(locationServiceIntent,locationServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private void setText(TextView textView, String text){

    }

    public void setAsyncLocation(View view){
        if (!locationServiceConnected){
            connectLocationService();
        }
        setName(view,"setAsync");
        //locationService.findLocation();
    }

    public void updateLocation(View view){
        if (locationServiceConnected && locationService.ready() && false ){
            location = locationService.getLocation();
            TextView latitudeTextView = (TextView)findViewById(R.id.latitudeTextView);
            TextView longitudeTextView = (TextView)findViewById(R.id.longitudeTextView);


            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

                latitudeTextView.setText(Double.toString(latitude));
                longitudeTextView.setText(Double.toString(longitude));

                setName(view, "NULL");

        }else{
            setName(view,"emulator");
        }

    }

    public void setName(View view, String s){
        TextView testView = (TextView)findViewById(R.id.areaName);
        testView.setText(s);
    }

}
