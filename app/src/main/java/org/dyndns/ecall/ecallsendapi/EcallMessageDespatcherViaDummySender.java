package org.dyndns.ecall.ecallsendapi;

import org.dyndns.ecall.ecalldataapi.EcallAlert;

/**
 * Created by bajaques on 3/11/2016.
 */

public class EcallMessageDespatcherViaDummySender extends EcallMessageDespatcher {


    /**
     * Dummy sender for use with items that don't need to be sent, but should be logged to the db
     */


        public EcallMessageDespatcherViaDummySender(EcallAlert alert)
        {
            this.InitStructures(alert);
        }

        @Override
        public String getDespatchTypeDescriptor ()
        {
            return ( "DUMMY");
        }



        @Override
    /* connect to destination service */

        public Boolean connectToService ()
        {
            return true;
        }

        @Override
    /* get message ready for depatch */
        public  Boolean prepareMessage () {
            return true;
        }

        @Override
    /* send the message  */
        public  Boolean despatchMessage ()
        {
            return true;
        }


        @Override
    /* get depatch result */
        public  Boolean deliveryComplete () {
            return true;
        }

        @Override
    /* get depatch result */
        public  String getDespatchResult () {
            return "DummySenderOK";
        }

        @Override
    /* get depatch result */
        public  Boolean deliverySuccessful () {
            return true;
        }

        @Override
    /* doesAsync Delivery  */
        public  Boolean doesAsyncDelivery  () {
            return false;
        }







}
