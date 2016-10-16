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

    //get video/sound record time setting value
    public int getRecordTime(Context context,String fileName,String key)
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


    // set video sound record time key value
    public void setRecordTime(int setTextKeyValue, Context context, Spinner spinner, String key, String fileName)
    {
        /*
        context = getApplicationContext();
        fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String key = getString(R.string.SoundVideoRecordTime);
        //editor.putString(key, setTextKeyValue);

        String fileName = getString(R.string.OptSettingsFile);
        int userChoice = spinner.getSelectedItemPosition();
        SharedPreferences shardPref = getSharedPreferences(fileName,0);
        SharedPreferences.Editor prefEditor = shardPref.edit();
        prefEditor.putInt("SoundVideoRecordTime",userChoice);
        prefEditor.commit();
        */
    }

    public int testtestfuck()
    {

        int num = 50;
        return num;
    }
}
