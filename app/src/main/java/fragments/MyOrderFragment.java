package fragments;

import imgLoader.JSONParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import util.ConnectionDetector;
import util.ObjectSerializer;
import Config.ConstValue;
import adapters.ListorderAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.developer.snapzo.OrderDetailActivity;
import com.developer.snapzo.R;

@SuppressLint("NewApi")
public class MyOrderFragment extends Fragment {
	Activity act;	
	public SharedPreferences settings;
	public ConnectionDetector cd;
	ArrayList<HashMap<String, String>> ListorderArray;
	ListorderAdapter adapter;
	ListView listOrderitem;
	 Button btncall;
	
	
    @SuppressWarnings("unchecked")
	@SuppressLint("NewApi") @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_myorder, container, false);
        act =getActivity();
    
        
        
		settings = act.getSharedPreferences(ConstValue.MAIN_PREF, 0);
		cd=new ConnectionDetector(act);
		
		ListorderArray = new ArrayList<HashMap<String,String>>();
		try {
			ListorderArray = (ArrayList<HashMap<String,String>>) ObjectSerializer.deserialize(settings.getString("orderitem", ObjectSerializer.serialize(new ArrayList<HashMap<String,String>>())));		
		}catch (IOException e) {
			    e.printStackTrace();
		}
		listOrderitem = (ListView)rootView.findViewById(R.id.listView1);
		adapter = new ListorderAdapter(act, ListorderArray);
		listOrderitem.setAdapter(adapter);
		
		
		
		listOrderitem.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(act,OrderDetailActivity.class);
				//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra("order_id", ListorderArray.get(arg2).get("id"));
				intent.putExtra("status", ListorderArray.get(arg2).get("status"));
				intent.putExtra("position",arg2);
				startActivityForResult(intent,100);
			}
		});
		
		new loadOrderitemTask().execute(true);
        return rootView;
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
    	super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            // Make sure the request was successful
            if (resultCode == act.RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
            	HashMap<String, String> map = ListorderArray.get(data.getIntExtra("current_postion",0));
				map.put("status", "2");
				ListorderArray.set(data.getIntExtra("current_postion",0), map);
				adapter.notifyDataSetChanged();
                // Do something with the contact here (bigger example below)
            }
        }
        
    }
   
    public class loadOrderitemTask extends AsyncTask<Boolean, Void, ArrayList<HashMap<String, String>>> {

  		JSONParser jParser;
  		JSONObject json;
  		
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
  				settings.edit().putString("orderitem",ObjectSerializer.serialize(ListorderArray)).commit();
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

  					String urlstring = ConstValue.JSON_LIST_ORDER+"&user_id="+settings.getString("userid","");
  					
  					json = jParser.getJSONFromUrl(urlstring);
  					if (json.has("data")) {
  						
  					if(json.get("data") instanceof JSONArray){
  						
  						JSONArray jsonDrList = json.getJSONArray("data");
  						
  						ListorderArray.clear();
  						
  						
  						for (int i = 0; i < jsonDrList.length(); i++) {
  							JSONObject obj = jsonDrList.getJSONObject(i);
  							HashMap<String, String> map = new HashMap<String, String>();
  							
  							
  							map.put("id", obj.getString("id"));
  							
  							map.put("recipt_no", obj.getString("recipt_no"));
  							map.put("status", obj.getString("status"));
  							map.put("order_date", obj.getString("order_date"));
  							
  							ListorderArray.add(map);
  							
  											
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
