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
    private boolean deliveryCompleteFlag=false;

    public EcallMessageDespatcherViaSMS(EcallAlert alert)
    {
        this.InitStructures(alert);
        phoneNo = alert.getContact().getPhoneNumber().toString();
                //.replaceAll("[^\\d]", "");
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


            try {
                JSONObject mainObject = new JSONObject(getAlert().getPayload());
                message =""+mainObject.getString("MessageText");
                try {
                    message+=mainObject.getString("Location");
                }
                catch(Exception e) {
                    try {
                        message += " Lat:" + mainObject.getString("Latitude") +
                                " Long:" + mainObject.getString("Longitude") + ". ";

                    } catch (Exception e2) {
                        //No lat or long in JSON string
                        // Generally if GPS co-ordinates are set to off.
                    }
                }
                message +=" Date:"+mainObject.getString("Date")+
                        " Time:"+mainObject.getString("Time")+" "+mainObject.getString("Website");
                Log.d("DEBUG",message);
            }
            catch(Exception e)
            {
                Log.d("DEBUG","Message creation failed"+e.toString());
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
                Log.d("DEBUG","SMS sent to "+phoneNo +":"+message);
                this.deliveryCompleteFlag=true;
            }

            catch (Exception e) {
                Log.d("DEBUG",e.getMessage());
                e.printStackTrace();
                return false;
            }


            return true;
        }


        @Override
    /* get depatch result */
        public  Boolean deliveryComplete () {
            return deliveryCompleteFlag;
        }

        @Override
    /* get depatch result */
        public  String getDespatchResult () {
            return null;
        }

        @Override
    /* get depatch result */
        public  Boolean deliverySuccessful () {
            return deliveryCompleteFlag;
        }

        @Override
    /* doesAsync Delivery  */
        public  Boolean doesAsyncDelivery  () {
            return false;
        }



}
