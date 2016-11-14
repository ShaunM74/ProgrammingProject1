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

        // connection and send state
    Boolean isConnected = false;
    Boolean isSent = false;
    String deliveryMessage;


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
        isConnected = false;
        isSent = false;
        deliveryMessage="Unsent";
        EcallRegistration reg = new EcallRegistration(securityContext);
        Log.d("DEBUG","B4 Connect"+reg.getCertID());
        connection = new EcallIOTConnection(securityContext);
        connection.startConnection(reg.getCertID(), "ECALL");
        Log.d("DEBUG","After Connect");

        boolean fini = false;
        String s = "x";
        int count = 0;

        connection.startConnectionStatusHandler(reg.getCertID(), "ECALL");
        while (!fini) {
            s = connection.getStatus();
            fini = false;
            if (s.equalsIgnoreCase("Connected")) {
                Log.d("DEBUG","Connected:"+s);
                isConnected = true;
                fini = true;
            }
            if (count++ > 60)
                fini = true;

            if (!fini) {
                Log.d("DEBUG",""+count);
                try {
                    Thread.sleep(1000);
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
        isSent = false;
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
            deliveryMessage="Field issue - Base fields";
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

        return true;
    }

    @Override
    /* send the message  */
    public  Boolean despatchMessage ()
    {


            // Publish the alert itself
        String s = alertJSON.toString();
        connection.publishData("EcallNewAlerts", s);

            // Commit the alert
        connection.publishData("EcallNewAlertsCommit", alertCommitJSON.toString());


            // Check for an attachment
        String fileloc;
        try {
            fileloc = alertJSON.getString("AttachmentLocation");
            fileloc += alertJSON.getString("AttachmentName");
        }
        catch (JSONException e) {
            // no attachment so exit with success
            deliveryMessage="Publish Completed";
            isSent = true;
            return true;
        }

            // Send the attachment
        int blockCount = 0;
        long fileSize =0 ;
        try {

                // Send the attachment 1 block at a time
            EcallIOTEncoder f = new EcallIOTEncoder(fileloc, 60000);
            fileSize = f.fileSize;
            while (!f.atEof()) {
                JSONObject blockJSON = new JSONObject(baseBlockJSON.toString());

                String encodedBuffer = f.getNextEncodedSection();
                int blockLength = f.getLatestBlockLength();

                    // add the specific block data
                blockJSON.put("CRC", "XXX");
                blockJSON.put("BlockData", encodedBuffer);
                blockJSON.put("BlockNumber", blockCount+1); // preemtive add 1 to blockcount, increment if success
                blockJSON.put("BlockLength", blockLength); // preemtive add 1 to blockcount, increment if success

                connection.publishData("EcallNewAttachments", blockJSON.toString());
                Log.d("DEBUG","Sending block "+blockCount);
                blockCount++;
            }

            // Commit the attachments
            try {
                JSONObject finiJSON = new JSONObject(baseBlockJSON.toString());
                finiJSON.put("BlockCount", blockCount);
                finiJSON.put("FileSize", fileSize);
                connection.publishData("EcallNewAttachmentCommit", finiJSON.toString());
                deliveryMessage="PublishAttachment Completed";
                isSent = true;
            } catch (JSONException e) {
                deliveryMessage="PublishAttachment Error";
                return false;
            }
            isSent = true;

        } catch (JSONException e) {
            return false;
        }


        return true;
    }


    @Override
    /* get depatch result */
    public  Boolean deliveryComplete () {
        return this.isSent;
    }

    @Override
    /* get depatch result */
    public  String getDespatchResult () {
        return deliveryMessage;
    }

    @Override
    /* get depatch result */
    public  Boolean deliverySuccessful () {
        return isSent;
    }

    @Override
    /* doesAsync Delivery  */
    public  Boolean doesAsyncDelivery  () {
        return false;
    }


    public void setSecurityContext(Context context) {
        this.securityContext = context;
    }

    public  Boolean getIsConnected () {
        return this.isConnected;
    }
}
