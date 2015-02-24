package com.example.angessmith.mappingapplication;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

 // Created by AngeSSmith on 11/15/14.

public class StoredImageData {


    public static ArrayList<ImageObject> getStoredImages (Context context) {
        
        ArrayList<ImageObject> arrayList = new ArrayList<ImageObject>();
        // Get the list
        File external = context.getExternalFilesDir(null);
        boolean fileExists = new File(external, MapViewActivity.IMAGE_FILE_NAME).exists();
        if (fileExists) {
            try {
                // Get the file
                File file = new File(external, MapViewActivity.IMAGE_FILE_NAME);
                // create the input streams
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                arrayList = (ArrayList<ImageObject>) ois.readObject();
                ois.close();
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (StreamCorruptedException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }
}
