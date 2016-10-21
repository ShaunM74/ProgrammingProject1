package group4.programmingproject1;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.widget.Toast;

import org.dyndns.ecall.ecalldataapi.EcallRegister;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

import static android.R.attr.button;

//testing code
//import group4.programmingproject1.dataHandler;

public class DevModeActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Button testRegister;
    private Button testContact;
    private Button clearTextButton;
    private TextView TextBox;
    Context context;
    private static final int CONTACT_PICKER_RESULT = 1001;
    private static final int myPickerResult = 12376;

    //test code for spinner data being recovered from datahandler
    //TextView test text;
    //Spinner SpinnerVidSnd;

    //Google fused location test
    GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_mode);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(myToolbar);
        // Try to set icon, if not found, leave blank

        if(getSupportActionBar() != null)
            getSupportActionBar().setIcon(R.drawable.appiconsmall);

        testRegister = (Button)findViewById(R.id.testregisterButton);
        testContact = (Button)findViewById(R.id.testContactButton);
        clearTextButton = (Button)findViewById(R.id.clearTextButton);
        TextBox = (TextView)findViewById(R.id.returnTextView);
        context=this;

        // On touch listeners to report button touches
        // Using touch listeners to capture finger raised events.
        clearTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v)
            {
                TextBox.setText("");
            }
        });

        testContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
 /*               Intent i = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI);
                //i.setType(ContactsContract.CommonDataKinds.Email.CONTEN‌​T_TYPE);
                startActivityForResult(i, CONTACT_PICKER_RESULT);*/
                Intent i = new Intent(context, ContactPickerActivity.class);
                startActivityForResult(i,myPickerResult);
                //startActivity(i);
            }

        });

        testRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                new registerDevice().execute();
            }

        });

        //###############################################
        //test values  for checking the dataHandler class
        //###############################################
        dataHandler data1 = new dataHandler();
        //this code tests if the get and set video time works
        /*
        TextView testtext = (TextView) findViewById(R.id.testSpinner);
        data1.setRecordTime(4,getApplicationContext(),getString(R.string.OptSettingsFile));
        testtext.setText( String.valueOf(data1.getRecordTime(getApplicationContext(),getString(R.string.OptSettingsFile),getString(R.string.SoundVideoRecordTime))));
        */
        //this code tests actual data values
        TextView testtext = (TextView) findViewById(R.id.testSpinner);
        TextView testtext2 = (TextView) findViewById(R.id.dataHLongitest);
        TextView testtext3 = (TextView) findViewById(R.id.GPSTIME);
        //data1.setRecordTimeActualBySecondsValue(4,getApplicationContext(),getString(R.string.OptSettingsFile));
        //testtext.setText( String.valueOf(data1.getRecordTimeActualSecondsValue(getApplicationContext(),getString(R.string.OptSettingsFile),getString(R.string.SoundVideoRecordTime))));

        //###############################################
        // Google Fused Location API
        //###############################################
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

       //data1.saveGPS(getApplicationContext(),getString(R.string.GPSLat), getString(R.string.GPSLONG),getString(R.string.OptSettingsFile),String.valueOf(mLastLocation.getLatitude()),String.valueOf(mLastLocation.getLongitude()));
        //data1.saveGPS(getApplicationContext(),getString(R.string.GPSLat), getString(R.string.GPSLONG),getString(R.string.OptSettingsFile),"Latitudetest","longitudetest");
        dataHandler.GPSobject gps;
        gps = data1.getGPS(getApplicationContext(),getString(R.string.GPSLat),getString(R.string.GPSLONG),getString(R.string.GPStime),getString(R.string.OptSettingsFile));

        testtext.setText(gps.getLatitude());
        testtext2.setText(gps.getLongitude());
        testtext3.setText(gps.getGPSTime());


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    // handle contact results

                    Cursor cursor = null;

                    try {
                        Uri result = data.getData();
                        String id = result.getLastPathSegment();

                        //Get Name
                        cursor = getContentResolver().query(result, null, null, null, null);
                        if (cursor.moveToFirst()) {
                            TextBox.append(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)) + "\n");
                            TextBox.append(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)) + "\n");
                            TextBox.append(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + "\n");
                            TextBox.append(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)) + "\n");

                        }
                        cursor.close();

                        } catch (Exception e) { }
                    break;
                case myPickerResult:
                {
                    TextBox.append(data.getStringExtra("name")+"\n");
                    TextBox.append(data.getStringExtra("email")+"\n");
                    TextBox.append(data.getStringExtra("phone")+"\n");
                }
            }

        } else {
            // gracefully handle failure

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        mLatitudeText = (TextView) findViewById(R.id.latitudedevtext);
        mLongitudeText= (TextView) findViewById(R.id.longitudedevtext);

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null)
        {
            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    class registerDevice extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... voids) {
            String tempString ="";
            tempString = EcallRegister.registerDevice();
            return tempString;
        }


        @Override
        protected void onPostExecute(String results) {
            TextBox.append(results.toString());

        }
    }
    //Google API Fused Location code test
    protected void onStart()
    {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop()
    {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


}
