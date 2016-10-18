package org.dyndns.ecall.ecallsendapi;

import android.content.Context;

import org.dyndns.ecall.ecalldataapi.EcallAlert;

/**
 * Created by bajaques on 4/10/2016.
 */

public class EcallMessageDespatcherViaIOT extends EcallMessageDespatcher {
    EcallIOTConnection connection ;
    String messagePayload ;
    String despatchResult ;
    Context securityContext ;

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

        // could jump out here if the alert is not sent

        connection = new EcallIOTConnection(securityContext);
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
        return false;
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


    public void SetSecurityContext(Context context) {
        this.securityContext = context;
    }
}
