package org.dyndns.ecall.ecallsendapi;

import android.content.Context;
import android.util.Log;

import org.dyndns.ecall.ecalldataapi.EcallAlert;
import org.dyndns.ecall.ecalldataapi.EcallRegistration;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bajaques on 4/10/2016.
 */

public class EcallMessageDespatcherViaIOT extends EcallMessageDespatcher {
    EcallIOTConnection connection ;
    String messagePayload ;
    String despatchResult ;
    Context securityContext ;
    String alertTopic = "EcallNewAlerts" ;
    String attachmentTopic = "EcallNewAttachment" ;
    JSONObject alertJSON;
    JSONObject alertCommitJSON;
    JSONObject baseBlockJSON;


    public JSONObject getAlertJSON() {
        return alertJSON;
    }

    public EcallMessageDespatcherViaIOT(EcallAlert alert )
    {
        this.InitStructures(alert );

    }
    @Override
    public String getDespatchTypeDescriptor ()
    {
        return ( "IOT");
    }

    @Override
    /* connect to destination service */
    public Boolean connectToService ()
    {
        EcallRegistration reg = new EcallRegistration(securityContext);
        reg.setCertificatePrivateKey();
        connection = new EcallIOTConnection(securityContext);
        connection.startConnection(reg.getCertID(), "ECALL");

        boolean fini = false;
        String s = "x";
        int count = 0;

        try {
            connection.startConnectionStatusHandler(reg.getCertID(), "ECALL");
        }
        catch (Exception e)
        {
            Log.d("DEBUG","Service "+e.toString());
        }
        while (!fini) {
            s = connection.getStatus();
            fini = false;
            if (s.equalsIgnoreCase("Connected")) {
                fini = true;
                Log.d("DEBUG","Connected service");
            }
            if (count++ > 60) {
                fini = true;
                Log.d("DEBUG","Connecting service "+s +":"+count);
            }

            if (!fini) {

                try {
                    Log.d("DEBUG","Waiting");
                    wait(5000);
                } catch (Exception e) {
                }

            }

        }

        // create a new iot connection

        return true;
    }

    @Override
    /* get message ready for depatch */
    public  Boolean prepareMessage () {
        try {
            alertJSON = new JSONObject(this.getAlert().getPayload());
        }
        catch(Exception e)
        {

        }
        try {
            alertCommitJSON = new JSONObject();
            alertCommitJSON.put("AlertID", alertJSON.getString("AlertID"));
            alertCommitJSON.put("AccountID", alertJSON.getString("AccountID"));
        }
        catch(Exception e)
        {
            String s = e.getMessage();

        }

        try {
            baseBlockJSON = new JSONObject();
            baseBlockJSON.put("AlertID", alertJSON.getString("AlertID"));
            baseBlockJSON.put("DeviceID", alertJSON.getString("DeviceID"));
            baseBlockJSON.put("AttachmentName",alertJSON.getString("AttachmentName"));
        }
        catch(Exception e)
        {

        }
        Log.d("DEBUG","Preparing message");
        return true;
    }

    @Override
    /* send the message  */
    public  Boolean despatchMessage ()
    {


        // Publish the alert itself
        String s = alertJSON.toString();
        connection.publishData("EcallNewAlerts", s);
        Log.d("DEBUG","Despatching"+s);
        // Commit the alert
        connection.publishData("EcallNewAlertsCommit", alertCommitJSON.toString());

        Log.d("DEBUG","Commiting");
        // Check for an attachment
        String fileloc;
        try {
            fileloc = alertJSON.getString("AttachmentLocation");
            Log.d("DEBUG","Attaching");
        }
        catch (JSONException e) {
            // no attachment so exit with success
            Log.d("DEBUG","No Attachment");
            return true;
        }

        // Send the attachment
        int blockCount = 0;
        try {

            // Send the attachment 1 block at a time
            EcallIOTEncoder f = new EcallIOTEncoder(fileloc, 60000);
            while (!f.atEof()) {
                JSONObject blockJSON = new JSONObject(baseBlockJSON.toString());
                String encodedBuffer = f.getNextEncodedSection();
                // add the specific block data
                blockJSON.put("CRC", "XXX");
                blockJSON.put("BlockData", encodedBuffer);
                blockJSON.put("BlockNumber", blockCount+1); // add 1 to blockcount, increment if success
                connection.publishData("EcallNewAttachments", blockJSON.toString());
                blockCount++;
            }

            // Commit the attachments
            try {
                JSONObject finiJSON = new JSONObject(baseBlockJSON.toString());
                finiJSON.put("BlockCount", blockCount);
                connection.publishData("EcallNewAttachmentCommit", finiJSON.toString());
            } catch (JSONException e) {
                return false;
            }

        } catch (JSONException e) {
            return false;
        }


        return true;
    }


    @Override
    /* get depatch result */
    public  Boolean deliveryComplete () {
        return false;
    }

    @Override
    /* get depatch result */
    public  String getDespatchResult () {
        return null;
    }

    @Override
    /* get depatch result */
    public  Boolean deliverySuccessful () {
        return false;
    }

    @Override
    /* doesAsync Delivery  */
    public  Boolean doesAsyncDelivery  () {
        return false;
    }


    public void setSecurityContext(Context context) {
        this.securityContext = context;
    }
}