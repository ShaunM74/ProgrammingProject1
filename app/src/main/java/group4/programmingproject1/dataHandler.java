package group4.programmingproject1;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.*;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
//import android.widget.Spinner;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
//import java.util.Date;

/**
 * Created by Shane Drobnick on 16/10/2016.
 */

public class dataHandler extends AppCompatActivity
{

    //private Context context;
    //private String fileName;

    //get video/sound record time setting value as array index
    public int getRecordTimeByIndex(Context context, String fileName, String key) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName, Context.MODE_PRIVATE);

        int userChoice = sharedPreferences.getInt("SoundVideoRecordTime", -1);
        if (userChoice != -1) {
            return userChoice;
        }

        return -1;

    }


    // set video sound record time by array index
    public void setRecordTimeByIndex(int userChoice, Context context, String fileName) {

        //Locking in Value to no more than 5 seconds no less than 3 defensive programming yo!
        if ( userChoice <= -1)
        {
            userChoice = 0;
        }
        else if ( userChoice >= 3 )
        {
            userChoice = 2;
        }
        else
        {
            userChoice = 1;
        }


        SharedPreferences shardPref = context.getSharedPreferences(fileName,0);
        SharedPreferences.Editor prefEditor = shardPref.edit();
        prefEditor.putInt("SoundVideoRecordTime",userChoice);
        prefEditor.commit();

    }


    //get video/sound record time setting value as actual seconds value
    public int getRecordTimeBySeconds(Context context, String fileName, String key) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName, Context.MODE_PRIVATE);

        int userChoice = sharedPreferences.getInt("SoundVideoRecordTime",-1);
        if (userChoice != -1)
        {
            return userChoice+3;
        }

        return -1;

    }

    public static int getRecordTimeBySeconds(Context context)
    {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.OptSettingsFile), Context.MODE_PRIVATE);

        int userChoice = sharedPreferences.getInt("SoundVideoRecordTime",-1);
        if (userChoice != -1)
        {
            return userChoice+3;
        }

        return -1;
    }

    // set video sound record time by actual seconds value
    public void setRecordTimeBySeconds(int userChoice,Context context, String fileName)
    {

        //Locking in Value to no more than 5 seconds no less than 3 defensive programming yo!
        if ( userChoice <= 3)
        {
            userChoice = 0;
        }
        else if ( userChoice >= 5 )
        {
            userChoice = 2;
        }
        else
        {
            userChoice = 1;
        }


        SharedPreferences shardPref = context.getSharedPreferences(fileName,0);
        SharedPreferences.Editor prefEditor = shardPref.edit();
        prefEditor.putInt("SoundVideoRecordTime",userChoice);
        prefEditor.commit();

    }

    public void saveGPS(Context context, String latkey,String Longkey,String fileName,String Latitude,String Longitude)
    {

        //Context context = getApplicationContext();
        //String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //String key = getString(R.string.SendTxtMsg);
        editor.putString(latkey, Latitude);
        editor.putString(Longkey,Longitude );

        editor.commit();

    }

    static public void saveGPS(Context context, String latkey,String Longkey,String timekey,String fileName,String Latitude,String Longitude, String time)
    {

        //Context context = getApplicationContext();
        //String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //String key = getString(R.string.SendTxtMsg);
        editor.putString(latkey, Latitude);
        editor.putString(Longkey,Longitude );
        editor.putString(timekey,time);

        editor.commit();

    }

    public void saveGPS(Context context,String Latitude,String Longitude, String time)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                getString(R.string.OptSettingsFile),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //String key = getString(R.string.SendTxtMsg);
        editor.putString(getString(R.string.GPSLat), Latitude);
        editor.putString(getString(R.string.GPSLONG),Longitude );
        editor.putString(getString(R.string.GPStime),time);

        editor.commit();


    }

   static  public void saveAddress(Context context, String address)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.OptSettingsFile), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        editor.putString(context.getString(R.string.GPSAddress), address);

        editor.commit();


    }

   static public String getaddress(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.OptSettingsFile), context.MODE_PRIVATE);

        String address = sharedPreferences.getString(context.getString(R.string.GPSAddress), null);


        return address;
    }

    public GPSobject getGPS(Context context, String keylat, String keyLong, String fileName) {
        //String lat;
        //String longi;

        //Context context = getApplicationContext();
        //String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName, Context.MODE_PRIVATE);

        //String key = getString(R.string.SendEmail);
        String lat = sharedPreferences.getString(keylat,null);
        String longi = sharedPreferences.getString(keyLong,null);
        //String lat = "test1";
        //String longi = "test2";

    /*
        if(existingTextMsg != null) {
            if (existingTextMsg.equals("true"))
            {
                CheckBox emailBox = (CheckBox)findViewById(R.id.Checkbox_Email);
                emailBox.setChecked(true);
            }
        }
    */

        GPSobject gps = new GPSobject(lat,longi);

        return gps;
    }

    public GPSobject getGPS(Context context,String keylat,String keyLong,String keytime,String fileName)
    {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName, Context.MODE_PRIVATE);


        String lat = sharedPreferences.getString(keylat,null);
        String longi = sharedPreferences.getString(keyLong,null);
        String time = sharedPreferences.getString(keytime,null);


        GPSobject gps = new GPSobject(lat, longi, time);

        return gps;
    }

   public GPSobject getGPS(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                getString(R.string.OptSettingsFile), context.MODE_PRIVATE);

        String lat = sharedPreferences.getString(getString(R.string.GPSLat),null);
        String longi = sharedPreferences.getString(getString(R.string.GPSLONG),null);
        String time = sharedPreferences.getString(getString(R.string.GPStime),null);

        GPSobject gps = new GPSobject(lat,longi,time);
        return gps;
    }
    //****************************************
    //checks for settings - these check if setting is selected true, or not false
    // for videa camera selection, true is face cam, false is front cam
    //****************************************
    static public boolean isTxt(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.OptSettingsFile), context.MODE_PRIVATE);

        String key = context.getString(R.string.SendTxtMsg);
        String existingTextMsg = sharedPreferences.getString(key,null);


        if(existingTextMsg != null)
        {
            if (existingTextMsg.equals("true"))
           {
                return true;
           }

        }
        return false;
    }

    static public boolean isCall(Context context)
    {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.OptSettingsFile), context.MODE_PRIVATE);

        String key = context.getString(R.string.CallEmeNum);
        String existingTextMsg = sharedPreferences.getString(key,null);


        if(existingTextMsg != null) {
            if (existingTextMsg.equals("true"))
            {
                return true;
            }
        }
        return false;
    }

   static public boolean isVid(Context context)
    {


        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.OptSettingsFile), context.MODE_PRIVATE);

        String key = context.getString(R.string.RecordVid);
        String existingTextMsg = sharedPreferences.getString(key,null);


        if(existingTextMsg != null) {
            if (existingTextMsg.equals("true"))
            {
                return true;
            }
        }

        return false;
    }

    //if isVid is yes and this can show you whether Front or not
    //true = front
    //false = face
    static public boolean whichCamera(Context context)
    {
        //Context context = getApplicationContext();
        //String fileName = getString(R.string.OptSettingsFile);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.OptSettingsFile), context.MODE_PRIVATE);

        String key = context.getString(R.string.CameraSwitch);
        String existingTextMsg = sharedPreferences.getString(key,null);


        if(existingTextMsg != null) {
            if (existingTextMsg.equals("true"))
            {
                //front cam if true
                return true;
            }
        }
        //face cam
        return false;
    }


    static public boolean isSnd(Context context)
    {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.OptSettingsFile), context.MODE_PRIVATE);

        String key = context.getString(R.string.RecordSnd);
        String existingTextMsg = sharedPreferences.getString(key,null);


        if(existingTextMsg != null) {
            if (existingTextMsg.equals("true"))
            {
                return true;
            }
        }
        return false;
    }

   static public boolean isEmail(Context context)
    {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.OptSettingsFile), context.MODE_PRIVATE);

        String key = context.getString(R.string.SendEmail);
        String existingTextMsg = sharedPreferences.getString(key,null);


        if(existingTextMsg != null) {
            if (existingTextMsg.equals("true"))
            {
                return true;
            }
        }
        return false;
    }

    static public  boolean isGPSMaps(Context context)
    {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.OptSettingsFile), Context.MODE_PRIVATE);

        String key = context.getString(R.string.MapsGPS);
        String existingTextMsg = sharedPreferences.getString(key,null);


        if(existingTextMsg != null) {
            if (existingTextMsg.equals("true"))
            {
                return true;
            }
        }

        return false;
    }


