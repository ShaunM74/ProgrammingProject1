package org.dyndns.ecall.ecallsendapi;

import android.content.Context;
import android.util.Log;

import org.dyndns.ecall.ecalldataapi.EcallAlert;

import java.util.Iterator;
import java.util.List;
import org.dyndns.ecall.ecalldataapi.EcallAlertQueue;

/**
 * Created by bajaques on 4/10/2016.
 */

public class EcallSendProcessor {
    private EcallAlertQueue pendingAlerts;
    private Context securityContext ;


    public EcallSendProcessor()
    {
        pendingAlerts = new EcallAlertQueue();
    }

    public void setSecurityContext(Context context) {
        securityContext = context;
    }

    public void addAlert(EcallAlert alert)
    {
        pendingAlerts.queueAlert(alert);
    }

    private EcallMessageDespatcher createMessageDespatcher(EcallAlert alert)
    {
        switch(alert.getAlertMethod()) {
            case EMAIL:
                return ((EcallMessageDespatcher) new  EcallMessageDespatcherViaEmail(alert));
            case SMS:
                return ((EcallMessageDespatcher) new EcallMessageDespatcherViaSMS(alert));

        }


        return null;
    }




    public void processPendingAlerts() throws EcallSendException
    {

        while (pendingAlerts.getQueueCount() > 0) {

            EcallAlert alert = pendingAlerts.firstAlert();
            EcallMessageDespatcher despatcher ;
            EcallMessageDespatcherViaIOT iotDespatcher ;


            // send the alert data using appropriate method


            if(!alert.isSent()) {
                despatcher = createMessageDespatcher(alert);
                despatcher.sendMessage() ;
                while(!despatcher.deliveryComplete()) {
                    // yield or something - do we really need to implement call back ?
                    // alternatively, do we keep  a list of despatchers working asynchronously
                    // much easier to assume at this point that delivery will be synchronous
                    // the pending alerts queue allows for failure and resending
                    // we could mark as pending, but the lifertime of the despatcher objects
                    // will need to be allowed for
                    // we'd need to add to a list to be purged as they completed
                }
                // if delivered note the fact so it is not resent if the upload fails
                if(despatcher.deliveryComplete()) {
                    if (despatcher.deliverySuccessful()) {
                        alert.setStatus(EcallAlert.alertStatusEnum.SENT); // mark the alert as sent in case the
                    }
                    else
                        alert.setStatus(EcallAlert.alertStatusEnum.FAILED); // mark the alert as sent in case the
                }

                // if the message has been sent then upload to the backend
                // note if this message has sent ,but the upload has faled then
                // the alert will stay in the queue and come back to here to re upload
                if(alert.isSent()) {
                    if (!alert.isUploaded()) {
                        iotDespatcher = new EcallMessageDespatcherViaIOT(alert);
                        // note we may have to fiddle here to make sure the despatcher doesnt
                        // get disposed of before completion

                        iotDespatcher.setSecurityContext(securityContext);
                        iotDespatcher.sendMessage();
                        alert.setStatus(EcallAlert.alertStatusEnum.UPLOADED);

                    }
                }
                if(alert.isUploaded()) {
                    pendingAlerts.dropAlert(alert);  // check this doesn't stuff up the iterator.
                    try {
                        Thread.sleep(1000);
                    }
                    catch(Exception e)
                    {

                    }
                }
                pendingAlerts.saveAlerts() ;   //update the saved data so that app can restore if required
                // if this creates a performance issue, move outside the
                // block
            }

        }
    }





}