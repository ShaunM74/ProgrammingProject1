package group4.programmingproject1;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
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

import org.dyndns.ecall.ecalldataapi.EcallAlert;
import org.dyndns.ecall.ecalldataapi.EcallContact;
import org.dyndns.ecall.ecalldataapi.EcallDataException;
import org.dyndns.ecall.ecalldataapi.EcallRegister;
import org.dyndns.ecall.ecallsendapi.EcallMessageDespatcher;
import org.dyndns.ecall.ecallsendapi.EcallMessageDespatcherViaSMS;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    private Handler alertHandler;

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

        context = getApplicationContext();
        alertHandler = new Handler() {
            @Override public void handleMessage(Message msg) {
                String mString=(String)msg.obj;
                Toast.makeText(context, mString, Toast.LENGTH_SHORT).show();
            }
        };


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
                doAlert();
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
        gps = data1.getGPS(getApplicationContext(),getString(R.string.GPSLat),getString(R.string.GPSLONG),getString(R.string.OptSettingsFile));

        testtext.setText(gps.getLatitude());
        testtext2.setText(gps.getLongitude());






    }

    private void doAlert()
    {
        Cursor contacts;
        //final Context context = getApplicationContext();
        ContentResolver cr = getContentResolver();
        final EcallContact currentContact;
        EcallContact tempCurrentContact;

        String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName, Context.MODE_PRIVATE);

        String idKey = getString(R.string.pref_contact_id);
        String existingID = sharedPreferences.getString(idKey,null);

        if(existingID!=null) {
            tempCurrentContact = new EcallContact(existingID);
            contacts = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.RAW_CONTACT_ID + " = " + existingID + "", null, ContactsContract.CommonDataKinds.Email.RAW_CONTACT_ID + " ASC");

            contacts.moveToNext();
            String existingName = null;

            try {
                tempCurrentContact.setDisplayName(contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
            } catch (Exception e) {
                Log.d("Debug", e.getMessage().toString());
            }

            Cursor pCur = cr.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID + " = ?",
                    new String[]{existingID}, null);
            while (pCur.moveToNext())
            {
                int phoneType = pCur.getInt(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                    tempCurrentContact.setPhoneNumber(pCur.getString(contacts.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[^\\d]", ""));
                }
            }
            pCur.close();

            tempCurrentContact.setEmailAddress(contacts.getString(contacts.getColumnIndex(
                    ContactsContract.CommonDataKinds.Email.DATA1)));


            if (existingName != null) {

            }
            // End of todo

            currentContact = tempCurrentContact;

  //          Runnable alertRunnable = new Runnable() {
  //              @Override
  //              public void run() {
                    /////////////////////////////////////////////////////
                    //create SMS alert
                    /////////////////////////////////////////////////////
                    dataHandler datahandler=new dataHandler();
                    EcallAlert alertSMS= null;
                    final dataHandler.GPSobject currentGPS = datahandler.getGPS(getApplicationContext(),
                            getString(R.string.GPSLat),getString(R.string.GPSLONG),
                            getString(R.string.OptSettingsFile));

                    try {
                        String defaultMessage = "I am in need of assistance!";
                        JSONObject payLoadObject = new JSONObject();
                        try {
                            Date baseDate = new Date();
                            String date = new SimpleDateFormat("yyyy/MM/dd").format(baseDate);
                            String time = new SimpleDateFormat("HHmmss").format(baseDate);
                            payLoadObject.put("Message", defaultMessage);
                            payLoadObject.put("Latitude", currentGPS.getLatitude());
                            payLoadObject.put("Longitude", currentGPS.getLongitude());
                            payLoadObject.put("Date", date );
                            payLoadObject.put("Time",time );

                            Log.d("DEBUG", payLoadObject.toString());
                            alertSMS = new EcallAlert(currentContact, EcallAlert.alertMethodEnum.SMS,
                                    payLoadObject.toString());

                            ///////////////////////////////////////
                            // Debug for SMS sending service
                            //////////////////////////////////////////////////

                            EcallMessageDespatcher newSMS = new EcallMessageDespatcherViaSMS(alertSMS);
                            newSMS.prepareMessage();
                            if(newSMS.despatchMessage())
                            {
                                Message msg=new Message();
                                msg.obj="SMS sent";
                                //alertHandler.sendMessage(msg);
                                Toast.makeText(context, "SMS sent", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Message msg=new Message();
                                msg.obj="SMS failed";
                                //alertHandler.sendMessage(msg);
                                Toast.makeText(context, "SMS failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e)
                        {

                        }
                    }
                    catch(EcallDataException e)
                    {

                    }
                //}

           // };

        }


        // If there is no contact set, alert cannot run.

        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.CustomDialogTheme);
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
