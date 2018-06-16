package com.developer.snapzo;

import util.ConnectionDetector;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

@SuppressWarnings("deprecation")
@SuppressLint("NewApi") 
public class SplashActivity extends ActionBarActivity implements MultiplePermissionsListener {
	public SharedPreferences settings;
	public ConnectionDetector cd;

	private static int SPLASH_TIME_OUT = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity
				SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
				if(sp.getInt("session",0)==1)
				{Intent i = new Intent(SplashActivity.this, MainActivity.class);
					startActivity(i);
				}else{Intent i = new Intent(SplashActivity.this, LoginActivity.class);
					startActivity(i);}


				// close this activity
				finish();
			}
		}, SPLASH_TIME_OUT);
	}




	@Override
	public void onPermissionsChecked(MultiplePermissionsReport report) {

	}

	@Override
	public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

	}
}
