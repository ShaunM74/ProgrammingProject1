package org.dyndns.ecall.ecalldataapi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by s3221035 on 5/10/16.
 */

public class EcallAlertQueue {
    private List<EcallAlert> alertQueue;


        // will need to consider contention issues here
        // mey be best splitting to 2 classes - queueWriter and queueReader
        // in the short term if running in one process, this won't be an issue.

        // if the processes are split then we will need to modify the way this works and possibly write through a file.


    public EcallAlertQueue() {
        // initialise the queue
        alertQueue = new ArrayList<EcallAlert>();
        loadAlerts();
    }

    public int getQueueCount()
    {
        return alertQueue.size();

    }

    public void queueAlert(EcallAlert item){

        saveAlerts();
    }
    public EcallAlert firstAlert(){
        return null;
    }
    public void dropAlert(EcallAlert item){
        saveAlerts();
    }

    // save any pending alerts
    public void saveAlerts() {
        Iterator<EcallAlert> saveIterator = alertQueue.iterator();
        while (saveIterator.hasNext()) {

            // write out this alert
            System.out.println(saveIterator.next());
        }
    }

    // recover any pending alerts
    public void loadAlerts()
    {

    }

}
