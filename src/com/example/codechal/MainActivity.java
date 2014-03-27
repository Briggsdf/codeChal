package com.example.codechal;
//import com.dropbox.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.UploadRequest;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AppKeyPair;
import  com.dropbox.client2.session.Session.AccessType;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.DropBoxManager.Entry;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;

public class MainActivity extends Activity {
	final static private String APP_KEY="uooamarlm9d5jpf";
	final static private String APP_SECRET = "c8vln60zaa80b9t";
	final static private AccessType ACCESS_TYPE= AccessType.APP_FOLDER;
	private  DropboxAPI<AndroidAuthSession> mDBApi;
	File photoFile = null;
	String mCurrentPhotoPath;
	static final int REQUEST_IMAGE_CAPTURE = 1;
	
	
	private class uploadHandler extends AsyncTask<URL, Integer, Long> {

		@Override
		protected Long doInBackground(URL... params) {
			// TODO Auto-generated method stub
			return null;
		}
	    // Do the long-running work in here
	  
	    

	   

	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		AppKeyPair appKeys= new AppKeyPair(APP_KEY, APP_SECRET);
		AndroidAuthSession session = new AndroidAuthSession(appKeys,ACCESS_TYPE);
		mDBApi = new DropboxAPI<AndroidAuthSession>(session);
		
	
	
		//final Button buttonAuth = (Button) findViewById(R.id.buttonAuthiD);
		
	}
	

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void viewFolder(View v) throws DropboxException{
		viewFolder vFolder = new viewFolder(MainActivity.this, mDBApi, "/Public");
		vFolder.execute();
		
		
	}
	
	public void upload(View v)  {
		Toast t= Toast.makeText(getApplicationContext(), "Please Take a Photo First", Toast.LENGTH_LONG);
		if(photoFile!=null){
	upload u= new upload(MainActivity.this, mDBApi,"/Public",photoFile);
	u.execute();

		}
		else
			t.show();
	}
	public void takePicture(View v) {
	
	 
		  Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	    }
	    
	    
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
           
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photoFile));
           // startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
	}
	
	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    File storageDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );

	    // Save a file: path for use with ACTION_VIEW intents
	    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    return image;
	}

	
	public void authenticate(View v){
		
		mDBApi.getSession().startOAuth2Authentication(MainActivity.this);	
		
		
	}
	
	
	protected void onResume() {
	    super.onResume();
	    //AndroidAuthSession session = mDBApi.getSession();
	    if (mDBApi.getSession().authenticationSuccessful()) {
	        try {
	            // Required to complete auth, sets the access token on the session
	            mDBApi.getSession().finishAuthentication();

	            String accessToken = mDBApi.getSession().getOAuth2AccessToken();
	        } catch (IllegalStateException e) {
	            Log.i("DbAuthLog", "Error authenticating", e);
	        }
	    }
	}
}
