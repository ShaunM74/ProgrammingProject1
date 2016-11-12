package group4.programmingproject1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Shane Drobnick on 6/11/2016.
 */


public class PhoneActivity extends AppCompatActivity
{

    Button call;
    //number needs to be retrieved from contact settings here , edit to test
    private String number = "0467640471";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        //** add contact given number to be called retrieval here
        //number =
        //*******************

        Intent intent = new Intent(android.content.Intent.ACTION_CALL, Uri.parse("tel: "+number));

        if (ContextCompat.checkSelfPermission(PhoneActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PhoneActivity.this, new String[]{Manifest.permission.CALL_PHONE},1);
        }
        else
        {
            startActivity(intent);
        }

        finish();

    }
}