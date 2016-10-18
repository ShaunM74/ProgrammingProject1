package group4.programmingproject1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

/**
 * Created by Yipster on 16/10/2016.
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

    public class GPSobject
    {
        private String latitude;
        private String longitude;

        public GPSobject (String Latitude, String Longitude)
        {
            latitude = Latitude;
            longitude = Longitude;
        }


        public String getLatitude()
        {
            return this.latitude;
        }

        public String getLongitude()
        {
            return this.longitude;
        }

    }

}


