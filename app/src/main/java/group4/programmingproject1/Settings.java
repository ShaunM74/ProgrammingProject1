package group4.programmingproject1;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;


public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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




}
