package org.dyndns.ecall.ecallsendapi;

import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import android.content.Context;

import org.dyndns.ecall.ecalldataapi.EcallAlert;
import org.json.JSONObject;

/**
 * Created by bajaques on 4/10/2016.
 */

public class EcallMessageDespatcherViaSMS extends EcallMessageDespatcher {

    private String phoneNo=null;
    private String message=null;

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
            return true;
        }

        @Override
    /* get message ready for depatch */
        public  Boolean prepareMessage () {

            phoneNo = getContact().getPhone();
            try {
                JSONObject mainObject = new JSONObject(getAlert().getPayload());
                message =mainObject.getString("Message") + " Lat:"+mainObject.getString("Latitude")+
                        " Long:"+mainObject.getString("Longitude")+" Date:"+mainObject.getString("Date")+
                        " Time:"+mainObject.getString("Time");

            }
            catch(Exception e)
            {
                return false;
            }
            return true;
        }

        @Override
    /* send the message  */
        public  Boolean despatchMessage ()
        {
            Log.i("Send SMS", "");


            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, message, null, null);
            }

            catch (Exception e) {

                e.printStackTrace();
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



}
