package org.dyndns.ecall.ecalldataapi;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.amazonaws.mobileconnectors.iot.AWSIotKeystoreHelper;

import org.json.JSONException;
import org.json.JSONObject;

import group4.programmingproject1.dataHandler;

/**
 * Created by bajaques on 31/10/2016.
 */

public class EcallRegistration {
    String certificate;
    String keystoreName = "ECALL" ;
    private Context context ;
   // private Activity activity;

    String certARN = "arn:aws:iot:us-west-2:665849750025:cert/f45f2a3a9dae58f3455d49addc08a4c125b47d52a2178ef97e61bad54f4b23df";

    String certID = "f45f2a3a9dae58f3455d49addc08a4c125b47d52a2178ef97e61bad54f4b23df";
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

    String certPubKey = "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlNJdqGP6MzzT3XlIiGrW\n" +
            "zY4wnGKZsdtmLzo9XvYDd/uS8kwLy/BeNTx7klcgBsmQtTV1OeIK3OTZf1Zm/3m4\n" +
            "zqBdoexMeGy5ms2BpX5J75fOZTBx/5IEWMsgXOrTm2Z0BEU0Vj6TmLtOa24+EVky\n" +
            "QiELiUy5F0NU+drxMSRSXTV6MEPRJHsHi35pvBdjIWKykN6/JCFkMwSpSxRWP785\n" +
            "yTozZk2TNtv6l+neyrjKYYhMsLuxf2uZ0DKkhsUTHLjN80UKescKq0FwLXXO9pVy\n" +
            "W40+RxXVc4tLjmZjBzka8fHCHHTLM3qYOpfOZDggGLmGHXFXAU/GNZd0ridWlfMF\n" +
            "OQIDAQAB\n" +
            "-----END PUBLIC KEY-----\n";

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

    // instantiation using the super class (Activity) to pass context
    public EcallRegistration(Context caller)
    {
        context = caller;

        // load the certid from the settings

    }


    public String getCertID()
    {
        return this.certID;
    }

    public EcallRegistration(Context caller,String registrationResponse)
    {
       // this.activity = activity;
        context = caller;

        JSONObject json;
        try {
            json = new JSONObject(registrationResponse);
            certARN=json.getString("CertificateARN");
            certID=json.getString("CertificateOD");
            certARN=json.getString("CertificateARN");
            certPEM=json.getString("CertificatePEM");
            certPubKey = json.getString("CertificatePublicKey");
            certPrivKey = json.getString("CertificatePrivateKey");
            String deviceID = json.getString("DeviceID");

            dataHandler.setCertID(context,certID);
            dataHandler.setDeviceID(context,deviceID);

        }
        catch(JSONException e){};


    }
    public void setCertificatePrivateKey( )
    {
        this.certificate = certID;

        String keystorePath=null;
        String keystorePassword=null;
        try {
            keystorePath = context.getFilesDir().getPath();
            keystorePassword = "";


            // save the certifictae
            AWSIotKeystoreHelper.saveCertificateAndPrivateKey(certID,certPEM,
                    certPrivKey,
                    keystorePath, keystoreName, keystorePassword);
        }
        catch (Exception e)
        {
            Log.d("DEBUG",e.getMessage().toString());
        }



    }





}
