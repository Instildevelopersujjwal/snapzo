package com.developer.snapzo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import fragments.MyCart;
import util.Common;
import util.ConnectionDetector;
import util.ObjectSerializer;
import Config.ConstValue;
import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class PlaceorderActivity extends ActionBarActivity {
	public SharedPreferences settings;
	public ConnectionDetector cd;
	Common common;
	Button BtnSave;
	ProgressDialog dialog;
	private RadioGroup radioGroup;
	  private RadioButton radioButton;
	  
	TextView txtitemtotal,textView3;
	ArrayList<HashMap<String, String>> cartArray;

	HashMap<String,String> site_settings;
	MyCart cart ;
	String delivery_charge;

	Double final_total_price;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_placeorder);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		 settings = getSharedPreferences(ConstValue.MAIN_PREF, 0);




		site_settings = new HashMap<String,String>();
		try {
			site_settings = (HashMap<String,String>) ObjectSerializer.deserialize(settings.getString("site_settings", ObjectSerializer.serialize(new HashMap<String, String>())));
		}catch (IOException e) {
			e.printStackTrace();
		}

		cd=new ConnectionDetector(this);
		 common = new Common();
		 
		 cartArray = new ArrayList<HashMap<String,String>>();
		try {
			cartArray = (ArrayList<HashMap<String,String>>) ObjectSerializer.deserialize(settings.getString("cart_items", ObjectSerializer.serialize(new ArrayList<HashMap<String,String>>())));
		}catch (IOException e) {
			e.printStackTrace();
		}
			cart = new MyCart(getApplicationContext());
			cartArray = cart.get_items();
			
			 	txtitemtotal=(TextView)findViewById(R.id.textitem);
				Integer totalitem = (Integer) cartArray.size();
		        txtitemtotal.setText(totalitem.toString());

		Double totalprice = cart.get_order_total();
		double max_value = Double.parseDouble(site_settings.get(getString(R.string.placeorderactivity_max_purchase_order)));
		if (totalprice >= max_value) {
			delivery_charge = "0.0";
		}else{
			delivery_charge = site_settings.get(getString(R.string.placeorderactivity_delivery_charge));
		}
		TextView txtDeliverycharge = (TextView)findViewById(R.id.delivery_charge);
		txtDeliverycharge.setText(delivery_charge);

		TextView txtTotalPrice = (TextView)findViewById(R.id.textTotalPrice);
		txtTotalPrice.setText(getString(R.string.placeorderactivity_total_price)+totalprice);

		final_total_price = Double.parseDouble(delivery_charge) + totalprice;



		BtnSave =(Button)findViewById(R.id.btnsave);
		        BtnSave.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						 radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
						int selectedId = radioGroup.getCheckedRadioButtonId();
						radioButton = (RadioButton) findViewById(selectedId);
						if(selectedId == R.id.radio0)
						{
							new OrderTask().execute(true);
						}else{
							Intent intent = new Intent(PlaceorderActivity.this,PaywithPaypal.class);
							startActivity(intent);
						}
						// find the radiobutton by returned id


						

						
						
					}
				});
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.placeorder, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		 if(id == android.R.id.home){
			finish();
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	class OrderTask extends AsyncTask<Boolean, Void, String> {
		String radiobutton;
		@Override
		protected String doInBackground(Boolean... params) {
			// TODO Auto-generated method stub

			String responseString = null;

			try {
            	
				
            	
            	 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            	 
            	 ArrayList<HashMap<String,String>> cartArray = new ArrayList<HashMap<String,String>>();
         		MyCart cart = new MyCart(getApplicationContext());
         		cartArray = cart.get_items();
            	 for(int i=0;i<cartArray.size();i++)
                 {
            		 HashMap<String, String> selected_product = cartArray.get(i);
            		 Double effected_price =  Double.parseDouble(selected_product.get("price"));
         			if(!selected_product.get("discount").equalsIgnoreCase("") && !selected_product.get("discount").equalsIgnoreCase("0") )
         			{
         			Double discount = Double.parseDouble(selected_product.get("discount"));
         			Double price = Double.parseDouble(selected_product.get("price"));
         			Double discount_amount =  discount * price / 100;
         			
         			effected_price = price - discount_amount ; 
         			}
                    
                      nameValuePairs.add(new BasicNameValuePair("order_item[]", cartArray.get(i).get("id")));
                      nameValuePairs.add(new BasicNameValuePair("order_item_qty[]", cartArray.get(i).get("qty")));
                      nameValuePairs.add(new BasicNameValuePair("order_item_gmqty[]", cartArray.get(i).get("gmqty")));
                      nameValuePairs.add(new BasicNameValuePair("order_item_price[]", effected_price.toString()));
                      nameValuePairs.add(new BasicNameValuePair("order_item_type[]", cartArray.get(i).get("unit")));
                 }
            	 
            	 nameValuePairs.add(new BasicNameValuePair("mobile",settings.getString("order_phone".toString(), responseString)));
            	 nameValuePairs.add(new BasicNameValuePair("city",settings.getString("order_city".toString(), responseString)));
            	 nameValuePairs.add(new BasicNameValuePair("email",settings.getString("order_email".toString(), responseString)));
            	 nameValuePairs.add(new BasicNameValuePair("person_name",settings.getString("order_name".toString(), responseString)));
            	 nameValuePairs.add(new BasicNameValuePair("zipcode",settings.getString("order_zipcode".toString(), responseString)));
            	 nameValuePairs.add(new BasicNameValuePair("address",settings.getString("order_address".toString(), responseString)));
            	
            	 nameValuePairs.add(new BasicNameValuePair("user_id",settings.getString("userid".toString(), responseString)));

				nameValuePairs.add(new BasicNameValuePair("totalprice",String.valueOf(final_total_price)));
            	 nameValuePairs.add(new BasicNameValuePair("totalitem",String.valueOf(cartArray.size())));

            	 nameValuePairs.add(new BasicNameValuePair("cod",radiobutton));
	    	        JSONObject jObj = common.sendJsonData(ConstValue.JSON_ADD_ORDER, nameValuePairs) ;
	    	        if(jObj.getString("responce").equalsIgnoreCase("success")){
	        			
        			}else{
        				responseString = jObj.getString("error"); 
        			}
               
 
            } catch (JSONException e) {
    			Log.e("JSON Parser", getString(R.string.placeorderactivity_error_parsing_data) + e.toString());
    		}
 
            return responseString;
			
			
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected void onCancelled(String result) {
			// TODO Auto-generated method stub
			super.onCancelled(result);
		}

		@Override
		protected void onPostExecute(String result) {
			if(result != null){
				
				
				Toast.makeText(getApplicationContext(),getString(R.string.placeorderactivity_order_placed_succesfull), Toast.LENGTH_LONG).show();
				MyCart emptycart = new MyCart(getApplicationContext());
				emptycart.empty_cart();
			}else{
				
				
				MyCart emptycart = new MyCart(getApplicationContext());
				emptycart.empty_cart();
				Intent intent = new Intent(PlaceorderActivity.this,CompleteOrderActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
				finish();
			}
			// TODO Auto-generated method stub
			dialog.dismiss();
		}

		private ArrayList<HashMap<String, String>> get_items() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			radiobutton = radioButton.getText().toString();
			dialog = ProgressDialog.show(PlaceorderActivity.this, "",getString(R.string.loading), true);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
	}
	
	
}
