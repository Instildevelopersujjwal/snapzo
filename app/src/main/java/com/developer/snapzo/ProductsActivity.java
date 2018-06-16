package com.developer.snapzo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import imgLoader.AnimateFirstDisplayListener;
import imgLoader.JSONParser;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import util.ConnectionDetector;
import util.ObjectSerializer;
import Config.ConstValue;
import adapters.ProductsAdapter2;

import android.app.SearchManager;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@SuppressLint("NewApi") 
public class ProductsActivity extends ActionBarActivity{
	
	public SharedPreferences settings;
	public ConnectionDetector cd;
	static ArrayList<HashMap<String, String>> products_array2;
	ProductsAdapter2 adapter;
	ListView products_listview;
	DisplayImageOptions options;
	ImageLoaderConfiguration imgconfig;
	ProgressDialog dialog;
	
	TextView txtcount;
	
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	HashMap<String, String>  catMap;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		settings = getSharedPreferences(ConstValue.MAIN_PREF, 0);
        cd = new ConnectionDetector(getApplicationContext());
        
		super.onCreate(savedInstanceState);


	//	try{
			setContentView(R.layout.activity_products);

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
			getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			imgconfig = new ImageLoaderConfiguration.Builder(this)
					.build();
			ImageLoader.getInstance().init(imgconfig);
	//	}catch(Exception e){Toast.makeText(ProductsActivity.this,"Error:"+e.getMessage(),Toast.LENGTH_LONG).show();}

		

 		


 		ArrayList<HashMap<String,String>> categoryArray = new ArrayList<HashMap<String,String>>();
		try {
			categoryArray = (ArrayList<HashMap<String,String>>) ObjectSerializer.deserialize(settings.getString("categoryname", ObjectSerializer.serialize(new ArrayList<HashMap<String,String>>())));
		}catch (IOException e) {
			    e.printStackTrace();
		}

		catMap = new HashMap<String, String>();
		catMap = categoryArray.get(getIntent().getExtras().getInt("position"));

 		products_array2 = new ArrayList<HashMap<String,String>>();
        try {
        	products_array2 = (ArrayList<HashMap<String,String>>) ObjectSerializer.deserialize(settings.getString(""+catMap.get("id"), ObjectSerializer.serialize(new ArrayList<HashMap<String,String>>())));
 		}catch (IOException e) {
 			    e.printStackTrace();
 		}

        products_listview = (ListView)findViewById(R.id.listView1);
        adapter = new ProductsAdapter2(getApplicationContext(), products_array2);
        products_listview.setAdapter(adapter);

        TextView txtTitle = (TextView)findViewById(R.id.catname);
		txtTitle.setText(catMap.get("name"));

		txtcount = (TextView)findViewById(R.id.textcount);


		products_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				// TODO Auto-generated method stub

				try {
					settings.edit().putString(getString(R.string.productsActivity),ObjectSerializer.serialize(products_array2.get(position))).commit();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Intent intent = new Intent(ProductsActivity.this, ProductCategory.class);

				intent.putExtra("position", position);
				startActivity(intent);

			}
		});


        new loadProductsTask().execute(true);




	}
	@Override
	protected void onNewIntent(Intent intent) {

		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			if (!query.equalsIgnoreCase("")) {
				//use the query to search your data somehow
				new loadProductsTask().execute(true);
			}
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.products, menu);

/*
		try{
			MenuItem searchItem = menu.findItem(R.id.action_search);
			SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

			SearchManager searchManager =
					(SearchManager) getSystemService(Context.SEARCH_SERVICE);

			searchView.setSearchableInfo(
					searchManager.getSearchableInfo(getComponentName()));

		}
		catch (Exception e)
		{
			Toast.makeText(ProductsActivity.this,"Error:"+e.getMessage(),Toast.LENGTH_LONG).show();
		}
*/

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if(id==R.id.action_viewcart){
        	Intent intent = new Intent(ProductsActivity.this,ViewcartActivity.class);
			startActivity(intent);

        }
		else if(id == android.R.id.home){
			finish();
		}
		return super.onOptionsItemSelected(item);
	}


	public class loadProductsTask extends AsyncTask<Boolean, Void, ArrayList<HashMap<String, String>>> {

		JSONParser jParser;
		JSONObject json;
		String count;
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
				settings.edit().putString("products_"+catMap.get("id"),ObjectSerializer.serialize(products_array2)).commit();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			txtcount.setText(count);
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
					String urlstring = ConstValue.JSON_PRODUCTS2+"&id="+catMap.get("id");

					if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
						String query = getIntent().getStringExtra(SearchManager.QUERY);
						//use the query to search your data somehow
						urlstring = urlstring + "&search="+query;
					}

					json = jParser.getJSONFromUrl(urlstring);
					count = json.getString("count");
					if (json.has("data")) {
						if(json.get("data") instanceof JSONArray){
							JSONArray jsonDrList = json.getJSONArray("data");
							products_array2.clear();
							for (int i = 0; i < jsonDrList.length(); i++) {
								JSONObject obj = jsonDrList.getJSONObject(i);
								put_object(obj);
							}
						}else if(json.get("data") instanceof JSONObject){
							put_object(json.getJSONObject("data"));
						}
					}
				}else
				{
					Toast.makeText(ProductsActivity.this,getString(R.string.internetconnection), Toast.LENGTH_LONG).show();
				}

				jParser = null;
				json = null;

				} catch (Exception e) {
					// TODO: handle exception

					return null;
				}
			return null;
		}

		public void put_object(JSONObject obj){
			HashMap<String, String> map = new HashMap<String, String>();


			try {
			map.put("id", obj.getString("id"));
			map.put("title", obj.getString("title"));
			map.put("slug", obj.getString("slug"));
			map.put("description", obj.getString("description"));
			map.put("image", obj.getString("image"));

		//	map.put("price", obj.getString("price"));
		//	map.put("currency", obj.getString("currency"));
		//	map.put("discount", obj.getString("discount"));
		//	map.put("cod", obj.getString("cod"));
		//	map.put("emi", obj.getString("emi"));
			map.put("status", obj.getString("status"));

		//	map.put("gmqty", obj.getString("gmqty"));
		//	map.put("unit", obj.getString("unit"));
		//	map.put("deliverycharge", obj.getString("deliverycharge"));
		//	map.put("tax", obj.getString("tax"));
			map.put("category_id", obj.getString("category_id"));

		//	map.put("on_date", obj.getString("on_date"));

		//	map.put("stock", obj.getString("stock"));
		//	map.put("type", obj.getString("type"));
		//	map.put("total_qty_stock", obj.getString("total_qty_stock"));
		//	map.put("consume_qty_stock", obj.getString("consume_qty_stock"));

			products_array2.add(map);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}








}
