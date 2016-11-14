package org.dyndns.ecall.ecallsendapi;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.security.KeyStore;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;



import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttLastWillAndTestament;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttNewMessageCallback;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos;
import com.amazonaws.mobileconnectors.iot.AWSIotKeystoreHelper;




import java.util.UUID;

public class EcallIOTConnection
{
    static final String LOG_TAG = EcallIOTConnection.class.getCanonicalName();
    /**
     * Created by bajaques on 1/10/2016.
     */

    // State Variables

    private String Status="Initialising" ;
    private Context context ;
    //private Activity activity;
    private String certificate;


    // connection Defaults
    //"a1chund80153hz.iot.us-west-2.amazonaws.com";

    private String CUSTOMER_SPECIFIC_ENDPOINT =   "a1chund80153hz.iot.us-west-2.amazonaws.com";
    private Regions MY_REGION = Regions.US_WEST_2;



    // connection variables
    AWSIotMqttManager mqttManager;
    String clientId;

    KeyStore clientKeyStore = null;


    // instantiation using the super class (Activity) to pass context
    public EcallIOTConnection(Context caller)
    {
       // this.activity = activity;
        context = caller;
    }

    //setters to set connection info - certificates etc
    public void setEndpoint (String endpoint) {
        CUSTOMER_SPECIFIC_ENDPOINT=endpoint;
    }
    public void setCertificatePrivateKey(String id, String certpem,String certpubkey, String privkey, String keystore )
    {
        this.certificate = id;

        String keystorePath = this.context.getFilesDir().getPath();
        String keystoreName = keystore;
        String keystorePassword = "";

                // save the certifictae
        AWSIotKeystoreHelper.saveCertificateAndPrivateKey(id,certpem,
                privkey,
                keystorePath, keystoreName, keystorePassword);


    }


    public void establishMQQTConection (String id, String keystore)
    {
        clientId= UUID.randomUUID().toString();
        Region region = Region.getRegion(MY_REGION);



        mqttManager = new AWSIotMqttManager(clientId, CUSTOMER_SPECIFIC_ENDPOINT);

    }

    public void startConnection(String id, String keystore)
    {
        // MQTT client IDs are required to be unique per AWS IoT account.
        // This UUID is "practically unique" but does not _guarantee_
        // uniqueness.

        // MQTT Client
        establishMQQTConection (id,keystore);

    }
    public String getConnectionStatus() {
        return Status;
    }
    public void setContext(Context newContext)
    {
        context = newContext;
    }
    public void startConnectionStatusHandler(String certid,String keystore) {
      //  final Activity activity1 = (Activity) context;
        try {
            // get the certificate
            String keystorePath = this.context.getFilesDir().getPath();
            String keystoreName = keystore;
            String keystorePassword = "";

            // read the certifictae

            clientKeyStore = AWSIotKeystoreHelper.getIotKeystore(certid,
                    keystorePath, keystoreName, keystorePassword);
        }
        catch(Exception e)
        {
            Log.d("DEBUG",e.getMessage());
        }
        try {
            mqttManager.connect(clientKeyStore, new AWSIotMqttClientStatusCallback() {
                @Override
                public void onStatusChanged(final AWSIotMqttClientStatus status,
                                            final Throwable throwable) {
                    Log.d(LOG_TAG, "Status = " + String.valueOf(status));
                    if (status == AWSIotMqttClientStatus.Connected) {
                        setStatus("Connected");
                        Log.d("DEBUG","connected");
                    }

                    new Runnable() {
                        @Override
                        public void run() {
                            if (status == AWSIotMqttClientStatus.Connecting) {
                                setStatus("Connecting...");

                            } else if (status == AWSIotMqttClientStatus.Connected) {
                                setStatus("Connected");

                            } else if (status == AWSIotMqttClientStatus.Reconnecting) {
                                if (throwable != null) {
                                    Log.e(LOG_TAG, "Connection error.", throwable);
                                }
                                setStatus("Reconnecting");
                            } else if (status == AWSIotMqttClientStatus.ConnectionLost) {
                                if (throwable != null) {
                                    Log.e(LOG_TAG, "Connection error.", throwable);
                                }
                                setStatus("Disconnected");
                            } else {
                                setStatus("Disconnected");

                            }
                        }
                    };
                }
            });
        } catch (final Exception e) {
            Log.e(LOG_TAG, "Connection error.", e);
            setStatus("Error! " + e.getMessage());
        }
    }



    public void publishData(String topic,String data){


        final String msg = data;

        try {
            mqttManager.publishString(msg, topic, AWSIotMqttQos.QOS1);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Publish error.", e);
        }

    };

    public void setStatus(String newstatus)
    {
        this.Status = newstatus;
    }
    public void testStatusUpdate()
        {
            Status=Status;
            int n=mqttManager.getMaxAutoReconnectAttempts();
            n++;
        };

    public String getStatus()
    {

        return Status;
    }
    public void close()
    {
        mqttManager.disconnect();
    };


}
