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
    }

    public int getBlockNumber()
    {
        return blockNumber;
    }

    public String getNextEncodedSection() {

        // adjust buffer length if at end of file;
        if (bufferLength > (fileSize - filePos)) {
            bufferLength = (int) (fileSize - filePos);
            this.eof = true;
        }
        byte[] readBuffer = new byte[bufferLength];

        // read buflen char
        if (fileSize == 0) {
            bufferLength=10;
            readBuffer = "1234567890".getBytes();
        } else {
            try {
                inputStream.read(readBuffer, (int) filePos, bufferLength);  // Potential issue here 2gb
            } catch (java.io.IOException e) {
                this.eof = true;
            }
        }
        blockNumber++;
        filePos += bufferLength;

        // encode and return
        return Base64.encode(readBuffer,0,bufferLength,0).toString();


    }



}
