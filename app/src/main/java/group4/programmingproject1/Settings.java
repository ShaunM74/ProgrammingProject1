package group4.programmingproject1;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class Settings extends AppCompatActivity {

    private CheckBox textBox,emailBox,soundBox,videoBox,callBox,mapgpsBox;
    private Switch cameraSwitch;
    private Boolean sendTextMessage,sendEmailMessage,sendSound,SendVideo,sendCall,sendMapGPS,cameraWhich;
    private String setTextKeyValue,setGmailKeyValue,setCallKeyValue,setSoundKeyValue,setVideoKeyValue,setMapGPSKeyValue,setCameraKeyValue;
    private TableRow contactRow;
    private TextView contactTextBox;
    private ImageView contactImage;
    private static final int myPickerResult = 12347;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context =this;

        contactRow = (TableRow)findViewById(R.id.contactrow);
        contactTextBox = (TextView)findViewById(R.id.contacttext);
        contactImage = (ImageView)findViewById(R.id.contactimage);


        //getting setting checkbox values for display if checked or not
        getTextMessageSettingsKeyValueFile();
        getEmailSettingsKeyValueFile();
        getCallSettingsKeyValueFile();
        getSoundSettingsKeyValueFile();
        getVideoSettingsKeyValueFile();
        getMapGPSSettingsKeyValueFile();
        getCameraSwitchSettingsKeyValueFile();
        getContactSettingsKeyValueFile();

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
                    setSoundKeyValue = "true";
                    setSoundSettingsKeyValueFile(setSoundKeyValue);
                }
                else
                {
                    Toast.makeText(Settings.this,"Send record sound Unchecked",Toast.LENGTH_SHORT).show();
                    //setting values off needs to be saved
                    setSoundKeyValue = "false";
                    setSoundSettingsKeyValueFile(setSoundKeyValue);
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
                    setVideoKeyValue = "true";
                    setVideoSettingsKeyValueFile(setVideoKeyValue);
                }
                else
                {
                    Toast.makeText(Settings.this,"Send record video Unchecked",Toast.LENGTH_SHORT).show();
                    //setting values off needs to be saved
                    setVideoKeyValue = "false";
                    setVideoSettingsKeyValueFile(setVideoKeyValue);
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
                    setCallKeyValue = "true";
                    setCallSettingsKeyValueFile(setCallKeyValue);
                }
                else
                {
                    Toast.makeText(Settings.this,"Call Phone Unchecked",Toast.LENGTH_SHORT).show();
                    //setting values off needs to be saved
                    setCallKeyValue = "false";
                    setCallSettingsKeyValueFile(setCallKeyValue);
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
                    setMapGPSKeyValue = "true";
                    setMapGPSSettingsKeyValueFile(setMapGPSKeyValue);
                }
                else
                {
                    Toast.makeText(Settings.this,"Send Gmap/GPS Unchecked",Toast.LENGTH_SHORT).show();
                    //setting values off needs to be saved
                    setMapGPSKeyValue = "false";
                    setMapGPSSettingsKeyValueFile(setMapGPSKeyValue);
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
                    setCameraKeyValue = "true";
                    setCameraSwitchSettingsKeyValueFile(setCameraKeyValue);
                }
                else
                {
                    Toast.makeText(Settings.this,"Front Camera Selected",Toast.LENGTH_SHORT).show();
                    setCameraKeyValue = "false";
                    setCameraSwitchSettingsKeyValueFile(setCameraKeyValue);
                }
            }
        });



        contactRow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(context, ContactPickerActivity.class);
                startActivityForResult(i,myPickerResult);
            }

        });

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case myPickerResult: {

                    contactTextBox.setText("Contact selected \n");
                    contactTextBox.append(data.getStringExtra("name") + "\n");
                    contactTextBox.append(data.getStringExtra("email") + "\n");
                    contactTextBox.append(data.getStringExtra("phone") + "\n");
                    String image_thumb = data.getStringExtra("thumbURI");
                    // Set a default icon if contact doesn't have a thumbnail
                    try {
                        if (image_thumb != null) {
                            contactImage.setImageURI(Uri.parse(image_thumb));
                        } else {
                            contactImage.setImageResource(R.drawable.appicon);
                            Log.e("No Image Thumb", "--------------");
                        }
                    } catch (Exception e) {

                    }
                    setContactKeyValueFile(data.getStringExtra("name"), data.getStringExtra("email"),
                            data.getStringExtra("phone"), image_thumb);

                }
            }

        } else {
            // gracefully handle failure

        }
    }

    //based on tutorial for saving preferences
    //https://www.youtube.com/watch?v=Tl6lcP_8Dl4

    //these get and set  check the stored values of the checkbox and set the checks accordingly

    //get values for contact
    private void getContactSettingsKeyValueFile()
    {
        Context context = getApplicationContext();
        String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName, Context.MODE_PRIVATE);

        String nameKey = getString(R.string.pref_contact_name);

        String existingName = sharedPreferences.getString(nameKey,null);
        String emailKey = getString(R.string.pref_contact_email);
        String existingEmail = sharedPreferences.getString(emailKey,null);
        String phoneKey = getString(R.string.pref_contact_phone_number);
        String existingPhone = sharedPreferences.getString(phoneKey,null);
        String thumbKey = getString(R.string.pref_contact_image_uri);
        String existingThumb = sharedPreferences.getString(thumbKey,null);

        if(existingName != null) {
            contactTextBox.setText("Contact selected \n");
            contactTextBox.append(existingName + "\n");
            contactTextBox.append(existingEmail + "\n");
            contactTextBox.append(existingPhone + "\n");

            // Set a default icon if there is no thumbnail
            try {
                if (existingThumb != null) {
                    contactImage.setImageURI(Uri.parse(existingThumb));
                } else {
                    contactImage.setImageResource(R.drawable.appicon);
                    Log.e("No Image Thumb", "--------------");
                }
            } catch (Exception e) {

            }

        }
    }

    // set txt message key value
    private void setContactKeyValueFile(String contactName, String contactEmail,
                                        String contactPhone, String contactThumb)
    {
        Context context = getApplicationContext();
        String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String nameKey = getString(R.string.pref_contact_name);
        editor.putString(nameKey, contactName);
        String emailKey = getString(R.string.pref_contact_email);
        editor.putString(emailKey, contactEmail);
        String phoneKey = getString(R.string.pref_contact_phone_number);
        editor.putString(phoneKey, contactPhone);
        // If contact does not have a thumbnail, remove the key.
        if(contactThumb != null) {
            String thumbKey = getString(R.string.pref_contact_image_uri);
            editor.putString(thumbKey, contactThumb);
        }
        else
        {
            editor.remove(getString(R.string.pref_contact_image_uri));
        }


        editor.commit();


    }


    //get txt msg value
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

    // set txt message key value
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
    //get email message value
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

    // set gmail message key value
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
    //get Phone call message value
    private void getCallSettingsKeyValueFile()
    {
        Context context = getApplicationContext();
        String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName, Context.MODE_PRIVATE);

        String key = getString(R.string.CallEmeNum);
        String existingTextMsg = sharedPreferences.getString(key,null);


        if(existingTextMsg != null) {
            if (existingTextMsg.equals("true"))
            {
                CheckBox emailBox = (CheckBox)findViewById(R.id.Checkbox_EmeNum);
                emailBox.setChecked(true);
            }
        }
    }

    // set Phone call message key value
    private void setCallSettingsKeyValueFile(String setTextKeyValue)
    {
        Context context = getApplicationContext();
        String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String key = getString(R.string.CallEmeNum);
        editor.putString(key, setTextKeyValue);

        editor.commit();

    }

    //get Sound message value
    private void getSoundSettingsKeyValueFile()
    {
        Context context = getApplicationContext();
        String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName, Context.MODE_PRIVATE);

        String key = getString(R.string.RecordSnd);
        String existingTextMsg = sharedPreferences.getString(key,null);


        if(existingTextMsg != null) {
            if (existingTextMsg.equals("true"))
            {
                CheckBox emailBox = (CheckBox)findViewById(R.id.Checkbox_RecSound);
                emailBox.setChecked(true);
            }
        }
    }

    // set Sound message key value
    private void setSoundSettingsKeyValueFile(String setTextKeyValue)
    {
        Context context = getApplicationContext();
        String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String key = getString(R.string.RecordSnd);
        editor.putString(key, setTextKeyValue);

        editor.commit();


    }

    //get Video message value
    private void getVideoSettingsKeyValueFile()
    {
        Context context = getApplicationContext();
        String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName, Context.MODE_PRIVATE);

        String key = getString(R.string.RecordVid);
        String existingTextMsg = sharedPreferences.getString(key,null);


        if(existingTextMsg != null) {
            if (existingTextMsg.equals("true"))
            {
                CheckBox emailBox = (CheckBox)findViewById(R.id.Checkbox_RecVideo);
                emailBox.setChecked(true);
            }
        }
    }

    // set Video message key value
    private void setVideoSettingsKeyValueFile(String setTextKeyValue)
    {
        Context context = getApplicationContext();
        String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String key = getString(R.string.RecordVid);
        editor.putString(key, setTextKeyValue);

        editor.commit();


    }

    //get MapGPS message value
    private void getMapGPSSettingsKeyValueFile()
    {
        Context context = getApplicationContext();
        String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName, Context.MODE_PRIVATE);

        String key = getString(R.string.MapsGPS);
        String existingTextMsg = sharedPreferences.getString(key,null);


        if(existingTextMsg != null) {
            if (existingTextMsg.equals("true"))
            {
                CheckBox emailBox = (CheckBox)findViewById(R.id.CheckBox_GPS);
                emailBox.setChecked(true);
            }
        }
    }

    // set MapGPS message key value
    private void setMapGPSSettingsKeyValueFile(String setTextKeyValue)
    {
        Context context = getApplicationContext();
        String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String key = getString(R.string.MapsGPS);
        editor.putString(key, setTextKeyValue);

        editor.commit();


    }

    //get Camera Switch value
    private void getCameraSwitchSettingsKeyValueFile()
    {
        Context context = getApplicationContext();
        String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName, Context.MODE_PRIVATE);

        String key = getString(R.string.CameraSwitch);
        String existingTextMsg = sharedPreferences.getString(key,null);


        if(existingTextMsg != null) {
            if (existingTextMsg.equals("true"))
            {
                Switch switchBox = (Switch)findViewById(R.id.switch_Camera);
                switchBox.setChecked(true);
            }
        }
    }

    // set Camera switch key value
    private void setCameraSwitchSettingsKeyValueFile(String setTextKeyValue)
    {
        Context context = getApplicationContext();
        String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String key = getString(R.string.CameraSwitch);
        editor.putString(key, setTextKeyValue);

        editor.commit();


    }
}

