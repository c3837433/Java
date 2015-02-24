package com.example.angessmith.goodfeed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

 // Created by AngeSSmith on 12/8/14.
// Custom class for recurring camera methods
public class HelperMethods {
    public static final String TAG = "HelperMethods";

    public Uri createImageUri() {
        String imageName = new SimpleDateFormat("MMddyyyy_HHmmss").format(new Date(System.currentTimeMillis()));
        // Check for sd card
        boolean hasSdCard = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED
        );
        if (hasSdCard) {
            // Create the directory or get it
            File meAppDirectory = new File(Environment.getExternalStoragePublicDirectory (
                    Environment.DIRECTORY_PICTURES), "GoodFeed");
            // if the directory doesn't exist, create one
            if (! meAppDirectory.exists()){
                if (! meAppDirectory.mkdirs()){
                    Log.d(TAG, "Creating new URI, unable to access the directory");
                    return null;
                }
            }
            // create a new image file
            File newImage = new File(meAppDirectory.getPath() + File.separator +
                    "img" + imageName + ".jpg");
            try {
                // try to create the new image file
                newImage.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return Uri.fromFile(newImage);
        } else {
            return null;
        }
    }


    // Make a smaller bitmap of the returned image
    public Bitmap createThumbnailOfImage(Uri uri) {
        // no thumbnail, get the image sizes
        BitmapFactory.Options imageOptions = new BitmapFactory.Options();
        imageOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(uri.getPath(), imageOptions);
        int imageWidth = imageOptions.outWidth;
        int imageHeight = imageOptions.outHeight;
        // Create a thumbnail of the image
        return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(uri.getEncodedPath()), imageWidth, imageHeight);
    }
}
