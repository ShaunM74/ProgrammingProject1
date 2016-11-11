package org.dyndns.ecall.ecalldataapi;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


import group4.programmingproject1.dataHandler;

/**
 * Created by ShaunM on 17/10/2016.
 */

public class EcallRegister {


    public void EcallRegister() {
    }

    public static String registerDevice(Context context)
    {
        String tempString ="";
        String accountID= dataHandler.getAccountID(context);
        String deviceID = dataHandler.getDeviceKey(context);
        String data=null;
        try {
             data = URLEncoder.encode("accountKey", "UTF-8")
                    + "=" + URLEncoder.encode(accountID, "UTF-8");

            data += "&" + URLEncoder.encode("deviceKey", "UTF-8") + "="
                    + URLEncoder.encode(deviceID, "UTF-8");
        }
        catch(UnsupportedEncodingException e)
        {

        }
        try {
            URL url = new URL("http://ecall.dyndns.org/EcallAPIRegister.php");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            wr.write( data );
            wr.flush();
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
        Log.d("Debug","returning:"+tempString);
        return tempString;
    }


    class registerDevice extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... voids) {
            String tempString ="";
            //tempString = EcallRegister.registerDevice();
            return tempString;
        }


        @Override
        protected void onPostExecute(String results) {

        }
    }








    private static String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }

    public static boolean checkRegistration()
    {
        return false;
    }
}
