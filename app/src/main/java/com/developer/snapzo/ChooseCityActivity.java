package com.developer.snapzo;

import imgLoader.JSONParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import util.ConnectionDetector;
import util.ObjectSerializer;
import Config.ConstValue;
import adapters.CityAdapter;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("NewApi") @SuppressWarnings("deprecation")
public class ChooseCityActivity extends ActionBarActivity {
	public SharedPreferences settings;
	public ConnectionDetector cd;
	
	CityAdapter adapter;
	ArrayList<HashMap<String, String>> cityArray;
	ListView listview;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_city);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg_a));
		
		settings = getSharedPreferences(ConstValue.MAIN_PREF, 0);
		cd=new ConnectionDetector(this);
		
		cityArray = new ArrayList<HashMap<String,String>>();
		try {
			cityArray = (ArrayList<HashMap<String,String>>) ObjectSerializer.deserialize(settings.getString("cityname", ObjectSerializer.serialize(new ArrayList<HashMap<String,String>>())));		
		}catch (IOException e) {
			    e.printStackTrace();
		}
		listview = (ListView)findViewById(R.id.listView1);
		adapter = new CityAdapter(ChooseCityActivity.this, cityArray);
		listview.setAdapter(adapter);
		
		new loadCityTask().execute(true);
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choose_city, menu);
		return true;
	}


	
	public class loadCityTask extends AsyncTask<Boolean, Void, ArrayList<HashMap<String, String>>> {

		JSONParser jParser;
		JSONObject json;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
			// TODO Auto-generated method stub
			if (result!=null) {
				//adapter.notifyDataSetChanged();
			}	
			try {
				settings.edit().putString("cityname",ObjectSerializer.serialize(cityArray)).commit();
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
				Boolean... params) {
			// TODO Auto-generated method stub
			
			try {
				jParser = new JSONParser();
				
				if(cd.isConnectingToInternet())
				{
					String query = "";

					String urlstring = ConstValue.JSON_CITY;
					
					json = jParser.getJSONFromUrl(urlstring);
					if (json.has("data")) {
						
					if(json.get("data") instanceof JSONArray){
						
						JSONArray jsonDrList = json.getJSONArray("data");
						
						cityArray.clear();
						
						
						for (int i = 0; i < jsonDrList.length(); i++) {
							JSONObject obj = jsonDrList.getJSONObject(i);
							HashMap<String, String> map = new HashMap<String, String>();
							
							
							map.put("id", obj.getString("id"));
							
							map.put("name", obj.getString("name"));
							
							cityArray.add(map);
							
											
						}
					}
					
					}
				}else
				{
					Toast.makeText(ChooseCityActivity.this,getString(R.string.internetconnection), Toast.LENGTH_LONG).show();
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
