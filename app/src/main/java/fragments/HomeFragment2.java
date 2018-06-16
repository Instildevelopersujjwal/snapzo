package fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.developer.snapzo.ProductCategory;
import com.developer.snapzo.ProductsActivity;
import com.developer.snapzo.R;
import com.developer.snapzo.SubCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import Config.ConstValue;
import adapters.CategoryAdapter;
import adapters.SliderImageAdapter;
import imgLoader.JSONParser;
import util.ConnectionDetector;
import util.ObjectSerializer;

@SuppressLint("NewApi")
public class HomeFragment2 extends Fragment {
	Activity act;	
	public SharedPreferences settings;
	public ConnectionDetector cd;
	CategoryAdapter adapter;
	//SliderImageAdapter adapter1;
	ArrayList<HashMap<String, String>> categoryArray2;

	ArrayList<HashMap<String, String>> slideryArray;
	HashMap<String, String> site_settings;
	PagerAdapter pageadapter;
	GridView listview;
	ViewPager viewPager;
	HashMap<String, String>  catMap;
	static ArrayList<HashMap<String, String>> products_array2;

	int mParam1;

    @SuppressWarnings("unchecked")
	@SuppressLint("NewApi") @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home2, container, false);
        act =getActivity();
    
        
 //-----
		settings = act.getSharedPreferences(ConstValue.MAIN_PREF, 0);
		cd=new ConnectionDetector(act);
		
		
		categoryArray2 = new ArrayList<HashMap<String,String>>();
		try {
			categoryArray2 = (ArrayList<HashMap<String,String>>) ObjectSerializer.deserialize(settings.getString("subcategoryname", ObjectSerializer.serialize(new ArrayList<HashMap<String,String>>())));
		}catch (IOException e) {
			    e.printStackTrace();
		}
		listview = (GridView)rootView.findViewById(R.id.gridView1);
		categoryArray2.clear();
		adapter = new CategoryAdapter(act, categoryArray2);

		listview.setAdapter(adapter);
		
		/*
		slideryArray = new ArrayList<HashMap<String,String>>();
		try {
			slideryArray = (ArrayList<HashMap<String,String>>) ObjectSerializer.deserialize(settings.getString("imagename", ObjectSerializer.serialize(new ArrayList<HashMap<String,String>>())));		
		}catch (IOException e) {
			    e.printStackTrace();
		}*/
		//viewPager = (ViewPager)rootView.findViewById(R.id.viewPager);
//viewPager.setVisibility(View.GONE);


		catMap = new HashMap<String, String>();
		if (getArguments() != null) {
			mParam1 = Integer.parseInt(getArguments().getString("params"));
			//Toast.makeText(act,"passed:"+mParam1,Toast.LENGTH_SHORT).show();
		}
