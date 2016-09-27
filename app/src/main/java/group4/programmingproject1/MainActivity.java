package group4.programmingproject1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageButton alertButton = null;

    private ImageButton cancelButton = null;

    private Vibrator vibrator;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(myToolbar);
        // Try to set icon, if not found, leave blank
        try {
            getSupportActionBar().setIcon(R.drawable.appiconsmall);
        }
        catch (Exception e)
        {
            Log.d("MainActivity","Small logo not found");
        }

        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);


        // Assign buttons to variables

        alertButton = (ImageButton)findViewById(R.id.alertButton);
        cancelButton = (ImageButton)findViewById(R.id.cancelButton);


        // On touch listeners to report button touches
        // Using touch listeners to capture finger raised events.
        alertButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch (View v, MotionEvent event)
            {
                return onAlertTouch(v,event);
            }
        });



    }

    //Gets current screen orientation and sets it as requested screen orientation

    private void lockScreenRotation()
    {
        // Stop the screen orientation changing during an event
        switch (this.getResources().getConfiguration().orientation)
        {
            case Configuration.ORIENTATION_PORTRAIT:
                this.setRequestedOrientation(
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                this.setRequestedOrientation(
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }
    }

    // Sets requested orientation to be unspecified, which allows rotation

    private void unlockScreenRotation()
    {
        this.setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    // Handles touch events on Alert button

    public boolean onAlertTouch (View v, MotionEvent event) {
        Rect rect;
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            lockScreenRotation();
            int dot = 200;      // Length of a Morse Code "dot" in milliseconds
            int dash = 500;     // Length of a Morse Code "dash" in milliseconds
            int short_gap = 200;    // Length of Gap Between dots/dashes
            int medium_gap = 500;   // Length of Gap Between Letters
            int long_gap = 1000;    // Length of Gap Between Words
            long[] pattern = {
                    0,  // Start immediately
                    dot, short_gap, dot, short_gap, dot,    // s
                    medium_gap,
                    dash, short_gap, dash, short_gap, dash, // o
                    medium_gap,
                    dot, short_gap, dot, short_gap, dot,    // s
                    long_gap
            };

            vibrator.vibrate(pattern,0);

        }
        if (event.getAction() == MotionEvent.ACTION_UP) {

            // Check if event occured within the bounds of the button, if so do button action
            // Otherwise cancel/ignore the event.
            rect = new Rect(cancelButton.getLeft(), cancelButton.getTop(), cancelButton.getRight(), cancelButton.getBottom());
            if(rect.contains(v.getLeft()+(int)event.getX(),v.getTop()+(int)event.getY()))
            {
                Toast.makeText(MainActivity.this, "Did not set off alert", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(MainActivity.this, "Set off alert", Toast.LENGTH_LONG).show();
                MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.police);
                mediaPlayer.start();
            }
            vibrator.cancel();
            unlockScreenRotation();
            return true;
        }

        // Return false to indicate that touch event was not handled.
        return false;
    }

    private void doAbout()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.CustomDialogTheme);
        builder.setMessage("Alert! App\nVersion 0.1\nGroup 4\nRMIT")
                .setTitle("Alert!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        Log.d("Main","Created about dialog");

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    // Handles menu choices.
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.about: {
                doAbout();
                return true;
            }
            case R.id.help: {
                return true;
            }
            case R.id.quit:
            {
                // Build Alert dialog to confirm quitting
                AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.CustomDialogTheme);
                builder.setMessage("Do you wish to close this application?")
                        .setTitle("Quit?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })                ;

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
            case R.id.settings:
            {
                Intent intent = new Intent (this, Settings.class);
                startActivity (intent);
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
