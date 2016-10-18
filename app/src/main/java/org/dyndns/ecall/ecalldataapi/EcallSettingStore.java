package org.dyndns.ecall.ecalldataapi;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by bajaques on 4/10/2016.
 */

public class EcallSettingStore {

    HashMap<String,String> settingHashmap;

    public EcallSettingStore()
    {
        settingHashmap = new HashMap<String,String>();
        this.loadSettings();

    }



    public String getSettingValue(String settingName)
    {
        return settingHashmap.get(settingName);
    }
        // update or insert setting
    public void setSettingValue(String settingName,String value)
    {
        settingHashmap.put(settingName,value);
    }


    // Load Settings
    public void loadSettings()
    {

    }

    // save settings
    public void saveSettings() {

    }

}
