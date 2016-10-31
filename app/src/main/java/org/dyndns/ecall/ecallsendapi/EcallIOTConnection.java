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

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;

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
    private boolean CognitoCredentialsEstablished = false;
    private boolean ConnectionStatusHandlerStarted = false;
    private String Status="Initialising" ;
    private Context context ;
    private Activity activity;
    private String certificate;


    // connection Defaults
    //"a1chund80153hz.iot.us-west-2.amazonaws.com";

    private String CUSTOMER_SPECIFIC_ENDPOINT =   "a1chund80153hz.iot.us-west-2.amazonaws.com";
    //private String CUSTOMER_SPECIFIC_ENDPOINT =   "665849750025.iot.us-west-2.amazonaws.com";
    private Regions MY_REGION = Regions.US_WEST_2;

    // Cognito pool ID. For this app, pool needs to be unauthenticated pool with
    // AWS IoT permissions.
    private String COGNITO_POOL_ID = "us-west-2:669ac520-1850-46b4-8e87-497f4a43f734";
    private String ecallMQTTTopic ="uploads/test01-testDevice01" ;


    // connection variables
    AWSIotMqttManager mqttManager;
    String clientId;

    AWSCredentials awsCredentials;
    CognitoCachingCredentialsProvider credentialsProvider;
    KeyStore clientKeyStore = null;


    // instantiation using the super class (Activity) to pass context
    public EcallIOTConnection(Context caller, Activity activity)
    {
        this.activity = activity;
        context = caller;
    }

    // wrapper for context


    //setters to set connection info - certificates etc
    public void setEndpoint (String endpoint) {
        CUSTOMER_SPECIFIC_ENDPOINT=endpoint;
    }
    public void setPoolID (String poolID) {
        COGNITO_POOL_ID=poolID;
    }
    public void setTopic(String topic){
        ecallMQTTTopic = topic;
    }
    public void setCertificate(String cert){

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


    public void establishCognitoConnection() {


      //  credentialsProvider = new CognitoCachingCredentialsProvider(
     //           (Context) this.context , // context
     //           COGNITO_POOL_ID, // Identity Pool ID
      //          MY_REGION // Region
     //   );
        credentialsProvider = new CognitoCachingCredentialsProvider(
                    (Context) this.context ,
                "us-west-2:669ac520-1850-46b4-8e87-497f4a43f734", // Identity Pool ID
                Regions.US_WEST_2 // Region
        );

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
        //clientId = ;

        // Initialize the AWS Cognito credentials provider
        establishCognitoConnection ();

        // MQTT Client
        establishMQQTConection (id,keystore);

        // The following block uses IAM user credentials for authentication with AWS IoT.
        //awsCredentials = new BasicAWSCredentials("ACCESS_KEY_CHANGE_ME", "SECRET_KEY_CHANGE_ME");
        //btnConnect.setEnabled(true);

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

        // get the certificate
        String keystorePath = this.context.getFilesDir().getPath();
        String keystoreName = keystore;
        String keystorePassword = "";

        // read the certifictae

        clientKeyStore = AWSIotKeystoreHelper.getIotKeystore(certid,
                keystorePath, keystoreName, keystorePassword);

        try {
            mqttManager.connect(clientKeyStore, new AWSIotMqttClientStatusCallback() {
                @Override
                public void onStatusChanged(final AWSIotMqttClientStatus status,
                                            final Throwable throwable) {
                    Log.d(LOG_TAG, "Status = " + String.valueOf(status));

                    activity.runOnUiThread(new Runnable() {
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
                    });
                }
            });
        } catch (final Exception e) {
            Log.e(LOG_TAG, "Connection error.", e);
            setStatus("Error! " + e.getMessage());
        }
    }



    public void publishData(String data){

        final String topic = ecallMQTTTopic ;
        final String msg = data;

        try {
            mqttManager.publishString(msg, topic, AWSIotMqttQos.QOS0);
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
