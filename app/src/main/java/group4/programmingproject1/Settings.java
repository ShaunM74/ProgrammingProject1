package group4.programmingproject1;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;


public class Settings extends AppCompatActivity {

    CheckBox textBox,emailBox,soundBox,videoBox,callBox;

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
                    Toast.makeText(Settings.this,"Send Email Message Checked",Toast.LENGTH_SHORT).show();
                    //settings value on needs to be saved
                }
                else
                {
                    Toast.makeText(Settings.this,"Send Email Message Unchecked",Toast.LENGTH_SHORT).show();
                    //setting values off needs to be saved
                }
            }
        });





    }

}
