package group4.programmingproject1;

import java.io.File;
import java.io.IOException;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Shane Drobnick on 3/11/2016.
 */

public class SoundRecordActivity extends AppCompatActivity {

    private MediaRecorder myRecorder;
    private String outputFile;
    private String fileName;
    private String fileLocation;
    private TextView text;
    private int playTime = 5;
    Context context;
    private boolean recorded=false;
    public static final int RECORD_AUDIO = 0;
    private String alertID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_rec);
        Intent intent=getIntent();
        alertID = intent.getStringExtra("ALERT_ID");
        text = (TextView) findViewById(R.id.text1);
        dataHandler datahandler = new dataHandler();
        playTime =datahandler.getRecordTimeBySeconds(getApplicationContext(),
                        getString(R.string.OptSettingsFile),"SoundVideoRecordTime");

        outputFile = getSoundFilePath(getApplicationContext());

        myRecorder = new MediaRecorder();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    this.RECORD_AUDIO);

        } else {
            myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        }

        myRecorder = new MediaRecorder();
        myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        myRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        myRecorder.setOutputFile(outputFile);


        //hijacking for automation
        if(!recorded)
        {
            recorded=true;
            Log.d("DEBUG","Starting recording");
            start();

        }

    }

    private String getSoundFilePath(Context context) {
        fileLocation = context.getExternalFilesDir(null).getAbsolutePath() + "/";
        fileName = alertID + ".mp3";
        Log.d("Debug","Output:"+ fileLocation + fileName);
        return fileLocation+fileName;
    }
    // hijacking start
    private void start()
    {
        try {
            myRecorder.prepare();
            myRecorder.start();
            lockScreenRotation();
            new postTask().execute("soundrec");
        } catch (IllegalStateException e) {
            // start:it is called before prepare()
            // prepare: it is called after start() or before setOutputFormat()
            e.printStackTrace();
        } catch (IOException e) {
            // prepare() fails
            e.printStackTrace();
        }

        text.setText("Recording Sound");

        Toast.makeText(getApplicationContext(), "Start recording...",
                Toast.LENGTH_SHORT).show();
    }

    public void stop()
    {
        try {
            myRecorder.stop();
            myRecorder.reset();
            myRecorder.release();
            myRecorder  = null;

            text.setText("Recording Stopped");

            Toast.makeText(getApplicationContext(), "Stop recording...",
                    Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException e) {
            //  it is called before start()
            e.printStackTrace();
        } catch (RuntimeException e) {
            // no valid audio/video data has been received
            e.printStackTrace();
        }
    }

    // Sets requested orientation to be unspecified, which allows rotation

    private void unlockScreenRotation()
    {
        this.setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

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

    private class postTask extends AsyncTask<String,Integer,String>
    {

        @Override
        protected String doInBackground(String... params)
        {
            for ( int i = 0; i <= playTime-1; i++)
                try
                {
                    //tracker++;
                    Thread.sleep(1000);
                    Log.d("Debug",""+i+" seconds");


                }catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

            return "true";
        }

        @Override
        protected void onPostExecute(String doneTask)
        {
            //uptimer(tracker);
            Log.d("Debug","Stopping recorder post async");
            stop();
            unlockScreenRotation();

            Intent intent = new Intent(getString(R.string.recording_finished));
            intent.putExtra("fileName",fileName);
            intent.putExtra("fileLocation",fileLocation);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            try {
                Thread.sleep(100);
            }
            catch(Exception e)
            {
                Log.d("DEBUG","Sleep in recorder failed");
            }
            finish();
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }


    }


}