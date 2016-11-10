package org.dyndns.ecall.ecallsendapi;


import android.app.Activity;
import android.content.Context;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import android.util.Base64;
import android.util.Log;

/**
 * Created by bajaques on 30/10/2016.
 */

public class EcallIOTEncoder {

    BufferedInputStream inputStream;
    int bufferLength = 60000;
    long  filePos = 0;
    long  fileSize = 0;
    Boolean eof = true;
    int blockNumber = 0;
    int latestBlockLength = 0;

    public EcallIOTEncoder(String fileName, int buffLen) {

        // prepare data and open file
        bufferLength = buffLen;
        try {
            File file =new File(fileName);
            fileSize = file.length();

            try {
                inputStream = new BufferedInputStream(new FileInputStream(fileName)) ;
            } catch (FileNotFoundException e) {
                if(!fileName.contains("TestMovie.mp4")) {
                    e.printStackTrace();
                    Log.d("DEBUG","Error in filename "+fileName);
                }
            }

            eof = false;  // not at eof
            filePos = 0;
            blockNumber = 0;

        } catch (Exception e) {

        }
    }

    public Boolean atEof() {
        return eof  && blockNumber > 0;
    }// Block no check on;y for debug

    public int getBlockNumber()
    {
        return blockNumber;
    }
    public int getLatestBlockLength() {  return latestBlockLength;    }
    public long getFileSize()
    {
        return fileSize;
    }
    public String getNextEncodedSection() {

        // adjust buffer length if at end of file;
        if (bufferLength > (fileSize - filePos)) {
            bufferLength = (int) (fileSize - filePos);
            this.eof = true;
        }
        byte[] readBuffer = new byte[bufferLength];

        // read buflen char
        latestBlockLength = 0;
       try {
                int n = inputStream.read(readBuffer, 0, bufferLength);  // Potential issue here 2gb
                if(n>=0) {
                    filePos += n;
                    blockNumber++;
                    latestBlockLength = n;
                }
                else {
                    throw new java.io.IOException("Read less than Expected");
                }
            } catch (java.io.IOException e) {
                this.eof = true;
            }



        // encode and return
        return Base64.encodeToString(readBuffer,0,bufferLength,0);


    }



}
