package com.developer.snapzo;


import fragments.HomeFragment2;

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

import Config.ConstValue;

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

import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
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

public class SubCategory extends AppCompatActivity  {
    ActionBar actionBar;




    public SharedPreferences settings;
    public ConnectionDetector cd;

    DisplayImageOptions options;
    ImageLoaderConfiguration imgconfig;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    private BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
       int p = Integer.parseInt(getIntent().getExtras().getString("position"));
     //   Toast.makeText(SubCategory.this,"sub:"+p,Toast.LENGTH_SHORT).show();


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
            Toast.makeText(SubCategory.this,"Error:"+e.getMessage(),Toast.LENGTH_LONG).show();
        }
        set_fragment_page(0);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id==R.id.action_viewcart){

            Intent intent = new Intent(SubCategory.this,ViewcartActivity.class);
            startActivity(intent);

        }

        else if(id == android.R.id.home){
            Intent intent = new Intent(SubCategory.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.products, menu);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }



    public void set_fragment_page(int position){
        Fragment fragment = null;
        Intent intent = null;

        Bundle args;
        switch (position) {
            case 0:

                fragment = new HomeFragment2();
                args = new Bundle();
                int p = Integer.parseInt(getIntent().getExtras().getString("position"));
                args.putString("params", ""+p);
                fragment.setArguments(args);
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

        }
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


}

