/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package group4.programmingproject1;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

/**
 * Created by Shane Drobnick on 25/10/2016.
 */

public class CameraActivity extends Activity {

    String filename="";
    String fileLocation="";
    String alertID="";
    Context context;
    Camera2VideoFragment camFragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cameraactivity);
        lockScreenRotation();
        Intent intent=getIntent();
        alertID = intent.getStringExtra("ALERT_ID");
        if (null == savedInstanceState) {
            camFragment = new Camera2VideoFragment();
            Bundle args = new Bundle();
            args.putString("ALERT_ID", alertID);
            dataHandler datahandler = new dataHandler();
            args.putInt("RECORD_LENGTH",datahandler.getRecordTimeBySeconds(this,
                    getString(R.string.OptSettingsFile),"SoundVideoRecordTime"));
            args.putBoolean("CAMERA",dataHandler.whichCamera(getApplicationContext()));
            camFragment.setArguments(args);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container, camFragment)
                    .commit();
        }


    }

    @Override
    protected void onPause()
    {
        unlockScreenRotation();
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        unlockScreenRotation();
        super.onDestroy();
    }

    public String getAlertID()
    {
        return alertID;
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


}