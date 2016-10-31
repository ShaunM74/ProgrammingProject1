package org.dyndns.ecall.ecallsendapi;

import android.content.Context;
import android.util.Log;

import org.dyndns.ecall.ecalldataapi.EcallAlert;

import group4.programmingproject1.R;
import group4.programmingproject1.dataHandler;

/**
 * Created by bajaques on 4/10/2016.
 */

public class EcallMessageDespatcherViaIOT extends EcallMessageDespatcher {
    EcallIOTConnection connection ;
    String messagePayload ;
    String despatchResult ;
    Context securityContext ;
    boolean deliveryCompleteFlag=false;

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

        //EcallIOTConnection conn = new EcallIOTConnection(getBaseContext(), this);
        try {
            connection.setCertificatePrivateKey(dataHandler.getCertID(securityContext),
                    dataHandler.getCertPEM(securityContext),
                    dataHandler.getPubKey(securityContext),
                    dataHandler.getPrivKey(securityContext), "ECall");
        } catch (Exception e) {

            if (!e.getCause().getLocalizedMessage().contains("key store already has a key entry with alias")) {
                throw e;
            }
        }


        // could jump out here if the alert is not sent


        // create a new iot connection

        return false;
    }

    @Override
    /* get message ready for depatch */
    public  Boolean prepareMessage () {
        messagePayload = this.getAlert().getPayload();

        return true;
    }

    @Override
    /* send the message  */
    public  Boolean despatchMessage ()
    {
        connection.publishData(messagePayload);
        Log.d("DEBUG","Depatched message");
        connection.close();
        deliveryCompleteFlag=true;
        return false;
    }


    @Override
    /* get depatch result */
    public  Boolean deliveryComplete () {
        return deliveryComplete();
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


    public void SetSecurityContext(Context context) {
        this.securityContext = context;
    }
}
