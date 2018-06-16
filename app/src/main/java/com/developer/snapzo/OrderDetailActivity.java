package com.developer.snapzo;

import imgLoader.JSONParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.Common;
import util.ConnectionDetector;
import util.ObjectSerializer;
import Config.ConstValue;
import adapters.OrderdetailAdapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class OrderDetailActivity extends ActionBarActivity {

	public SharedPreferences settings;
	public ConnectionDetector cd;
	Common common;
	Button btncall;
	
	ArrayList<HashMap<String, String>> orderdetailArray;
	OrderdetailAdapter adapter;
	ListView listOrderdetail;
	TextView txtTotal;
	String order_id,order_status;
	int current_postion;
	//ProgressDialog dialog;
	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);
	
		settings = getSharedPreferences(ConstValue.MAIN_PREF, 0);
		cd=new ConnectionDetector(this);
		common = new Common();
		
		order_id = getIntent().getExtras().getString("order_id"); 
		order_status = getIntent().getExtras().getString("status"); 
		current_postion  = getIntent().getExtras().getInt("position"); 
		
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		orderdetailArray = new ArrayList<HashMap<String,String>>();
		try {
			orderdetailArray = (ArrayList<HashMap<String,String>>) ObjectSerializer.deserialize(settings.getString("orderdetails_"+order_id, ObjectSerializer.serialize(new ArrayList<HashMap<String,String>>())));		
		}catch (IOException e) {
			    e.printStackTrace();
		}
		listOrderdetail = (ListView)findViewById(R.id.listView1);
		adapter = new OrderdetailAdapter(OrderDetailActivity.this,orderdetailArray);
		listOrderdetail.setAdapter(adapter);
		 txtTotal = (TextView)findViewById(R.id.textTotalAmount);
		adapter.setOnDataChangeListener(new OrderdetailAdapter.OnDataChangeListener(){
	        public void onDataChanged(Double total){
	            //do whatever here
	        	txtTotal.setText("Total : "+total.toString());
	        }
	    });
		
		new loadOrderdetailTask().execute();
		 
		Button btnCancle = (Button)findViewById(R.id.buttonCancle);
		btnCancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new statusupdateTask().execute(order_id);
			}
		});
		if(!order_status.equalsIgnoreCase("0")){
			btnCancle.setVisibility(View.GONE);
		}
	}
	  class statusupdateTask extends AsyncTask<String, Void, String> {

			protected String doInBackground(String... params) {
				String responceString = null;
				List<NameValuePair> nameVapluePairs = new ArrayList<NameValuePair>(2);
				
					
				JSONObject jObj = common.sendJsonData(ConstValue.JSON_CANCLE_ORDER+"&orderid="+params[0], nameVapluePairs);
				try{
					if(jObj.getBoolean("responce")){
						JSONObject data = jObj.getJSONObject("data");
						if(!data.getString("id").equalsIgnoreCase("")){
							 
							
						}
					}
					else{
						responceString = jObj.getString("error");
					}
				}
				catch(JSONException e){
					responceString = e.getMessage();
				}
				// TODO Auto-generated method stub
				return responceString;
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
					Toast.makeText(OrderDetailActivity.this,getString(R.string.orderdetailactivity_order_cancle), Toast.LENGTH_LONG).show();
					Intent returnIntent = getIntent();
					returnIntent.putExtra("current_postion",current_postion);
					setResult(OrderDetailActivity.RESULT_OK,returnIntent);
					finish();
					
				}else{
				//	Intent intent = new Intent(context,MainActivity.class);
					//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					//startActivity(intent);
					
				}
				// TODO Auto-generated method stub
				//dialog.dismiss();
			}

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				//dialog = ProgressDialog.show(getApplicationContext(), "", "Loading. Please Wait..", true);
				super.onPreExecute();
			}

			@Override
			protected void onProgressUpdate(Void... values) {
				// TODO Auto-generated method stub
				super.onProgressUpdate(values);
			}
			
		}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.order_detail, menu);
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
	
	 public class loadOrderdetailTask extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {

	  		JSONParser jParser;
	  		JSONObject json;
	  		
	  		protected void onPreExecute() {
	  			// TODO Auto-generated method stub
	  			super.onPreExecute();
	  		}

	  		public void execute(boolean b) {
				// TODO Auto-generated method stub
				
			}

			@Override
	  		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
	  			// TODO Auto-generated method stub
	  			if (result!=null) {
	  				//adapter.notifyDataSetChanged();
	  			}	
	  			try {
	  				settings.edit().putString("orderdetails",ObjectSerializer.serialize(orderdetailArray)).commit();
	  			} catch (IOException e) {
	  				// TODO Auto-generated catch block
	  				e.printStackTrace();
	  			}
	  			
	  			
	  			adapter.notifyDataSetChanged();
	  			super.onPostExecute(result);
	  		}

	  		@Override
	  		protected void onProgressUpdate(Void... values) {
	  			// TODO Auto-generated method stub
	  			super.onProgressUpdate(values);
	  		}

	  		@Override
	  		protected void onCancelled(ArrayList<HashMap<String, String>> result) {
	  			// TODO Auto-generated method stub
	  			super.onCancelled(result);
	  		}

	  	
	  		@Override
	  		protected ArrayList<HashMap<String, String>> doInBackground(
	  				String... params) {
	  			// TODO Auto-generated method stub
	  			
	  			try {
	  				jParser = new JSONParser();
	  				
	  				if(cd.isConnectingToInternet())
	  				{
	  				//	String query = "";

	  					String urlstring = ConstValue.JSON_ORDER_DETAIL+"&order_id="+order_id;
	  					
	  					json = jParser.getJSONFromUrl(urlstring);
	  					if (json.has("data")) {
	  						
	  					if(json.get("data") instanceof JSONArray){
	  						
	  						JSONArray jsonDrList = json.getJSONArray("data");
	  						
	  						orderdetailArray.clear();
	  						
	  						
	  						for (int i = 0; i < jsonDrList.length(); i++) {
	  							JSONObject obj = jsonDrList.getJSONObject(i);
	  							HashMap<String, String> map = new HashMap<String, String>();
	  							
	  							
	  							//map.put("id", obj.getString("id"));
	  							
	  							map.put("title", obj.getString("title"));
	  							map.put("type", obj.getString("type"));
	  							map.put("gmqty", obj.getString("gmqty"));
	  							map.put("qty", obj.getString("qty"));
	  							map.put("price", obj.getString("price"));
	  							orderdetailArray.add(map);
	  							
	  											
	  						}
	  					}
	  					
	  					}
	  				}else
	  				{
	  					Toast.makeText(OrderDetailActivity.this,getString(R.string.internetconnection), Toast.LENGTH_LONG).show();
	  				}
	  					
	  			jParser = null;
	  			json = null;
	  			
	  				} catch (Exception e) {
	  					// TODO: handle exception
	  					
	  					return null;
	  				}
	  			return null;
	  		}

	  	}
	
}