//OBJECTS
    public class GPSobject
    {
        private String latitude;
        private String longitude;

        private String GPStime;

        public GPSobject (String Latitude, String Longitude)
        {
            latitude = Latitude;
            longitude = Longitude;
            GPStime = null;
        }

        public GPSobject (String Latitude,String Longitude,String gpsTime)
        {
            latitude = Latitude;
            longitude = Longitude;
            GPStime = gpsTime;
        }


        public String getLatitude()
        {
            return this.latitude;
        }

        public String getLongitude()
        {
            return this.longitude;
        }

        public String getGPSTime() {return this.GPStime;}

    }

    public static String getAccount(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Log.d("Debug", "Account:" + accounts[0].name.toString());
        return accounts[0].toString();
    }

    public static String getDeviceID(Context context) {
        String androidID = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("Debug", "Account:" + androidID);
        return androidID;
    }

    public static String getCertID(Context context)
    {
        String certID = "f45f2a3a9dae58f3455d49addc08a4c125b47d52a2178ef97e61bad54f4b23df";
        return certID;
    }
    public static String getCertPEM(Context context)
    {
        String certPEM = "-----BEGIN CERTIFICATE-----\n" +
                "MIIDWjCCAkKgAwIBAgIVAJb+S4+EQ3Bo5qnOQDRmH7FPHtrpMA0GCSqGSIb3DQEB\n" +
                "CwUAME0xSzBJBgNVBAsMQkFtYXpvbiBXZWIgU2VydmljZXMgTz1BbWF6b24uY29t\n" +
                "IEluYy4gTD1TZWF0dGxlIFNUPVdhc2hpbmd0b24gQz1VUzAeFw0xNjEwMjQwNTU1\n" +
                "NDFaFw00OTEyMzEyMzU5NTlaMB4xHDAaBgNVBAMME0FXUyBJb1QgQ2VydGlmaWNh\n" +
                "dGUwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCU0l2oY/ozPNPdeUiI\n" +
                "atbNjjCcYpmx22YvOj1e9gN3+5LyTAvL8F41PHuSVyAGyZC1NXU54grc5Nl/Vmb/\n" +
                "ebjOoF2h7Ex4bLmazYGlfknvl85lMHH/kgRYyyBc6tObZnQERTRWPpOYu05rbj4R\n" +
                "WTJCIQuJTLkXQ1T52vExJFJdNXowQ9EkeweLfmm8F2MhYrKQ3r8kIWQzBKlLFFY/\n" +
                "vznJOjNmTZM22/qX6d7KuMphiEywu7F/a5nQMqSGxRMcuM3zRQp6xwqrQXAtdc72\n" +
                "lXJbjT5HFdVzi0uOZmMHORrx8cIcdMszepg6l85kOCAYuYYdcVcBT8Y1l3SuJ1aV\n" +
                "8wU5AgMBAAGjYDBeMB8GA1UdIwQYMBaAFNsFHilw3TRm7wqbmmqSxf/ICMxIMB0G\n" +
                "A1UdDgQWBBQp8RL4hjT1X3342X+/lgN/TDbOgjAMBgNVHRMBAf8EAjAAMA4GA1Ud\n" +
                "DwEB/wQEAwIHgDANBgkqhkiG9w0BAQsFAAOCAQEAfiF8CwmFbgpGaylbpeEruk9z\n" +
                "1ZTEOQeckvm03qqgf38WUeFc4mdJWIjzwhNzq/3TWbSBEdn0m4elvTArsAwqYOtC\n" +
                "J5USPuzuGdlaZnSccFfDe1RpQD7+CTIY8+TMS7gLleRCqO0DrGFZSdPazcS//O7c\n" +
                "Gnsl3D6oV2BhXN2WyEtlNC4quM/E+nDMcl1MyzmSt4cKlxdyqbIUV0NrT9kuBX84\n" +
                "HHRGmS4EWDQdRw7gNl79zh6nXrybonwEB2TY/JfvflkTkvGbWXUOj6sPIpblQM7y\n" +
                "TvBD/BrlFy0wJtWwKLshdTC1VH4xG7wLVrtEq8Bnm4WBAXpVrhfkIuuFN6aIKA==\n" +
                "-----END CERTIFICATE-----\n";
        return certPEM;
    }
    public static String getPubKey(Context context)
    {
        String certPubKey = "-----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlNJdqGP6MzzT3XlIiGrW\n" +
                "zY4wnGKZsdtmLzo9XvYDd/uS8kwLy/BeNTx7klcgBsmQtTV1OeIK3OTZf1Zm/3m4\n" +
                "zqBdoexMeGy5ms2BpX5J75fOZTBx/5IEWMsgXOrTm2Z0BEU0Vj6TmLtOa24+EVky\n" +
                "QiELiUy5F0NU+drxMSRSXTV6MEPRJHsHi35pvBdjIWKykN6/JCFkMwSpSxRWP785\n" +
                "yTozZk2TNtv6l+neyrjKYYhMsLuxf2uZ0DKkhsUTHLjN80UKescKq0FwLXXO9pVy\n" +
                "W40+RxXVc4tLjmZjBzka8fHCHHTLM3qYOpfOZDggGLmGHXFXAU/GNZd0ridWlfMF\n" +
                "OQIDAQAB\n" +
                "-----END PUBLIC KEY-----\n";
        return certPubKey;
    }
    public static String getPrivKey(Context context)
    {
        String certPrivKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
                "MIIEpAIBAAKCAQEAlNJdqGP6MzzT3XlIiGrWzY4wnGKZsdtmLzo9XvYDd/uS8kwL\n" +
                "y/BeNTx7klcgBsmQtTV1OeIK3OTZf1Zm/3m4zqBdoexMeGy5ms2BpX5J75fOZTBx\n" +
                "/5IEWMsgXOrTm2Z0BEU0Vj6TmLtOa24+EVkyQiELiUy5F0NU+drxMSRSXTV6MEPR\n" +
                "JHsHi35pvBdjIWKykN6/JCFkMwSpSxRWP785yTozZk2TNtv6l+neyrjKYYhMsLux\n" +
                "f2uZ0DKkhsUTHLjN80UKescKq0FwLXXO9pVyW40+RxXVc4tLjmZjBzka8fHCHHTL\n" +
                "M3qYOpfOZDggGLmGHXFXAU/GNZd0ridWlfMFOQIDAQABAoIBABxL52z2HYOShEIv\n" +
                "JmBx2Afbilih0tFjgwllzHd61WwB1I3ncbLEMFV0+5X2pOtFdhNOZ8yqsyAPD1/L\n" +
                "4OegkEgRa9w23s9i9ON/QbBi09IPjjnlPTe33sW3UZ75M2Tv1Q1ezzW6zjuTGbUJ\n" +
                "kBmiWRED0Xq4sNTAg67CN2v9mP+JuUyeKMiOl9Cb7k7vg8D5jqE8H20Zkm4cU5Fx\n" +
                "OWyo+G5ZvgEju06M0aittLMOXi2sdFhGS1iCDrbbPDLOl0R4VOsJK4LCu6P0UyuA\n" +
                "9k0zXkIkNOfwFDBKMbw8+f6A6iE5F+rkYrZK35lpAzlzRjSDXihJ/nnSlEiH8LzF\n" +
                "pgWPNQECgYEA5bOUj5cJF+SQguJBhII+BucATj7UgJ+GBTnUNYWvWNTgVgtq2TUE\n" +
                "dgQpHYcIuDMkgPY3P2fau0kTornEK1QSFZdapzpEQ6ju8vkac8F9IIraU7Cy8NVq\n" +
                "ye4E0h5fhuvmWj/9nZK2o/QVsTxc5P9knHNh4yu6RShOMGkzkI7MEtkCgYEApdw/\n" +
                "DncV2ZAhbZT0Z479O+huRvXrqdVNbIAXFV5IjtqLSVZUjWEvN2H81rJgxBedLNNX\n" +
                "kIrfIz/lUW3JNXWz0b52u9buAmNPxlMRTH6WiHj8kOOSHw7deG80V/4YKb8E3qsC\n" +
                "ebBYrNZ3/j9ziAU6UJ4phxvC8grLt1e9/ktASWECgYAy4X2V9PnRrhKIu3+Rz5vX\n" +
                "wZGiw24k87EnNTEZjfxSbA0pYiwP4xuS3McSwFehHcsHOgLw65tbAwvzbrzScDP1\n" +
                "TmqJQnHenuwXLDC81W5XKArGoQxyJoQaKLwuowW8CEMlWKlgHyCP3sBzWxCktSHf\n" +
                "OfvD62q+aPgw5wzCB6+SuQKBgQCf1kl6Mi7/VFOym+qQZg8KjarAfbvXFjUfy6zz\n" +
                "LcI00M2MJkMjYDW1ZJvZd1ujfbI9gYoQRJRXDvt6ZpeSClT5W1hnGvCRoo09bc3s\n" +
                "7s04xYd4RacKgTPTHS/PYJJ+oJQb1ad0CArcvW4zuYgn3AnfqRwSFSHgSaEaldcF\n" +
                "2hUg4QKBgQDiuzm7uhhso4osk9Xtp4aMX81H6zAwajZuOu+boBchXT9QR98a77ws\n" +
                "wbl0qW9x2NFSMe/dX+EeZv73C7Tic9CsAh0f18YnBVcxSSmfnGBk1NTaSiPkD/hO\n" +
                "Ny/HBxnHR63WvYuS5isqm7XTawim1IVlUn1gph6YuPnkWffRB2pW/A==\n" +
                "-----END RSA PRIVATE KEY-----";
        return certPrivKey;
    }
}