//		catMap = categoryArray2.get(mParam1);

		categoryArray2 = new ArrayList<HashMap<String,String>>();
		try {
			categoryArray2 = (ArrayList<HashMap<String,String>>) ObjectSerializer.deserialize(settings.getString(""+catMap.get("id"), ObjectSerializer.serialize(new ArrayList<HashMap<String,String>>())));
		}catch (IOException e) {
			e.printStackTrace();
		}



	        
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				//	ConstValue.selected_products_list = categoryArray;

				//String Category[] =new String[categoryArray2.size()];
				//HashMap<String,String> m = categoryArray2.get()
				//String b = "selected id:"+categoryArray2.get(position).get("id");
				String a = ""+mParam1+""+categoryArray2.get(position).get("id");
				//Toast.makeText(act,"hf,string:"+b,Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(act, ProductCategory.class);
				intent.putExtra("position2", a);
				intent.putExtra("categoryvalue", mParam1);
				startActivity(intent);
			}
		});

		new load_settings().execute(true);
		new loadCategoryTask().execute(true);
	//	new loadSliderTask().execute(true);


        return rootView;
    }
    
 /*   public class loadSliderTask extends AsyncTask<Boolean, Void, ArrayList<HashMap<String, String>>> {

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
				adapter.notifyDataSetChanged();
			}	
			try {
				settings.edit().putString("imagename",ObjectSerializer.serialize(slideryArray)).commit();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pageadapter = new SliderImageAdapter(act,slideryArray);
	        viewPager.setAdapter(pageadapter);
			
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
				//	String query = "";

					String urlstring = ConstValue.JSON_SLIDER_IMAGE;
					
					json = jParser.getJSONFromUrl(urlstring);
					if (json.has("data")) {
						
					if(json.get("data") instanceof JSONArray){
						
						JSONArray jsonDrList = json.getJSONArray("data");
						
						slideryArray.clear();
						
						
						for (int i = 0; i < jsonDrList.length(); i++) {
							JSONObject obj = jsonDrList.getJSONObject(i);
							HashMap<String, String> map = new HashMap<String, String>();
							
							
							map.put("id", obj.getString("id"));
							
							map.put("image", obj.getString("image"));
							map.put("title", obj.getString("title"));
							map.put("status", obj.getString("status"));
							
							slideryArray.add(map);
							
											
						}
					}
					
					}
				}else
				{
					Toast.makeText(act, getString(R.string.loading), Toast.LENGTH_LONG).show();
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
    
	*/
    public class loadCategoryTask extends AsyncTask<Boolean, Void, ArrayList<HashMap<String, String>>> {

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
			//	adapter.notifyDataSetChanged();
			}	
			try {
				settings.edit().putString("subcategoryname",ObjectSerializer.serialize(categoryArray2)).commit();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//System.out.println("system post exeute");
			adapter = new CategoryAdapter(act, categoryArray2);
			listview.setAdapter(adapter);

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

                 //   System.out.print("catmap:"+catMap.get("id"));
				//	System.out.print("\nmparam:"+mParam1);
					//String urlstring = ConstValue.JSON_CATEGORY_A+"&id="+catMap.get("id");
					String urlstring = ConstValue.JSON_CATEGORY_A+"&id="+mParam1;

					json = jParser.getJSONFromUrl(urlstring);
					if (json.has("data")) {
						
					if(json.get("data") instanceof JSONArray){
						
						JSONArray jsonDrList = json.getJSONArray("data");
					//	System.out.println("cleared");
						categoryArray2.clear();
						
						
						for (int i = 0; i < jsonDrList.length(); i++) {
							JSONObject obj = jsonDrList.getJSONObject(i);
							HashMap<String, String> map = new HashMap<String, String>();
							
							
							map.put("id", obj.getString("id"));
							
							map.put("name", obj.getString("name"));
							map.put("slug", obj.getString("slug"));
							map.put("description", obj.getString("description"));
							map.put("icon", obj.getString("icon"));
							
							categoryArray2.add(map);


							System.out.println("Update:yes");
											
						}

						Toast.makeText(act,"Updated", Toast.LENGTH_LONG).show();

					}
					
					}
					adapter = new CategoryAdapter(act, categoryArray2);
					listview.setAdapter(adapter);

					adapter.notifyDataSetChanged();
				}else
				{
					Toast.makeText(act,getString(R.string.internetconnection), Toast.LENGTH_LONG).show();
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



	public class load_settings extends AsyncTask<Boolean, Void, ArrayList<HashMap<String, String>>> {

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
				settings.edit().putString("site_settings",ObjectSerializer.serialize(site_settings)).commit();
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
					//	String query = "";

					String urlstring = ConstValue.JSON_SETTINGS;

					json = jParser.getJSONFromUrl(urlstring);
					if (json.has("data")) {

						if(json.get("data") instanceof JSONArray){

							JSONArray jsonDrList = json.getJSONArray("data");
							site_settings = new HashMap<String, String>();

							for (int i = 0; i < jsonDrList.length(); i++) {
								JSONObject obj = jsonDrList.getJSONObject(i);
								site_settings.put(obj.getString("title"), obj.getString("value"));

							}
						}

					}
				}else
				{
					Toast.makeText(act,getString(R.string.internetconnection), Toast.LENGTH_LONG).show();
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
