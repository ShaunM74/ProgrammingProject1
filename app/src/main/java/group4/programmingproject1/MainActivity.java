package group4.programmingproject1;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
//import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.dyndns.ecall.ecalldataapi.EcallRegister;
import org.dyndns.ecall.ecalldataapi.EcallRegistration;
import org.w3c.dom.Text;

import java.io.IOException;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static group4.programmingproject1.R.id.textView;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private ImageButton alertButton = null;

    private ImageButton cancelButton = null;

    private Vibrator vibrator;


    //GPS
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView textView ;
    private String Longitude = null;
    private String Latitude = null;
    private int slowCheck = 10000;
    private int shortCheck = 1000;
    //private Date GPSdate = null;

    // GPS saving handler
    private int GPSsaveRate = 600000;
    private Handler gpsHandler;


    //sound rec
    public static final int RECORD_AUDIO = 0;

    private Date lastAlertDate =null;
    Intent alertServiceIntent;
    AlertService alertService;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        Toolbar myToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(myToolbar);
        // Try to set icon, if not found, leave blank

            if(getSupportActionBar() != null)
            getSupportActionBar().setIcon(R.drawable.appiconsmall);

        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        LocalBroadcastManager.getInstance(this).registerReceiver(noContactBroadcastReceiver,
                new IntentFilter(getString(R.string.no_contact_error)));


        // Assign buttons to variables

        alertButton = (ImageButton)findViewById(R.id.alertButton);
        cancelButton = (ImageButton)findViewById(R.id.cancelButton);
        Log.d("Debug","CertID:"+dataHandler.getCertID(this));
        if(dataHandler.getCertID(this)==null)
        {
            RegisterDevice registerDevice = new RegisterDevice();
            registerDevice.execute();
        }
        //Permission for sending SMS request
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.SEND_SMS))
            {

            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]
                        {
                                Manifest.permission.SEND_SMS
                        },1);
            }
        }

        //Permission for mic recording
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    this.RECORD_AUDIO);

        }
        else
        {
           // Toast.makeText(MainActivity.this, "rec perm given", Toast.LENGTH_LONG).show();
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},1);
        }
        else
        {
            // Toast.makeText(MainActivity.this, "call perm given", Toast.LENGTH_LONG).show();
        }



        // On touch listeners to report button touches
        // Using touch listeners to capture finger raised events.
        alertButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch (View v, MotionEvent event)
            {
                return onAlertTouch(v,event);
            }
        });

        //GPS stuff here

        //test text on button
        textView = (TextView) findViewById(R.id.gpstesttext);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location) {
                //test text
                //textView.append("\n " + location.getLatitude() + " " + location.getLongitude());
                //** this is for the test text display over button, comment out to remove
                //textView.setText(location.getLatitude() + " " + location.getLongitude());
                //textView.setText("Lat:"+Latitude + " " + "Lon:"+Longitude);
                //**
                Longitude = String.valueOf(location.getLongitude());
                Latitude  = String.valueOf(location.getLatitude());


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            //checks if the user has disabled gps and takes to screen to enable
            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }

        };
        //configure button stuff call maybe here****************
        startGPS();


        // AlertService/////////////////////////////////////////////////////////////
        alertServiceIntent = new Intent(context,AlertService.class);
        startService(alertServiceIntent);


        //GPS saving
        gpsHandler = new Handler();
        startRepeatingGPSTask();
    }

    Runnable GPSStatusSaver = new Runnable() {
        @Override
        public void run()
        {
            try
            {
                //updateStatus();
                saveGPSNow();
                //Toast.makeText(MainActivity.this, "GPS SAVED by Handler", Toast.LENGTH_LONG).show();
            }
            finally
            {
                gpsHandler.postDelayed(GPSStatusSaver,GPSsaveRate);
            }
        }
    };


    private void startRepeatingGPSTask()
    {
        GPSStatusSaver.run();
    }
    private void stopRepeatingGPSTask()
    {
        gpsHandler.removeCallbacks(GPSStatusSaver);
    }
