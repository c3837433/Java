package com.example.angessmith.mappingapplication;

// Created by AngeSSmith on 11/15/14.

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.angessmith.mappingapplication.Fragment.AddImageFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class AddMapImageActivity extends Activity implements AddImageFragment.OnAddImageListener{

    // define properties
    public static final String TAG = "AddMapImageActivity.TAG";
    int REQUEST_TAKE_PICTURE = 34681315;
    Uri mImageUri;
    Double mLatitude;
    Double mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_map_image);
        Intent intent = getIntent();
        if (intent.hasExtra(MapViewActivity.EXTRA_LATITUDE)) {

            mLatitude = intent.getDoubleExtra(MapViewActivity.EXTRA_LATITUDE, 44.91064173539736);
            mLongitude = intent.getDoubleExtra(MapViewActivity.EXTRA_LONGITUDE, -93.20945117622614);
        }
        if (savedInstanceState == null) {
            // Add the map
            AddImageFragment fragment = new AddImageFragment();
            getFragmentManager().beginTransaction().replace(R.id.add_image_container, fragment).commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_map_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_save_image) {
            // save the current item
            saveNewImageToList();
        }
        return super.onOptionsItemSelected(item);
    }


    private Uri getNewImageUri () {
        String imageName = new SimpleDateFormat("MMddyyyy_HHmmss").format(new Date(System.currentTimeMillis()));
        // Make sure we have an sd card mounted
        boolean sdIsMounted = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED
        );
         if (sdIsMounted) {
             // Create the directory or get it
             File meAppDirectory = new File(Environment.getExternalStoragePublicDirectory (
                     Environment.DIRECTORY_PICTURES), "MappingApplication");
             // See if the directory already exists
             if (! meAppDirectory.exists()){
                 // if not, create it
                 if (! meAppDirectory.mkdirs()){
                     // if it can't create it
                     Log.d("MappingApplication", "can't create directory");
                     return null;
                 }
             }
             // create a new image
             File newImage = new File(meAppDirectory.getPath() + File.separator +
                     "IMG_"+ imageName + ".jpg");
             try {
                 newImage.createNewFile();

             } catch (Exception e) {
                 e.printStackTrace();
                 return null;
             }
             //
             return Uri.fromFile(newImage);
         } else {
             return null;
         }

    }

    @Override
    public void OpenCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // get the image uri
        mImageUri = getNewImageUri();
        if(mImageUri != null) {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            // Clear out the image view
            AddImageFragment.mNewImageView.setImageDrawable(null);
        }
        // Launch the camera
        startActivityForResult(cameraIntent, REQUEST_TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "Results returned");
        // Make sure we returned with a good image
        if(requestCode == REQUEST_TAKE_PICTURE && resultCode != RESULT_CANCELED) {
            if(data == null) {
                AddImageFragment.mNewImageView.setImageBitmap(BitmapFactory.decodeFile(mImageUri.getPath()));
            } else {
                AddImageFragment.mNewImageView.setImageBitmap((Bitmap)data.getParcelableExtra("data"));
            }
        }
    }

    // Save to file
    private void addImageToGallery(Uri imageUri) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(imageUri);
        sendBroadcast(scanIntent);
    }

    private void saveNewImageToList () {
        Log.i(TAG, "Saving items in list ");
        // make sure we have an image to save
        if (AddImageFragment.mNewImageView.getDrawable() != null) {
            addImageToGallery(mImageUri);
            // get the other values
            String title = String.valueOf(AddImageFragment.mImageTitleView.getText());
            String location = String.valueOf(AddImageFragment.mImageLocationView.getText());
            // convert the uri to a string
            String uriString = mImageUri.toString();
            // And create a new item
            ImageObject image = ImageObject.newInstance(uriString, title, location, mLatitude, mLongitude);
            ArrayList<ImageObject> imageList = StoredImageData.getStoredImages(this);
            imageList.add(image);
            // save this list
            saveCurrentListToFile(imageList);
            Log.i(TAG, "Items in list: " + imageList);
        }

    }
    public void saveCurrentListToFile (ArrayList<ImageObject> arrayList) {
        // Get the file directory and images file
        File filesDir = getExternalFilesDir(null);
        File file = new File(filesDir, MapViewActivity.IMAGE_FILE_NAME);
        try {
            // Write the current lsit to the file
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(arrayList);
            oos.close();
            // Return to the main view
            returnToMapView();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    // Finish the intent and return to the map
    public void returnToMapView() {
        Intent intent = new Intent(this, MapViewActivity.class);
        setResult(RESULT_OK, intent);
        finish();
    }
}
