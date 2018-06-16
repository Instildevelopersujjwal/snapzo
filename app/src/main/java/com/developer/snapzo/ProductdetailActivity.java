package com.developer.snapzo;

import fragments.MyCart;
import imgLoader.AnimateFirstDisplayListener;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import util.ConnectionDetector;
import util.ObjectSerializer;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import Config.ConstValue;
import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductdetailActivity extends ActionBarActivity {
	
	public SharedPreferences settings;
	public ConnectionDetector cd;
	static HashMap<String, String> selected_product;
	public static HashMap<String, String> product;
	
	Button addtocart;
	private Button _decrease;
    private Button _increase;
    private TextView _value;
    private int _counter = 1;
    private String _stringVal;
	DisplayImageOptions options;
	ImageLoaderConfiguration imgconfig;
	ProgressDialog dialog;
	
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_productdetail);
		
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		settings = getSharedPreferences(ConstValue.MAIN_PREF, 0);
		cd=new ConnectionDetector(this);
        
        File cacheDir = StorageUtils.getCacheDirectory(this);
 		options = new DisplayImageOptions.Builder()
 		.showImageOnLoading(R.drawable.progress)
 		.showImageForEmptyUri(R.drawable.progress)
 		.showImageOnFail(R.drawable.progress)
 		.cacheInMemory(true)
 		.cacheOnDisk(true)
 		.considerExifParams(true)
 		.displayer(new SimpleBitmapDisplayer())
 		.imageScaleType(ImageScaleType.NONE)
 		.build();
 		
 		imgconfig = new ImageLoaderConfiguration.Builder(this)
 		.build();
 		ImageLoader.getInstance().init(imgconfig);
 		
 		selected_product = new HashMap<String,String>();
        try {
        	selected_product = (HashMap<String,String>) ObjectSerializer.deserialize(settings.getString("selected_product", ObjectSerializer.serialize(new HashMap<String,String>())));		
 		}catch (IOException e) {
 			    e.printStackTrace();
 		}
        
        
		ImageView imgProduct = (ImageView)findViewById(R.id.proimage);
		ImageLoader.getInstance().displayImage(ConstValue.PRO_IMAGE_BIG_PATH+selected_product.get("image"), imgProduct, options, animateFirstListener);
		 
        TextView txtTitle = (TextView)findViewById(R.id.protitle);
		txtTitle.setText(selected_product.get("title"));
		
		
		
		TextView txtunit = (TextView)findViewById(R.id.txtunit);
		txtunit.setText(selected_product.get("unit"));
		
		TextView txtgm = (TextView)findViewById(R.id.txtgm);
		txtgm.setText(selected_product.get("gmqty"));
		
		TextView txtprice = (TextView)findViewById(R.id.txtprice);
		txtprice.setText(selected_product.get("price"));
		
		TextView textDiscount = (TextView)findViewById(R.id.textDiscount);
		textDiscount.setVisibility(View.GONE);
		
		TextView txtDiscountFlag = (TextView)findViewById(R.id.textDiscountFlag);
		txtDiscountFlag.setVisibility(View.GONE);
		
		TextView textCurrency = (TextView)findViewById(R.id.textCurrency);
		textCurrency.setText(selected_product.get("currency"));
		
		if(!selected_product.get("discount").equalsIgnoreCase("") && !selected_product.get("discount").equalsIgnoreCase("0")){
			Double discount = Double.parseDouble(selected_product.get("discount"));
			Double price = Double.parseDouble(selected_product.get("price"));
			Double discount_amount =  discount * price / 100;
			
			Double effected_price = price - discount_amount ; 
			//txtPrice.setBackgroundResource(R.drawable.strike_trough);
			txtprice.setPaintFlags(txtprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			textDiscount.setVisibility(View.VISIBLE);
			textDiscount.setText(effected_price.toString());
			
			txtDiscountFlag.setVisibility(View.VISIBLE);
			txtDiscountFlag.setText(discount+"% off");
		}
		
		
		
		TextView txtdetail = (TextView)findViewById(R.id.textdetaile);
		txtdetail.setText(selected_product.get("description"));
		
		int a = Integer.parseInt(selected_product.get("total_qty_stock").toString());
		int b = Integer.parseInt(selected_product.get("consume_qty_stock").toString());
	
		int result = a - b;
		
		TextView txtstock = (TextView)findViewById(R.id.stock);
		txtstock.setText(String.valueOf(result)+" "+selected_product.get("type")+"In Stock");
		
		//txtstock.setText(selected_product.get("stock")+" "+selected_product.get("type")+" In Stock");
		
		

_decrease = (Button)findViewById(R.id.button2);
_increase = (Button)findViewById(R.id.button1);
_value = (TextView)findViewById(R.id.textView3);

_decrease.setOnClickListener(new OnClickListener() {

    @Override
    public void onClick(View v) {

        Log.d("src", "Decreasing value...");
		if (_counter > 1) {
			_counter--;
			_stringVal = Integer.toString(_counter);
			_value.setText(_stringVal);
		}
    }
});

_increase.setOnClickListener(new OnClickListener() {

    @Override
    public void onClick(View v) {

        Log.d("src", "Increasing value...");
        _counter++;
        _stringVal = Integer.toString(_counter);
        _value.setText(_stringVal);


    }
});

addtocart=(Button)findViewById(R.id.button3);
addtocart.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		selected_product.put("qty",_value.getText().toString());
		
		
		MyCart cart = new MyCart(getApplicationContext());
		cart.add_to_cart(selected_product);
		
		//ConstValue.productToCart.add(selected_product);
		Toast.makeText(ProductdetailActivity.this,getString(R.string.placeorderactivity_product_added_to_cart), Toast.LENGTH_LONG).show();

	}
});
		
    
 		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.products, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		 if(id==R.id.action_viewcart){
        	Intent intent = new Intent(ProductdetailActivity.this,ViewcartActivity.class);
			startActivity(intent);
        	
        }
		else if(id == android.R.id.home){
			finish();
		}
		return super.onOptionsItemSelected(item);
	}



}
