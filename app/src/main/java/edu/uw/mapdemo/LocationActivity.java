package edu.uw.mapdemo;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.text.DateFormat;
import java.util.Date;

public class LocationActivity extends AppCompatActivity implements  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private static final String TAG = "** LOC DEMO **";

    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(mGoogleApiClient == null){
            mGoogleApiClient =
                    new GoogleApiClient.Builder(this)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                            .addApi(LocationServices.API) //use location
                            .build(); //build me the client
        }
    }

    @Override
    protected void onStart(){
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop(){
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    /** Helper method for getting location **/
    public void getLocation(View v){
        //TODO: Fill me in!
        if(mGoogleApiClient != null){
            //FLA refers to gathering ways of getting location (i.e. GPS, Wi-Fi)
            //we get the last location from the gathered methods of getting location
            Location loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (loc != null) {
                //we can get the Lat and Lng
                ((TextView)findViewById(R.id.txt_lat)).setText(""+loc.getLatitude());
                ((TextView)findViewById(R.id.txt_lng)).setText("" + loc.getLongitude());
            } else {
                Log.v(TAG, "Last location is null");
            }

        }

    }



    /**
     * Menus for user interaction
     **/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_get_loc:
                Log.v(TAG, "Map menu item");
                getLocation(null);
                return true;
            case R.id.menu_map:
                Log.v(TAG, "Map menu item");

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        //when API has connected
        getLocation(null);

        //initialize a location request and set the interval for update
        LocationRequest request = new LocationRequest();
        request.setInterval(10000); //update every 10 secs
        request.setFastestInterval(5000); //if the device is update the location faster than what we set
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //what is our priority for the request (accuracy vs. battery saving)

        //using 3 params:
        // GoogleApiClient,
        // Location Request (how we want to access the info),
        // Location Listener (listens to when the
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        ((TextView)findViewById(R.id.txt_lat)).setText("" + location.getLatitude());
        ((TextView)findViewById(R.id.txt_lng)).setText("" + location.getLongitude());
    }
}