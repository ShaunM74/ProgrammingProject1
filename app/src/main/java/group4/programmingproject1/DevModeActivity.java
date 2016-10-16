package group4.programmingproject1;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

import static android.R.attr.button;

public class DevModeActivity extends AppCompatActivity {

    private Button testRegister;
    private Button testContact;
    private Button clearTextButton;
    private TextView TextBox;
    Context context;
    private static final int CONTACT_PICKER_RESULT = 1001;
    private static final int myPickerResult = 12376;

    //test code for spinner data being recovered from datahandler
    TextView testtext;
    Spinner SpinnerVidSnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_mode);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(myToolbar);
        // Try to set icon, if not found, leave blank

        if(getSupportActionBar() != null)
            getSupportActionBar().setIcon(R.drawable.appiconsmall);

        testRegister = (Button)findViewById(R.id.testregisterButton);
        testContact = (Button)findViewById(R.id.testContactButton);
        clearTextButton = (Button)findViewById(R.id.clearTextButton);
        TextBox = (TextView)findViewById(R.id.returnTextView);
        context=this;

        // On touch listeners to report button touches
        // Using touch listeners to capture finger raised events.
        clearTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v)
            {
                TextBox.setText("");
            }
        });

        testContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
 /*               Intent i = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI);
                //i.setType(ContactsContract.CommonDataKinds.Email.CONTEN‌​T_TYPE);
                startActivityForResult(i, CONTACT_PICKER_RESULT);*/
                Intent i = new Intent(context, ContactPickerActivity.class);
                startActivityForResult(i,myPickerResult);
                //startActivity(i);
            }

        });

        testRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                new registerDevice().execute();
            }

        });

        //test value
        dataHandler data = new dataHandler();
        testtext = (TextView) findViewById(R.id.testSpinner);
        testtext.setText( String.valueOf(data.getRecordTime()));

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    // handle contact results

                    Cursor cursor = null;

                    try {
                        Uri result = data.getData();
                        String id = result.getLastPathSegment();

                        //Get Name
                        cursor = getContentResolver().query(result, null, null, null, null);
                        if (cursor.moveToFirst()) {
                            TextBox.append(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)) + "\n");
                            TextBox.append(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)) + "\n");
                            TextBox.append(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + "\n");
                            TextBox.append(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)) + "\n");

                        }
                        cursor.close();

                        } catch (Exception e) { }
                    break;
                case myPickerResult:
                {
                    TextBox.append(data.getStringExtra("name")+"\n");
                    TextBox.append(data.getStringExtra("email")+"\n");
                    TextBox.append(data.getStringExtra("phone")+"\n");
                }
            }

        } else {
            // gracefully handle failure

        }
    }



    class registerDevice extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... voids) {
            String tempString ="";
            try {
                URL url = new URL("http://54.70.221.177/testecallRegister.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    tempString=readStream(in);
                } finally {
                    urlConnection.disconnect();
                }
            }
            catch(Exception e)
                {

                }

             return tempString;
        }

        private String readStream(InputStream is) throws IOException {
            StringBuilder sb = new StringBuilder();
            BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
            for (String line = r.readLine(); line != null; line =r.readLine()){
                sb.append(line);
            }
            is.close();
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String results) {
            TextBox.append(results.toString());

        }
    }


}
