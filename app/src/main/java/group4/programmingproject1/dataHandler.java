package group4.programmingproject1;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

/**
 * Created by Yipster on 16/10/2016.
 */

public class dataHandler extends AppCompatActivity
{


    //get video/sound record time setting value
    public int getRecordTime()
    {

        Context context = getApplicationContext();
        String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName, Context.MODE_PRIVATE);

        //String key = getString(R.string.SoundVideoRecordTime);
        //String existingTextMsg = sharedPreferences.getString(key,null);

        int userChoice = sharedPreferences.getInt("SoundVideoRecordTime",-1);
        if (userChoice != -1)
        {
            return userChoice;
        }

        return -1;

    }


    // set video sound record time key value
    public void setRecordTime(int setTextKeyValue, Spinner spinner)
    {
 /*     Context context = getApplicationContext();
        String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String key = getString(R.string.SoundVideoRecordTime);
        editor.putString(key, setTextKeyValue);
*/
        String fileName = getString(R.string.OptSettingsFile);
        int userChoice = spinner.getSelectedItemPosition();
        SharedPreferences shardPref = getSharedPreferences(fileName,0);
        SharedPreferences.Editor prefEditor = shardPref.edit();
        prefEditor.putInt("SoundVideoRecordTime",userChoice);
        prefEditor.commit();

    }
}
