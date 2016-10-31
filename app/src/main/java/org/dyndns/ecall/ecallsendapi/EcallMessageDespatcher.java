package org.dyndns.ecall.ecallsendapi;

import android.util.Log;

import org.dyndns.ecall.ecalldataapi.EcallAlert;
import org.dyndns.ecall.ecalldataapi.EcallContact;

/**
 * Created by bajaques on 4/10/2016.
 */

public abstract class EcallMessageDespatcher {
    private EcallAlert alert ;



    protected void InitStructures(EcallAlert alert ) {
        this.alert = alert;
    }


    public EcallAlert getAlert() {

        return this.alert;
    }

    public EcallContact getContact() {

        return this.alert.getContact();
    }


    public void sendMessage() throws EcallSendException
    {
        try {
            Log.d("DEBUG","Doing depatching");
            this.connectToService();
            this.prepareMessage();
            this.despatchMessage();
        }
        catch ( Exception e) {

        }
    }

    public abstract String getDespatchTypeDescriptor() ;

    /* connect to destination service */
    public abstract Boolean connectToService ();

    /* get message ready for depatch */
    public abstract Boolean prepareMessage ();

    /* send the message  */
    public abstract Boolean despatchMessage ();

    /* get depatch result */
    public abstract Boolean deliveryComplete ();
    public abstract String getDespatchResult ();
    public abstract Boolean deliverySuccessful ();


    /*  for async despatchers  - must be implemented anyway   */
    public abstract Boolean doesAsyncDelivery ();



}


