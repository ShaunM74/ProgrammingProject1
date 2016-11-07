package group4.programmingproject1;

import java.io.File;
import java.io.IOException;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
    //private MediaPlayer myPlayer;
    private String outputFile;

   // private TextView text;
    private int playTime = 5;
    //private int playTime = dataHandler.getRecordTimeBySeconds(getApplicationContext());

    public static final int RECORD_AUDIO = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // text = (TextView) findViewById(R.id.text1);
        // store it to sd card
        //outputFile = Environment.getExternalStorageDirectory().
        //       getAbsolutePath() + "/SNDTEST.3gpp";

        //alternative from codestack and our video file save
        outputFile = Environment.getExternalStorageDirectory() + File.separator
                + Environment.DIRECTORY_DCIM + File.separator + "FILE_NAME";


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


        //myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        //myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        myRecorder.setOutputFile(outputFile);
        //myRecorder.setMaxDuration(playTime);

        //hijacking for automation
        start();
    }

    private String getSoundFilePath(Context context) {
        Log.d("Debug","Output:"+ context.getExternalFilesDir(null).getAbsolutePath() + "/" +
                System.currentTimeMillis() + "SRM.mp3");
        return context.getExternalFilesDir(null).getAbsolutePath() + "/"
                + System.currentTimeMillis() + "SRM.mp3";
    }
    // hijacking start
    private void start()
    {
        try {
            myRecorder.prepare();
            myRecorder.start();
            new postTask().execute("soundrec");
        } catch (IllegalStateException e) {
            // start:it is called before prepare()
            // prepare: it is called after start() or before setOutputFormat()
            e.printStackTrace();
        } catch (IOException e) {
            // prepare() fails
            e.printStackTrace();
        }

        //text.setText("Recording Sound");

        Toast.makeText(getApplicationContext(), "Start recording...",
                Toast.LENGTH_SHORT).show();
    }

    public void stop(){
        try {
            myRecorder.stop();
            myRecorder.release();
            myRecorder  = null;

            //text.setText("Recording Stopped");

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
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }


    }


}