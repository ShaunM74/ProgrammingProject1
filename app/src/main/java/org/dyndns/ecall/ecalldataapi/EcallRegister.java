package org.dyndns.ecall.ecalldataapi;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ShaunM on 17/10/2016.
 */

public class EcallRegister {


    public void EcallRegister() {
    }

    public static String registerDevice()
    {
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


    class registerDevice extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... voids) {
            String tempString ="";
            tempString = EcallRegister.registerDevice();
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
