package group4.programmingproject1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
//import android.widget.Spinner;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
//import java.util.Date;

/**
 * Created by Shane Drobnick on 16/10/2016.
 */

public class dataHandler extends AppCompatActivity
{

    //private Context context;
    //private String fileName;

    //get video/sound record time setting value as array index
    public int getRecordTimeByIndex(Context context,String fileName,String key)
    {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName, Context.MODE_PRIVATE);

        int userChoice = sharedPreferences.getInt("SoundVideoRecordTime",-1);
        if (userChoice != -1)
        {
            return userChoice;
        }

        return -1;

    }


    // set video sound record time by array index
    public void setRecordTimeByIndex(int userChoice,Context context, String fileName)
    {

        //Locking in Value to no more than 5 seconds no less than 3 defensive programming yo!
        if ( userChoice <= -1)
        {
            userChoice = 0;
        }
        else if ( userChoice >= 3 )
        {
            userChoice = 2;
        }
        else
        {
            userChoice = 1;
        }


        SharedPreferences shardPref = context.getSharedPreferences(fileName,0);
        SharedPreferences.Editor prefEditor = shardPref.edit();
        prefEditor.putInt("SoundVideoRecordTime",userChoice);
        prefEditor.commit();

    }


    //get video/sound record time setting value as actual seconds value
    public int getRecordTimeBySeconds(Context context,String fileName,String key)
    {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName, Context.MODE_PRIVATE);

        int userChoice = sharedPreferences.getInt("SoundVideoRecordTime",-1);
        if (userChoice != -1)
        {
            return userChoice+3;
        }

        return -1;

    }

    // set video sound record time by actual seconds value
    public void setRecordTimeBySeconds(int userChoice,Context context, String fileName)
    {

        //Locking in Value to no more than 5 seconds no less than 3 defensive programming yo!
        if ( userChoice <= 3)
        {
            userChoice = 0;
        }
        else if ( userChoice >= 5 )
        {
            userChoice = 2;
        }
        else
        {
            userChoice = 1;
        }


        SharedPreferences shardPref = context.getSharedPreferences(fileName,0);
        SharedPreferences.Editor prefEditor = shardPref.edit();
        prefEditor.putInt("SoundVideoRecordTime",userChoice);
        prefEditor.commit();

    }

    public void saveGPS(Context context, String latkey,String Longkey,String fileName,String Latitude,String Longitude)
    {

        //Context context = getApplicationContext();
        //String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //String key = getString(R.string.SendTxtMsg);
        editor.putString(latkey, Latitude);
        editor.putString(Longkey,Longitude );

        editor.commit();

    }

    static public void saveGPS(Context context, String latkey,String Longkey,String timekey,String fileName,String Latitude,String Longitude, String time)
    {

        //Context context = getApplicationContext();
        //String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //String key = getString(R.string.SendTxtMsg);
        editor.putString(latkey, Latitude);
        editor.putString(Longkey,Longitude );
        editor.putString(timekey,time);

        editor.commit();

    }

    public void saveGPS(Context context,String Latitude,String Longitude, String time)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                getString(R.string.OptSettingsFile),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //String key = getString(R.string.SendTxtMsg);
        editor.putString(getString(R.string.GPSLat), Latitude);
        editor.putString(getString(R.string.GPSLONG),Longitude );
        editor.putString(getString(R.string.GPStime),time);

        editor.commit();


    }

    public GPSobject getGPS(Context context,String keylat,String keyLong,String fileName)
    {
        //String lat;
        //String longi;

        //Context context = getApplicationContext();
        //String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName, Context.MODE_PRIVATE);

        //String key = getString(R.string.SendEmail);
        String lat = sharedPreferences.getString(keylat,null);
        String longi = sharedPreferences.getString(keyLong,null);
        //String lat = "test1";
        //String longi = "test2";

    /*
        if(existingTextMsg != null) {
            if (existingTextMsg.equals("true"))
            {
                CheckBox emailBox = (CheckBox)findViewById(R.id.Checkbox_Email);
                emailBox.setChecked(true);
            }
        }
    */

        GPSobject gps = new GPSobject(lat,longi);

        return gps;
    }

    public GPSobject getGPS(Context context,String keylat,String keyLong,String keytime,String fileName)
    {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName, Context.MODE_PRIVATE);


        String lat = sharedPreferences.getString(keylat,null);
        String longi = sharedPreferences.getString(keyLong,null);
        String time = sharedPreferences.getString(keytime,null);


        GPSobject gps = new GPSobject(lat,longi,time);

        return gps;
    }

   public GPSobject getGPS(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                getString(R.string.OptSettingsFile), context.MODE_PRIVATE);

        String lat = sharedPreferences.getString(getString(R.string.GPSLat),null);
        String longi = sharedPreferences.getString(getString(R.string.GPSLONG),null);
        String time = sharedPreferences.getString(getString(R.string.GPStime),null);

        GPSobject gps = new GPSobject(lat,longi,time);
        return gps;
    }
    //****************************************
    //checks for settings - these check if setting is selected true, or not false
    // for videa camera selection, true is face cam, false is front cam
    //****************************************
    static public boolean isTxt(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.OptSettingsFile), context.MODE_PRIVATE);

        String key = context.getString(R.string.SendTxtMsg);
        String existingTextMsg = sharedPreferences.getString(key,null);


        if(existingTextMsg != null)
        {
            if (existingTextMsg.equals("true"))
           {
                return true;
           }

        }
        return false;
    }

    static public boolean isCall(Context context)
    {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.OptSettingsFile), context.MODE_PRIVATE);

        String key = context.getString(R.string.CallEmeNum);
        String existingTextMsg = sharedPreferences.getString(key,null);


        if(existingTextMsg != null) {
            if (existingTextMsg.equals("true"))
            {
                return true;
            }
        }
        return false;
    }

   static public boolean isVid(Context context)
    {


        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.OptSettingsFile), context.MODE_PRIVATE);

        String key = context.getString(R.string.RecordVid);
        String existingTextMsg = sharedPreferences.getString(key,null);


        if(existingTextMsg != null) {
            if (existingTextMsg.equals("true"))
            {
                return true;
            }
        }

        return false;
    }

    //if isVid is yes and this can show you whether Front or not
    //true = front
    //false = face
    static public boolean whichCamera(Context context)
    {
        //Context context = getApplicationContext();
        //String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.OptSettingsFile), context.MODE_PRIVATE);

        String key = context.getString(R.string.CameraSwitch);
        String existingTextMsg = sharedPreferences.getString(key,null);


        if(existingTextMsg != null) {
            if (existingTextMsg.equals("true"))
            {
                //front cam if true
                return true;
            }
        }
        //face cam
        return false;
    }


    static public boolean isSnd(Context context)
    {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.OptSettingsFile), context.MODE_PRIVATE);

        String key = context.getString(R.string.RecordSnd);
        String existingTextMsg = sharedPreferences.getString(key,null);


        if(existingTextMsg != null) {
            if (existingTextMsg.equals("true"))
            {
                return true;
            }
        }
        return false;
    }

   static public boolean isEmail(Context context)
    {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.OptSettingsFile), context.MODE_PRIVATE);

        String key = context.getString(R.string.SendEmail);
        String existingTextMsg = sharedPreferences.getString(key,null);


        if(existingTextMsg != null) {
            if (existingTextMsg.equals("true"))
            {
                return true;
            }
        }
        return false;
    }

    static public  boolean isGPSMaps(Context context)
    {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.OptSettingsFile), Context.MODE_PRIVATE);

        String key = context.getString(R.string.MapsGPS);
        String existingTextMsg = sharedPreferences.getString(key,null);


        if(existingTextMsg != null) {
            if (existingTextMsg.equals("true"))
            {
                return true;
            }
        }

        return false;
    }


//OBJECTS
    public class GPSobject
    {
        private String latitude;
        private String longitude;

        private String GPStime;

        public GPSobject (String Latitude, String Longitude)
        {
            latitude = Latitude;
            longitude = Longitude;
            GPStime = null;
        }

        public GPSobject (String Latitude,String Longitude,String gpsTime)
        {
            latitude = Latitude;
            longitude = Longitude;
            GPStime = gpsTime;
        }


        public String getLatitude()
        {
            return this.latitude;
        }

        public String getLongitude()
        {
            return this.longitude;
        }

        public String getGPSTime() {return this.GPStime;}

    }



}
