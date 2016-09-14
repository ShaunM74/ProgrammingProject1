package group4.programmingproject1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final Context context = this;

    private ImageButton alertButton = null;

    private ImageButton cancelButton = null;

    private ImageButton settingsButton = null;

    private Button nameButton = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameButton = (Button) findViewById(R.id.nameButton);
        alertButton = (ImageButton)findViewById(R.id.alertButton);
        cancelButton = (ImageButton)findViewById(R.id.cancelButton);
        settingsButton = (ImageButton)findViewById(R.id.settingsButton);

        alertButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch (View v, MotionEvent event)
            {
                return onAlertTouch(v,event);
            }
        });

        cancelButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch (View v, MotionEvent event)
            {
                return onCancelTouch(v,event);
            }
        });


    }



    public boolean onAlertTouch (View v, MotionEvent event) {
        Rect rect;
        if (event.getAction() == MotionEvent.ACTION_UP) {
            rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
            if(rect.contains(v.getLeft()+(int)event.getX(),v.getTop()+(int)event.getY()))
            {
                Toast.makeText(MainActivity.this, "Set off alert", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(MainActivity.this, "Did not set off alert", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            Toast.makeText(MainActivity.this, "Alert cancelled", Toast.LENGTH_LONG).show();
            return true;
        }

        else
        {
            Log.d("movement",event.toString());
        }
        return false;
    }

    public boolean onCancelTouch (View v, MotionEvent event) {
        Rect rect;
        if (event.getAction() == MotionEvent.ACTION_UP) {
            rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
            if(rect.contains(v.getLeft()+(int)event.getX(),v.getTop()+(int)event.getY()))
            {
                Toast.makeText(MainActivity.this, "Set off cancel", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(MainActivity.this, "Did not set off cancel", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            Toast.makeText(MainActivity.this, "Alert cancelled", Toast.LENGTH_LONG).show();
            return true;
        }

        else
        {
            Log.d("movement",event.toString());
        }
        return false;
    }


    public void doAboutButton(View view) {
        doAbout();
    }

    private void doAbout()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.about: {
                doAbout();
                return true;
            }
            case R.id.help:

                return true;
            case R.id.quit:
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
                Log.d("Main","quitDialog");

            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
