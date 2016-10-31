package group4.programmingproject1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.cast.CastRemoteDisplayLocalService;

import org.dyndns.ecall.ecalldataapi.*;
import org.dyndns.ecall.ecallsendapi.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AlertService extends Service {


    private final IBinder binder = new AlertServiceBinder();

    Handler alertHandler;
    Context tempcontext;

    public AlertService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        tempcontext = this;
        alertHandler = new Handler() {
            @Override public void handleMessage(Message msg) {
                Log.d("DEBUG", "In handler");
                String mString=(String)msg.obj;
                Toast.makeText(tempcontext, mString, Toast.LENGTH_SHORT).show();
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(alertBroadcastReceiver,
                new IntentFilter("startInstAlertAlarm"));


        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(alertBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "Service binded", Toast.LENGTH_SHORT).show();
        return binder;
    }
    //returns the instance of the service
    public class LocalBinder extends Binder {
        public AlertService getServiceInstance(){
            return AlertService.this;
        }
    }

    public final class AlertServiceBinder extends Binder {
        final AlertService getService() {
            return AlertService.this;
        }
    }

    private BroadcastReceiver alertBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
//            String message = intent.getStringExtra("message");
//            Log.d("receiver", "Got message: " + message);
            startAlert();
        }
    };

    public void startAlert()
    {
        Context context = getApplicationContext();
        Toast.makeText(context, "Alert Processing Started", Toast.LENGTH_SHORT).show();

// ToDO: Update to use the dataHandler to retrieve the contact details

        Cursor contacts;

        ContentResolver cr = getContentResolver();
        EcallContact tempCurrentContact;
        final EcallContact currentContact;
        String existingPhone="";
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
                tempCurrentContact.setDisplayName(""+contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                        Log.d("Debug","Name:"+contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
            } catch (Exception e) {
                Log.d("Debug", "ERROR:"+e.getMessage().toString());
            }
            if(tempCurrentContact.getDisplayName() == null)
            {
                Log.d("Debug",tempCurrentContact.getDisplayName());
            }

            Cursor pCur = cr.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID + " = ?",
                    new String[]{existingID}, null);
            while (pCur.moveToNext()) {
                int phoneType = pCur.getInt(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                    tempCurrentContact.setPhoneNumber(""+pCur.getString(contacts.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[^\\d]", ""));
                    Log.d("Debug","Phone:"+pCur.getString(contacts.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER)).toString().replaceAll("[^\\d]", ""));
                }
            }
            pCur.close();

            tempCurrentContact.setEmailAddress(""+contacts.getString(contacts.getColumnIndex(
                    ContactsContract.CommonDataKinds.Email.DATA1)));

            currentContact=tempCurrentContact;
            if (currentContact.getDisplayName() == null)
            {
                Log.d("Debug","Display name null");
            }


                // End of todo


                try {
                    //Runnable alertRunnable =
                    new Runnable() {
                        @Override
                        public void run() {
                            /////////////////////////////////////////////////////
                            //create SMS alert
                            /////////////////////////////////////////////////////

                            dataHandler datahandler = new dataHandler();
                            EcallAlert alertSMS = null;
                            final dataHandler.GPSobject currentGPS = datahandler.getGPS(getApplicationContext(),
                                    getString(R.string.GPSLat), getString(R.string.GPSLONG),
                                    getString(R.string.OptSettingsFile));

                            try {
                                String defaultMessage = "I am in need of assistance!";
                                JSONObject payLoadObject = new JSONObject();
                                try {
                                    Date baseDate = new Date();
                                    String date = new SimpleDateFormat("yyyy/MM/dd").format(baseDate);
                                    String time = new SimpleDateFormat("HH:mm:ss").format(baseDate);
                                    payLoadObject.put("Message", defaultMessage);
                                    payLoadObject.put("Latitude", currentGPS.getLatitude());
                                    payLoadObject.put("Longitude", currentGPS.getLongitude());
                                    payLoadObject.put("Date", date);
                                    payLoadObject.put("Time", time);

                                    Log.d("DEBUG", payLoadObject.toString());
                                    alertSMS = new EcallAlert(currentContact, EcallAlert.alertMethodEnum.SMS,
                                            payLoadObject.toString());

                                    ///////////////////////////////////////
                                    // Debug for SMS sending service
                                    //////////////////////////////////////////////////

                                    EcallMessageDespatcher newSMS = new EcallMessageDespatcherViaSMS(alertSMS);
                                    newSMS.prepareMessage();
                                    if (newSMS.despatchMessage()) {
                                        Log.d("Debug", "SMS Sent");
                                        Message msg = new Message();
                                        msg.obj = "SMS sent";
                                        alertHandler.sendMessage(msg);

                                    } else {
                                        Log.d("Debug", "SMS Sent");
                                        Message msg = new Message();
                                        msg.obj = "SMS failed";
                                        alertHandler.sendMessage(msg);
                                    }
                                } catch (JSONException e) {

                                }
                            } catch (EcallDataException e) {

                            }
                        }

                    }.run();
                } catch (Exception e) {
                    Log.d("DEBUG", e.getMessage().toString());
                }

           // }
        }

        // If there is no contact set, alert cannot run.

        else
        {
            Intent intent = new Intent("noContactError");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }

        Log.d("DEBUG", "Past runnable!");

    }
}
