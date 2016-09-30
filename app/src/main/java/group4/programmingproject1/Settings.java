package group4.programmingproject1;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;


public class Settings extends AppCompatActivity {

    CheckBox textBox,emailBox,soundBox,videoBox,callBox,mapgpsBox;
    Switch cameraSwitch;
    Boolean sendTextMessage,sendEmailMessage,sendSound,SendVideo,sendCall,sendMapGPS,cameraWhich;
    String setTextKeyValue,setGmailKeyValue;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //getting setting checkbox values for display if checked or not
        getTextMessageSettingsKeyValueFile();
        getEmailSettingsKeyValueFile();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Attempt to set small icon, if not found, leave blank
        try
        {
            getSupportActionBar().setIcon(R.drawable.appiconsmall);

        }
        catch(Exception e)
        {
            Log.d("Settings","Small logo not found");
        }


        //CheckBox assignment
        textBox = (CheckBox)findViewById(R.id.checkbox_TextMsg);

        //TextMsg CheckBox
        textBox.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (textBox.isChecked())
                {
                    Toast.makeText(Settings.this,"Send Text Message Checked",Toast.LENGTH_SHORT).show();
                    //settings value on needs to be saved
                    setTextKeyValue = "true";
                    setTextMessageSettingsKeyValueFile(setTextKeyValue);
                }
                else
                {
                    Toast.makeText(Settings.this,"Send Text Message Unchecked",Toast.LENGTH_SHORT).show();
                    //setting values off needs to be saved
                    //setAddressKeyValue(false);
                    setTextKeyValue = "false";
                    setTextMessageSettingsKeyValueFile(setTextKeyValue);
                }
            }
        });

        emailBox = (CheckBox)findViewById(R.id.Checkbox_Email);

        //EmailMsg CheckBox

        emailBox.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (emailBox.isChecked())
                {
                    //play click sound
                    //v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                    Toast.makeText(Settings.this,"Send Email Message Checked",Toast.LENGTH_SHORT).show();
                    //settings value on needs to be saved
                    setGmailKeyValue = "true";
                    setEmailSettingsKeyValueFile(setGmailKeyValue);
                }
                else
                {
                    //play click sound
                    //v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                    Toast.makeText(Settings.this,"Send Email Message Unchecked",Toast.LENGTH_SHORT).show();
                    //setting values off needs to be saved
                    setGmailKeyValue = "false";
                    setEmailSettingsKeyValueFile(setGmailKeyValue);
                }
            }
        });



        soundBox = (CheckBox)findViewById(R.id.Checkbox_RecSound);

        //SoundMsg CheckBox

        soundBox.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (soundBox.isChecked())
                {
                    Toast.makeText(Settings.this,"Send record sound Checked",Toast.LENGTH_SHORT).show();
                    //settings value on needs to be saved
                }
                else
                {
                    Toast.makeText(Settings.this,"Send record sound Unchecked",Toast.LENGTH_SHORT).show();
                    //setting values off needs to be saved
                }
            }
        });

        videoBox = (CheckBox)findViewById(R.id.Checkbox_RecVideo);

        //SoundMsg CheckBox

        videoBox.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (videoBox.isChecked())
                {
                    Toast.makeText(Settings.this,"Send record video Checked",Toast.LENGTH_SHORT).show();
                    //settings value on needs to be saved
                }
                else
                {
                    Toast.makeText(Settings.this,"Send record video Unchecked",Toast.LENGTH_SHORT).show();
                    //setting values off needs to be saved
                }
            }
        });

        callBox = (CheckBox)findViewById(R.id.Checkbox_EmeNum);

        //SoundMsg CheckBox

        callBox.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (callBox.isChecked())
                {
                    Toast.makeText(Settings.this,"Call Phone Checked",Toast.LENGTH_SHORT).show();
                    //settings value on needs to be saved
                }
                else
                {
                    Toast.makeText(Settings.this,"Call Phone Unchecked",Toast.LENGTH_SHORT).show();
                    //setting values off needs to be saved
                }
            }
        });

        mapgpsBox = (CheckBox)findViewById(R.id.CheckBox_GPS);

        //SoundMsg CheckBox

        mapgpsBox.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (mapgpsBox.isChecked())
                {
                    Toast.makeText(Settings.this,"Send Gmap/GPS Checked",Toast.LENGTH_SHORT).show();
                    //settings value on needs to be saved
                }
                else
                {
                    Toast.makeText(Settings.this,"Send Gmap/GPS Unchecked",Toast.LENGTH_SHORT).show();
                    //setting values off needs to be saved
                }
            }
        });


        cameraSwitch = (Switch)findViewById(R.id.switch_Camera);

        cameraSwitch.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener()

        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    Toast.makeText(Settings.this,"Face Camera Selected",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Settings.this,"Front Camera Selected",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //based on tutorial for saving preferences
    //https://www.youtube.com/watch?v=Tl6lcP_8Dl4

    private void getTextMessageSettingsKeyValueFile()
    {
        Context context = getApplicationContext();
        String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName, Context.MODE_PRIVATE);

        String key = getString(R.string.SendTextMessage);
        String existingTextMsg = sharedPreferences.getString(key,null);


        if(existingTextMsg != null) {
            if (existingTextMsg.equals("true"))
            {
                CheckBox textBox = (CheckBox)findViewById(R.id.checkbox_TextMsg);
                textBox.setChecked(true);
            }
        }
    }

    // set text message key value
    private void setTextMessageSettingsKeyValueFile(String setTextKeyValue)
    {
        Context context = getApplicationContext();
        String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String key = getString(R.string.SendTextMessage);
        editor.putString(key, setTextKeyValue);

        editor.commit();


    }
    private void getEmailSettingsKeyValueFile()
    {
        Context context = getApplicationContext();
        String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName, Context.MODE_PRIVATE);

        String key = getString(R.string.SendEmail);
        String existingTextMsg = sharedPreferences.getString(key,null);


        if(existingTextMsg != null) {
            if (existingTextMsg.equals("true"))
            {
                CheckBox emailBox = (CheckBox)findViewById(R.id.Checkbox_Email);
                emailBox.setChecked(true);
            }
        }
    }

    // set text message key value
    private void setEmailSettingsKeyValueFile(String setTextKeyValue)
    {
        Context context = getApplicationContext();
        String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String key = getString(R.string.SendEmail);
        editor.putString(key, setTextKeyValue);

        editor.commit();


    }
}