/*
    @Override
    protected void onPause()
    {
        context.unregisterReceiver(noContactBroadcastReceiver);
        super.onPause();
    }

    @Override
    protect void onResume()
    {

    }
*/
    //Gets current screen orientation and sets it as requested screen orientation

    private void lockScreenRotation()
    {
        // Stop the screen orientation changing during an event
        switch (this.getResources().getConfiguration().orientation)
        {
            case Configuration.ORIENTATION_PORTRAIT:
                this.setRequestedOrientation(
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                this.setRequestedOrientation(
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }
    }

    // Sets requested orientation to be unspecified, which allows rotation

    private void unlockScreenRotation()
    {
        this.setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    // Handles touch events on Alert button

    public boolean onAlertTouch (View v, MotionEvent event) {
        Rect rect;
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            lockScreenRotation();
            alertButton.setImageResource(R.drawable.alertbuttonon);
            int beat1 = 350;
            int beat2 = 100;
            int small_gap = 150;
            int large_gap = 650;
            long[] heartbeat = {
                    0,
                    beat1, small_gap, beat2,
                    large_gap,
                    beat1, small_gap, beat2,
                    large_gap,
                    beat1, small_gap, beat2,
                    large_gap
            };

            vibrator.vibrate(heartbeat,0);
            startGPSTriggerHeld();

        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            saveGPSNow();
            // Check if event occured within the bounds of the button, if so do button action
            // Otherwise cancel/ignore the event.
            rect = new Rect(cancelButton.getLeft(), cancelButton.getTop(), cancelButton.getRight(), cancelButton.getBottom());
            if(rect.contains(v.getLeft()+(int)event.getX(),v.getTop()+(int)event.getY()))
            {
                Toast.makeText(MainActivity.this, "Alarm disarmed", Toast.LENGTH_LONG).show();
            }
            else
            {
                // Start of Alert processing, checking alert not fired within 30 seconds

                 Date alertDate = new java.util.Date();

                String alertID = new SimpleDateFormat("yyyyMMddHHmmss").format(alertDate)+"-"+
                        dataHandler.getDeviceID(context);
                if(lastAlertDate != null )
                {
                    long temp =(alertDate.getTime()-lastAlertDate.getTime());
                    if( temp<30000)
                    {

                        Toast.makeText(MainActivity.this, "Alert was already activated " +(temp/1000)+
                                " seconds ago", Toast.LENGTH_LONG).show();
                    }
                    else
                    {

                        Log.d("DEBUG","Doing another alert");

                        lastAlertDate = alertDate;
                        Intent intent = new Intent("startInstAlertAlarm");
                        intent.putExtra("alertID",alertID);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    }

                }
                else
                {
                    Log.d("DEBUG","Doing first alert");
                    lastAlertDate = alertDate;
                    Intent intent = new Intent("startInstAlertAlarm");
                    intent.putExtra("alertID",alertID);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

                }

            }
            vibrator.cancel();
            //GPS stop
            //locationManager.removeUpdates(locationListener);
            locationManager.requestLocationUpdates("gps", slowCheck, 0, locationListener);
            alertButton.setImageResource(R.drawable.alertbuttonoff);
            unlockScreenRotation();
            return true;
        }

        // Return false to indicate that touch event was not handled.
        return false;
    }

    private void doAbout()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.CustomDialogTheme);
        builder.setMessage(R.string.aboutText)
                .setTitle(R.string.app_name)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    // Handles menu choices.
    public boolean onOptionsItemSelected(MenuItem item) {
        final Context context=this;
        switch (item.getItemId()) {
            case R.id.about: {
                doAbout();
                return true;
            }
            case R.id.help: {
                Intent intent = new Intent (this, HelpActivity.class);
                startActivity (intent);
                return true;
            }
            case R.id.quit:
            {
                // Build Alert dialog to confirm quitting
                AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.CustomDialogTheme);
                builder.setMessage("Do you wish to close this application?")
                        .setTitle("Quit?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Shut down gps
                                locationManager.removeUpdates(locationListener);
                                stopRepeatingGPSTask();
                                Intent alertService = new Intent(context,AlertService.class);
                                //unbindService(alertServiceConnection);
                                stopService(alertService);
                                dialog.cancel();
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })                ;

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
            case R.id.settings:
            {
                Intent intent = new Intent (this, Settings.class);
                startActivity (intent);
                return true;
            }
            case R.id.devmenu:
            {
                Intent intent = new Intent(this, DevModeActivity.class);
                startActivity(intent);
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    //GPS STUFF
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startGPS();
                return;
        }
    }
    //this is the idle gps check function, checks every 30sec
    void startGPS(){
        // first check for permissions
        //if it doesnt have permission it hits return instead of continuing on to call the locationmanager and causing a crash
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        locationManager.requestLocationUpdates("gps", slowCheck, 0, locationListener);

    }
    // this is the active button held trigger, checks every 1 sec
    void startGPSTriggerHeld(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        locationManager.requestLocationUpdates("gps", shortCheck, 0, locationListener);
        saveGPSNow();
    }

    //FUNCTION TO SEND GPS VARIABLES longitude and latitude
    private void saveGPSNow()
    {
        dataHandler data1 = new dataHandler();
        if ( Latitude != null && Longitude != null )
        {
            //data1.saveGPS(getApplicationContext(), getString(R.string.GPSLat), getString(R.string.GPSLONG), getString(R.string.OptSettingsFile), String.valueOf(Latitude), String.valueOf(Longitude));
            data1.saveGPS(getApplicationContext(), getString(R.string.GPSLat), getString(R.string.GPSLONG),getString(R.string.GPStime), getString(R.string.OptSettingsFile), String.valueOf(Latitude), String.valueOf(Longitude),getTime());
            getAddress();
        }

    }

    private String getAddress()
    {


        List<Address> addresses = null;
        Geocoder geocoder;

        geocoder = new Geocoder(this,Locale.getDefault());
        String AllOurPowersCombined = null;

        try
        {
            addresses = geocoder.getFromLocation(Double.parseDouble(Latitude),Double.parseDouble(Longitude),1);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (NullPointerException n)
        {
            n.printStackTrace();
        }

        if (addresses != null) {
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();

            AllOurPowersCombined = address+" "+city+" at "+getTime();
        }


        if (AllOurPowersCombined != null)
        {
            dataHandler.saveAddress(context,AllOurPowersCombined);
            return AllOurPowersCombined;

        }
        else
        {
            return "Address Unknown";
        }
    }

    private String getTime()
    {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");

        String formattedDate = df.format(c.getTime());


        return formattedDate;
    }




    private BroadcastReceiver noContactBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.CustomDialogTheme);
            builder.setMessage(R.string.no_contact_alert)
                    .setTitle(R.string.app_name)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

        }
    };

    class RegisterDevice extends AsyncTask<Void, Void, String> {
        Context appContext;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            appContext = context;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String tempString ="";
            tempString = EcallRegister.registerDevice(appContext);
            return tempString;
        }


        @Override
        protected void onPostExecute(String results) {
            EcallRegistration ecallRegistration = new EcallRegistration(context,results);
            ecallRegistration.setCertificatePrivateKey();
        }
    }
}
