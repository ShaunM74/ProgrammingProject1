package org.dyndns.ecall.ecalldataapi;

/**
 * Created by bajaques on 4/10/2016.
 */





public class EcallAlert {

    private EcallContact contact;
    private EcallAlert.alertStatusEnum alertStatus;
    private EcallAlert.alertMethodEnum alertMethod;
    private String jsonPayload ;


    public enum alertMethodEnum { EMAIL , SMS }
    public enum alertStatusEnum { UNSENT , PENDING , CONNECTING, SENDING , FAILED, SENT, UPLOADED }


    private void initialiseSettings() {
        alertStatus = alertStatusEnum.UNSENT ;

    }
        // constructor - an alert must have a contact and method
    public EcallAlert(EcallContact contact, EcallAlert.alertMethodEnum method, String payload)
        throws EcallDataException
    {
        initialiseSettings();
        this.contact = contact;
        this.setAlertMethod(method);
        this.setPayload(payload);
    }



    public EcallAlert.alertMethodEnum getAlertMethod()
    {
        return (this.alertMethod) ;
    }
    public  void setAlertMethod(EcallAlert.alertMethodEnum method )
    {
        this.alertMethod = method;
    }

    public EcallContact getContact()
    {
        return contact;
    }




    // the payload for the alert consists of a JSON string with the
        // alert data as defined in the
    public String getPayload()
    {
        return this.jsonPayload;
    }
    public void setPayload (String payload) throws EcallDataException
    {
        // can implement sanity checks here
        // to ensure required data is in the payload and raise exeption if not

        // also add any auto or calculated  fields here
        try {
            getPayloadDataValue("alertID");
        }
        catch(EcallDataException e)
        {
            addPayloadPair("alertID", "yyyymmdd");
        }


        this.jsonPayload = payload;
    }
        // alow data value pairs to be added
    public void addPayloadPair( String key, String value)
    {

    }
    public String getPayloadDataValue(String key) throws EcallDataException
    {
        return null;
    }

    public void setStatus(alertStatusEnum newStatus)
    {
        alertStatus = newStatus;
    }
    public Boolean isSent()
    {
        return((alertStatus == alertStatusEnum.SENT)  || (alertStatus == alertStatusEnum.UPLOADED));
    }
    public Boolean isUploaded()
    {
        return(alertStatus == alertStatusEnum.UPLOADED) ;

    }




        // return fields that are available to be used with data
        // this should be all the data required to reconstitute the alert
        // so this string can be used to save pending alerts.
    public String toJson(){
        return null;
    }
        // should only be called by the processor to create an alert from data.
    public void populateFromJson(){

    }


        // converter code to keep compatible types
        // may need to consider moving to external class
    public String encodeBlob(byte[] data)
    {
        // possibly do this in base64 to keep in line with php standrd
        return null;
    }
}
