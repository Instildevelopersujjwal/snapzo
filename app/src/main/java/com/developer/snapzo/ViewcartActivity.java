package com.developer.snapzo;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import fragments.MyCart;
import util.ConnectionDetector;
import Config.ConstValue;
import adapters.ViewCartAdapter;
import util.ObjectSerializer;

import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewcartActivity extends ActionBarActivity  {
	SharedPreferences.OnSharedPreferenceChangeListener listener;
	public SharedPreferences settings;
	public ConnectionDetector cd;
	ArrayList<HashMap<String, String>> cartArray;
	ViewCartAdapter adapter;
	ListView listProduct;
	LinearLayout checkout;

	TextView txtQty,txtitemtotal,txttotalprice;
	TextView txtDeliverycharge;
	TextView txtTotalPrice;
	HashMap<String,String> site_settings;
	MyCart cart ;
	String delivery_charge;
	@SuppressLint("CutPasteId") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewcart);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		cd = new ConnectionDetector(getApplicationContext());
		settings = getSharedPreferences(ConstValue.MAIN_PREF, 0);

		site_settings = new HashMap<String,String>();
		try {
			site_settings = (HashMap<String,String>) ObjectSerializer.deserialize(settings.getString("site_settings", ObjectSerializer.serialize(new HashMap<String, String>())));
		}catch (IOException e) {
			e.printStackTrace();
		}

		cartArray = new ArrayList<HashMap<String,String>>();
		cart = new MyCart(getApplicationContext());
		cartArray = cart.get_items();

		txttotalprice=(TextView)findViewById(R.id.textView2);

		listProduct = (ListView)findViewById(R.id.listView1);
		checkout = (LinearLayout)findViewById(R.id.footerlayout);
		//code to add header when cartb is empty
		if(cartArray.size()==0){
			LayoutInflater inflater = getLayoutInflater();
			ViewGroup header = (ViewGroup) inflater.inflate(R.layout.emptycart_header, listProduct, false);
			listProduct.addHeaderView(header, null, true);
			checkout.setVisibility(checkout.GONE);


		}

		adapter = new ViewCartAdapter(getApplicationContext(), cartArray);
		listProduct.setAdapter(adapter);




		checkout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				double ordertotal = cart.get_order_total();
				double min_value = Double.parseDouble(site_settings.get(getString(R.string.viewcartactivity_min_purchase_order)));
				if (ordertotal > min_value) {
					Intent intent = new Intent(ViewcartActivity.this, CheckoutActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), getString(R.string.viewcartactivity_please_order_minimum_of_amount) + site_settings.get(getString(R.string.viewcartactivity_min_purchase_order)), Toast.LENGTH_LONG).show();
				}
			}
		});

		listProduct.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				if(cartArray.isEmpty()){

				}
				else {
					txtQty = (TextView) view.findViewById(R.id.textqty);

					showPopUpPhone(position);
				}
			}
		});

		updateTotalPrice();

		SharedPreferences sp = getSharedPreferences("item", MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("value", 1);
		editor.commit();
	//	Toast.makeText(getApplicationContext(),"edited",Toast.LENGTH_SHORT).show();


		SharedPreferences prefs = this.getSharedPreferences("item", 0);
		listener = new SharedPreferences.OnSharedPreferenceChangeListener() {

			public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

				//txtQty.setText(cartArray.size());
				SharedPreferences sp = getSharedPreferences("item", MODE_PRIVATE);
				if(sp.getInt("value",1)==0){updateTotalPrice();}
				SharedPreferences.Editor editor = sp.edit();
				editor.putInt("value", 1);
				editor.commit();




			}
		};
		prefs.registerOnSharedPreferenceChangeListener(listener);




	}

	public void updateTotalPrice(){
		txtitemtotal=(TextView)findViewById(R.id.textView1);
		cartArray = cart.get_items();
		Integer totalitem = (Integer) cartArray.size();
		txtitemtotal.setText(totalitem.toString());
		if(cartArray.size()==0){
			//listProduct.setAdapter(null);
			//LayoutInflater inflater = getLayoutInflater();
		//	ViewGroup header = (ViewGroup) inflater.inflate(R.layout.emptycart_header, listProduct, false);
//			listProduct.addHeaderView(header, null, true);
//
			checkout.setVisibility(checkout.GONE);

		}
/*double time = 200.3456;
DecimalFormat df = new DecimalFormat("#.##");
time = Double.valueOf(df.format(time));*/
		double totalpricea = cart.get_order_total();
		DecimalFormat df = new DecimalFormat("#.##");
		double totalprice = Double.valueOf(df.format(totalpricea));
		//long totalprice = Math.round(totalpricea);
		double max_value = Double.parseDouble(site_settings.get(getString(R.string.viewcartactivity_max_purchase_order)));
		if (totalprice >= max_value) {
			delivery_charge = "0.0";
		}else{
			delivery_charge = site_settings.get(getString(R.string.viewcartactivity_delivery_charge));
		}
		txtDeliverycharge = (TextView)findViewById(R.id.delivery_charge);
		txtDeliverycharge.setText(delivery_charge);
		txtTotalPrice = (TextView)findViewById(R.id.textTotalPrice);
		txtTotalPrice.setText(getString(R.string.viewcartactivity_total_price)+totalprice);

	}
	private void showPopUpPhone(final int position) {
		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
		helpBuilder.setTitle(getString(R.string.viewcartactivity_product_qty));
		final EditText input = new EditText(this);
		input.setSingleLine();
		input.setText(txtQty.getText());
		input.setHint(getString(R.string.viewcartactivity_qty)); //Set the text we want to edit
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		helpBuilder.setView(input);


		//Save button
		helpBuilder.setPositiveButton(getString(R.string.viewcartactivity_save),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//Save info here
						HashMap<String, String> map = new HashMap<String, String>();
						map = cartArray.get(position);
						map.put("qty", input.getText().toString());

						MyCart cart = new MyCart(getApplicationContext());
						cart.update_item_cart(map, position);
						txtQty.setText(input.getText());
						updateTotalPrice();

					//	SharedPreferences sp = getSharedPreferences("item", MODE_PRIVATE);
					//	SharedPreferences.Editor editor = sp.edit();
					//	editor.putInt("qty_update", Integer.parseInt(input.getText().toString()));
					//	editor.commit();
					}
				});



		//Cancel button
		helpBuilder.setNegativeButton(getString(R.string.viewcartactivity_cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Do nothing, just close the dialog box


			}
		});
		// Remember, create doesn't show the dialog
		AlertDialog helpDialog = helpBuilder.create();
		helpDialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.viewcart, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if(id==R.id.emptycart){
		//	cartArray = new ArrayList<HashMap<String,String>>();
		//	cart = new MyCart(getApplicationContext());
			cart.empty_cart();
			cartArray = new ArrayList<HashMap<String,String>>();
			cart = new MyCart(getApplicationContext());
			cartArray = cart.get_items();
			if(cartArray.size()==0){
				LayoutInflater inflater = getLayoutInflater();
				ViewGroup header = (ViewGroup) inflater.inflate(R.layout.emptycart_header, listProduct, false);
				listProduct.addHeaderView(header, null, true);

listProduct.setAdapter(null);
				checkout.setVisibility(checkout.GONE);


			}

		}
		else if(id == android.R.id.home){
			finish();
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		//Toast.makeText(ViewcartActivity.this,"back pressed",Toast.LENGTH_SHORT).show();
super.onBackPressed();
	}

}
