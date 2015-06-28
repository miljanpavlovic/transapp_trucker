package com.transapp.trucker.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;
import android.util.Log;

import com.transapp.trucker.commons.Constants;

import java.io.File;

/**
 * Created by Miljan on 6/29/2015.
 */
public class IOUtil {

    private static final String TAG = "IOUtil";

    private static String signatureDirPath =  Environment.getExternalStorageDirectory() + "/" + Constants.SIGNATURE_DIR + "/";


    public static File getSignatureDir(Context context){
        Log.d(TAG, "getSignatureDir called");
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(Constants.SIGNATURE_DIR, Context.MODE_PRIVATE);
        return directory;
    }

    public static String getSignatureName(){
        Log.d(TAG, "getSignatureName called");
        //prepareDirectory();
        String uniqueId = DateAndTimeUtil.getTodaysDate() + "_" + DateAndTimeUtil.getCurrentTime() + "_" + Math.random();
        String current = uniqueId + ".png";
        Log.d(TAG, "Signature name = " + current);
        return current;
    }

    private static boolean prepareDirectory() {
        try {
            if (IOUtil.createSignatureDir()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private static boolean createSignatureDir() {
        Log.d(TAG, "createSignatureDir called");
        Log.d(TAG, "signature dir path = " + signatureDirPath);
        File tempdir = new File(signatureDirPath);
        if (!tempdir.exists())
            tempdir.mkdirs();

        if (tempdir.isDirectory()) {
            File[] files = tempdir.listFiles();
            for (File file : files) {
                if (!file.delete()) {
                    Log.d(TAG , "Failed to delete " + file);
                }
            }
        }
        return (tempdir.isDirectory());
    }


}
