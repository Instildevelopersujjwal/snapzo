package com.developer.snapzo;



import fragments.QueryFragment;
import gcm.QuickstartPreferences;
import gcm.RegistrationIntentService;
import imgLoader.AnimateFirstDisplayListener;
import imgLoader.RoundedImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import util.ConnectionDetector;
import fragments.HomeFragment;
import fragments.MyOrderFragment;
import fragments.MyProfileFragment;
import Config.ConstValue;
import adapters.MenuAdapter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


@SuppressWarnings("deprecation")
@SuppressLint("NewApi")
//ActionBarActivity
public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {
	ActionBar actionBar;
	public static final String TAG = "MainActivity";

    // Whether the Log Fragment is currently shown
    private boolean mLogShown;
    
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDeawerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    
    ArrayList<HashMap<String, String>> menuArray;
    ListView mListView;
    MenuAdapter mAdapter;
    
    public SharedPreferences settings;
	public ConnectionDetector cd;
	 private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	    DisplayImageOptions options;
		ImageLoaderConfiguration imgconfig;

	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


	private BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //killer code starts
		SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
		if(sp.getInt("session",0)==1)
		{//
		}else{Intent i = new Intent(MainActivity.this, LoginActivity.class);
			startActivity(i);}
		///killer code ends





        settings = getSharedPreferences(ConstValue.MAIN_PREF, 0);
		cd=new ConnectionDetector(this);
		File cacheDir = StorageUtils.getCacheDirectory(this);
 		options = new DisplayImageOptions.Builder()
 		.showImageOnLoading(R.drawable.icons_side_menu_03)
 		.showImageForEmptyUri(R.drawable.icons_side_menu_03)
 		.showImageOnFail(R.drawable.icons_side_menu_03)
 		.cacheInMemory(true)
 		.cacheOnDisk(true)
 		.considerExifParams(true)
 		.displayer(new SimpleBitmapDisplayer())
 		.imageScaleType(ImageScaleType.EXACTLY)
 		.build();
 		
 		imgconfig = new ImageLoaderConfiguration.Builder(this)
 		.build();
 		ImageLoader.getInstance().init(imgconfig);
try
{
	//actionBar = getSupportActionBar();
   // actionBar.setDisplayShowCustomEnabled(true);
   // actionBar.setDisplayShowTitleEnabled(false);
 //   getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

//    LayoutInflater inflator = (LayoutInflater) this
//           .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    View v = inflator.inflate(R.menu.main, null);
  //  actionBar.setCustomView(v);
	actionBar = getSupportActionBar();
	actionBar.setDisplayShowCustomEnabled(true);
	actionBar.setDisplayShowTitleEnabled(false);
	getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	LayoutInflater inflator = (LayoutInflater) this
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	View v = inflator.inflate(R.layout.action_bar_title, null);
	actionBar.setCustomView(v);
	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	getSupportActionBar().setHomeButtonEnabled(true);
   // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    //getSupportActionBar().setHomeButtonEnabled(true);
   // getSupportActionBar().setHomeAsUpIndicator(R.drawable.icons_checkout);
}
catch(Exception e)
{
	Toast.makeText(MainActivity.this,"Error:"+e.getMessage(),Toast.LENGTH_LONG).show();
}

      
        
        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
      
        
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDeawerView = (LinearLayout)findViewById(R.id.left_drawer);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getSupportActionBar().setTitle("Home");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
               
                //getSupportActionBar().setTitle("User");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setHomeButtonEnabled(true);
        
       // getSupportActionBar().setBackgroundDrawable(R.drawable.background_01);
      
        
        menuArray = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        
        map.put("title", "MY PROFILE");
        map.put("desc", "Update Your Profile");
        menuArray.add(map);
        
        map = new HashMap<String, String>();
        map.put("title", "MY ORDERS");
        map.put("desc", "Your Order Detail");
        menuArray.add(map);
        
        map = new HashMap<String, String>();
        map.put("title", "VIEW CART");
        map.put("desc", "Current Item In Cart");
        menuArray.add(map);

		map = new HashMap<String, String>();
		map.put("title", "QUERY");
		map.put("desc", "Send Your Query");
		menuArray.add(map);

        map = new HashMap<String, String>();
        map.put("title", "LOGOUT");
        map.put("desc", "Shut Down");
        menuArray.add(map);
        

        mListView = (ListView)findViewById(R.id.listView1);
        LayoutInflater inflatorMenu = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View menu_header = inflatorMenu.inflate(R.layout.menu_header_view, null);
        TextView txtUserName = (TextView)menu_header.findViewById(R.id.left_user_title);
        txtUserName.setText(settings.getString("user_name", ""));
        TextView txtLocation = (TextView)menu_header.findViewById(R.id.left_user_desc);
        txtLocation.setText(settings.getString("user_city", ""));
        RoundedImageView imageProfile = (RoundedImageView)menu_header.findViewById(R.id.left_user_image);
        ImageLoader.getInstance().displayImage(ConstValue.IMAGE_PROFILE_PATH + settings.getString("user_image", ""), imageProfile, options, animateFirstListener);
    	
        
        mListView.addHeaderView(menu_header);
        mAdapter = new MenuAdapter(getApplicationContext(), menuArray);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				// TODO Auto-generated method stub
				set_fragment_page(arg2);

			}
		});

		if(getIntent().hasExtra("fragment_position")){
			set_fragment_page(getIntent().getExtras().getInt("fragment_position"));
		}else {
			set_fragment_page(0);
		}

		//----GCM SERVICE START----//
		mRegistrationBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				SharedPreferences sharedPreferences =
						PreferenceManager.getDefaultSharedPreferences(context);
				boolean sentToken = sharedPreferences
						.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
				if (sentToken) {

				} else {

				}
			}
		};

		if (checkPlayServices()) {
			// Start IntentService to register this application with GCM.
			Intent intent = new Intent(this, RegistrationIntentService.class);
			startService(intent);

		}
		//----END GCM SERVICE START----//
    }
    
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
        // Handle your other action bar items...
        int id = item.getItemId();
		 if(id==R.id.action_viewcart){

				Intent intent = new Intent(MainActivity.this,ViewcartActivity.class);
				startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
    }
	

	@Override
	public void onTabSelected(Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		set_fragment_page(Integer.parseInt(arg0.getTag().toString()));
	}

	@Override
	public void onTabUnselected(Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
	public void set_fragment_page(int position){
		Fragment fragment = null;
		Intent intent = null;

		Bundle args;
		switch (position) {
		case 0:
			fragment = new HomeFragment();
	        args = new Bundle();
	        fragment.setArguments(args);
			break;
			case 1:
				 fragment = new MyProfileFragment();
					break;
			case 2:
				fragment = new MyOrderFragment();

				break;
			case 3:
				intent = new Intent(MainActivity.this, ViewcartActivity.class);

				break;
			case 4:
				fragment = new QueryFragment();
				break;

			case 5:
				SharedPreferences myPrefs = getSharedPreferences("MY_PREF",
						MODE_PRIVATE);
				SharedPreferences.Editor editor = myPrefs.edit();
				editor.clear();
				editor.commit();
				//killer code starts
				SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
				SharedPreferences.Editor editor2 = sp.edit();
				editor2.putInt("session", 0);
				editor2.commit();
				//killer code ends

				Toast.makeText(getApplicationContext(),getString(R.string.mainactivity_logout_sucessfull), Toast.LENGTH_SHORT).show();
				Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
				intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent1);
                finish();
				break;
		
		default:
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
		if (fragment!=null) {
	        // Insert the fragment by replacing any existing fragment
	        FragmentManager fragmentManager = getFragmentManager();
	        if (position==0) {
	        	fragmentManager.beginTransaction()
	            .replace(R.id.sample_content_fragment, fragment)
	            .commit();
			}else{
				fragmentManager.beginTransaction()
	            .replace(R.id.sample_content_fragment, fragment)
	            .addToBackStack(null)
	            .commit();
			}
			mDrawerLayout.closeDrawer(mDeawerView);
	    }
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.mainactivity_mainactivity_are_exit))
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						MainActivity.this.finish();
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();

	}
	@Override
	protected void onResume() {
		super.onResume();
		LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
				new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
	}

	@Override
	protected void onPause() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
		super.onPause();
	}
	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
		int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (apiAvailability.isUserResolvableError(resultCode)) {
				apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
						.show();
			} else {
				Log.i(TAG,getString(R.string.mainactivity_this_device));
				finish();
			}
			return false;
		}
		return true;
	}
}
