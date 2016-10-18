package org.dyndns.ecall.ecallsendapi;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

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

    // connection Defaults
    private String CUSTOMER_SPECIFIC_ENDPOINT = "a1chund80153hz.iot.us-west-2.amazonaws.com";
    // Cognito pool ID. For this app, pool needs to be unauthenticated pool with
    // AWS IoT permissions.
    private String COGNITO_POOL_ID = "us-west-2:669ac520-1850-46b4-8e87-497f4a43f734";
    private Regions MY_REGION = Regions.US_WEST_2;
    private String ecallMQTTTopic ="$aws/things/test01-testDevice01/shadow/update" ;

    // connection variables
    AWSIotMqttManager mqttManager;
    String clientId;

    AWSCredentials awsCredentials;
    CognitoCachingCredentialsProvider credentialsProvider;

    // instantiation using the super class (Activuty) to pass context
    public EcallIOTConnection(Context caller)
    {
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
    public void setCertificatePrivateKey(String key)
    {

    }


    public void establishCognitoConnection() {

        final Activity activity = (Activity) context;

        credentialsProvider = new CognitoCachingCredentialsProvider(
                (Context) activity , // context
                COGNITO_POOL_ID, // Identity Pool ID
                MY_REGION // Region
        );
        // The following block uses a Cognito credentials provider for authentication with AWS IoT.
        new Thread(new Runnable() {
            @Override
            public void run() {
                awsCredentials = credentialsProvider.getCredentials();

                ((Activity) activity).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // event - Credentials Completed
                        CognitoCredentialsEstablished=true;
                    }
                });
            }
        }).start();

    }
    public void establishMQQTConection ()
    {
        clientId= UUID.randomUUID().toString();
        Region region = Region.getRegion(MY_REGION);
        mqttManager = new AWSIotMqttManager(clientId, CUSTOMER_SPECIFIC_ENDPOINT);

    }

    public void Connect()
    {
        // MQTT client IDs are required to be unique per AWS IoT account.
        // This UUID is "practically unique" but does not _guarantee_
        // uniqueness.
        //clientId = ;

        // Initialize the AWS Cognito credentials provider
        establishCognitoConnection ();

        // MQTT Client
        establishMQQTConection ();

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
    public void startConnectionStatusHandler() {
        final Activity activity = (Activity) context;

        try {

            mqttManager.connect(credentialsProvider, new AWSIotMqttClientStatusCallback() {
                @Override
                public void onStatusChanged(final AWSIotMqttClientStatus status,
                                            final Throwable throwable) {
                    Log.d(LOG_TAG, "Status = " + String.valueOf(status));

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (status == AWSIotMqttClientStatus.Connecting) {
                                Status = "Connecting...";

                            } else if (status == AWSIotMqttClientStatus.Connected) {
                                Status = ("Connected");

                            } else if (status == AWSIotMqttClientStatus.Reconnecting) {
                                if (throwable != null) {
                                    Log.e(LOG_TAG, "Connection error.", throwable);
                                }
                                Status = ("Reconnecting");
                            } else if (status == AWSIotMqttClientStatus.ConnectionLost) {
                                if (throwable != null) {
                                    Log.e(LOG_TAG, "Connection error.", throwable);
                                    throwable.printStackTrace();
                                }
                                Status = ("Disconnected");
                            } else {
                                Status = ("Disconnected");

                            }
                        }
                    });
                }
            });
        } catch (final Exception e) {
            Log.e(LOG_TAG, "Connection error.", e);
            Status = ("Error! " + e.getMessage());
        }
    };

    public void publishData(String data){

        final String topic = ecallMQTTTopic ;
        final String msg = data;

        try {
            mqttManager.publishString(msg, topic, AWSIotMqttQos.QOS0);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Publish error.", e);
        }

    };

    public void close()
    {

    };
}
