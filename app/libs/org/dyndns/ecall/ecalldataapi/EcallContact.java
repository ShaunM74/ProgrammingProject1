package org.dyndns.ecall.ecalldataapi;

/**
 * Created by bajaques on 4/10/2016.
 */

public class EcallContact {

    private String contactSystem; // source of the data - at present the Contact ID on android
    private String contactID;

        // create ecall contact from the contact ID
    public EcallContact(String _ID) throws EcallDataException
    {
        contactID = _ID;

            // retrieve the contact

    }

    public String getDisplayName() {

        return null;
    }

    public String getEmailAddress() {
        // at present retrieve the email from the contact

        // code to get the email
        return ("a@example.com");
    }

        // get / set the field to be used to contact the recipient
        // this is the fieldname in the Payload Data - eg FaxNumber
    public String  getContactAddressField()
    {

        return null;
    }
    public void setContactAddressField(String fieldName)
    {

    }
        // return fields that are available to be used with data
    public String toJson(){

        return null;
    }


}
