package org.dyndns.ecall.ecallsendapi;

import org.dyndns.ecall.ecalldataapi.EcallAlert;

/**
 * Created by bajaques on 4/10/2016.
 */

public class EcallMessageDespatcherViaSMS extends EcallMessageDespatcher {

    public EcallMessageDespatcherViaSMS(EcallAlert alert)
    {
        this.InitStructures(alert);


    }

        @Override
        public String getDespatchTypeDescriptor ()
        {
            return ( "SMS");
        }

        @Override
    /* connect to destination service */

        public Boolean connectToService ()
        {
            return false;
        }

        @Override
    /* get message ready for depatch */
        public  Boolean prepareMessage () {
            return false;
        }

        @Override
    /* send the message  */
        public  Boolean despatchMessage ()
        {
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



}
