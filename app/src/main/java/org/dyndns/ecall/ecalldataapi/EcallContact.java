package org.dyndns.ecall.ecalldataapi;

/**
 * Created by bajaques on 4/10/2016.
 */

public class EcallContact {

    private String contactID;
    private String displayName;
    private String emailAddress;
    private String phoneNumber;


        // create ecall contact from the contact ID
    public EcallContact(String _ID)
    {
        contactID = _ID;
        // retrieve the contact

    }

    public String getDisplayName() {

        return null;
    }
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getEmailAddress() {
        // at present retrieve the email from the contact

        // code to get the email
        return emailAddress;
    }

    public void setEmailAddress(String email)
    {
        this.emailAddress=emailAddress;

    }

    public String getPhoneNumber()
    {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phone)
    {
        this.phoneNumber=phoneNumber;

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
