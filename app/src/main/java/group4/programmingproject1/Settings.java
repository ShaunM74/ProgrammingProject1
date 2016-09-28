package group4.programmingproject1;


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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(myToolbar);

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
                }
                else
                {
                    Toast.makeText(Settings.this,"Send Text Message Unchecked",Toast.LENGTH_SHORT).show();
                    //setting values off needs to be saved
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
                }
                else
                {
                    //play click sound
                    //v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                    Toast.makeText(Settings.this,"Send Email Message Unchecked",Toast.LENGTH_SHORT).show();
                    //setting values off needs to be saved
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

}
