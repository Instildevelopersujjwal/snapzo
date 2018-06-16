package fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import util.Common;
import util.ConnectionDetector;
import Config.ConstValue;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.developer.snapzo.MainActivity;
import com.developer.snapzo.R;


@SuppressLint("NewApi")
public class MyProfileFragment extends Fragment {
	Activity act;	
	public SharedPreferences settings;
	public ConnectionDetector cd;
	EditText txtCity,txtPhone,txtName,txtUsername,txtAddress,txtEmail,txtZipcode;
	Button btnupadate;
	ProgressDialog dialog;
	Common common;
	
    @SuppressLint({ "NewApi", "CutPasteId" }) @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_myprofile, container, false);
        act =getActivity();
        
        common = new Common();
        
 
		settings = act.getSharedPreferences(ConstValue.MAIN_PREF, 0);
		cd=new ConnectionDetector(act);

		txtCity = (EditText)rootView.findViewById(R.id.editCity);
		txtName = (EditText)rootView.findViewById(R.id.editFname);
		txtPhone = (EditText)rootView.findViewById(R.id.editPhone);
		txtUsername = (EditText)rootView.findViewById(R.id.editUsername);
		txtAddress = (EditText)rootView.findViewById(R.id.editAddress);
		txtEmail = (EditText)rootView.findViewById(R.id.editEmail);
		txtZipcode = (EditText)rootView.findViewById(R.id.editZipcode);
		
		 txtName.setText(settings.getString("user_name", ""));
	        
		    
	        txtUsername.setText(settings.getString("username", ""));
			
	    
	        txtEmail.setText(settings.getString("user_email", ""));
	        
	    
	        txtPhone.setText(settings.getString("user_mobile", ""));
	        
	    
	        txtAddress.setText(settings.getString("user_address", ""));
			
	    
	        txtZipcode.setText(settings.getString("user_zipcode", ""));
	        
	    
	        txtCity.setText(settings.getString("user_city", ""));
	        
	    

		btnupadate = (Button)rootView.findViewById(R.id.btnupadate);
		btnupadate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new ProfileupdateTask().execute(true);
			}
		});
	
	
       
        return rootView;
    }
	
   
    
    class ProfileupdateTask extends AsyncTask<Boolean, Void, String> {
		String txtphone,txtcity,txtname,txtaddress,txtzipcode,txtemail,txtusername;
		@Override
		protected String doInBackground(Boolean... params) {
			String responceString = null;
			List<NameValuePair> nameVapluePairs = new ArrayList<NameValuePair>(2);
			nameVapluePairs.add(new BasicNameValuePair("mobile", txtphone));
			nameVapluePairs.add(new BasicNameValuePair("city",txtcity));
			nameVapluePairs.add(new BasicNameValuePair("name",txtname));
			nameVapluePairs.add(new BasicNameValuePair("address",txtaddress));
			nameVapluePairs.add(new BasicNameValuePair("zipcode",txtzipcode));
			nameVapluePairs.add(new BasicNameValuePair("email",txtemail));
			nameVapluePairs.add(new BasicNameValuePair("username",txtusername));
			nameVapluePairs.add(new BasicNameValuePair("id",settings.getString("userid","")));
			
			JSONObject jObj = common.sendJsonData(ConstValue.JSON_PROFILE_UPDATE, nameVapluePairs);
			try{
				if(jObj.getString("responce").equalsIgnoreCase("success")){
					JSONObject data = jObj.getJSONObject("data");
					if(!data.getString("id").equalsIgnoreCase("")){
						settings.edit().putString("userid", data.getString("id")).commit();
						settings.edit().putString("username", data.getString("username")).commit();
						settings.edit().putString("user_unique_code", data.getString("unique_code")).commit();
						settings.edit().putString("user_email", data.getString("email")).commit();
						settings.edit().putString("user_name", data.getString("name")).commit();
						settings.edit().putString("user_mobile", data.getString("mobile")).commit();
						settings.edit().putString("user_address", data.getString("address")).commit();
						settings.edit().putString("user_state", data.getString("state")).commit();
						settings.edit().putString("user_country", data.getString("country")).commit();
						settings.edit().putString("user_zipcode", data.getString("zipcode")).commit();
						settings.edit().putString("user_city", data.getString("city")).commit();
						settings.edit().putString("user_password", data.getString("password")).commit();
						settings.edit().putString("user_image", data.getString("image")).commit();
						settings.edit().putString("user_phone_verified", data.getString("phone_verified")).commit();
						settings.edit().putString("user_reg_date", data.getString("reg_date")).commit();
						settings.edit().putString("user_status", data.getString("status")).commit();
						
						
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
				Toast.makeText(act, getString(R.string.myprofilefragment_your_profile_update_Succesfull), Toast.LENGTH_LONG).show();
				
			}else{
				Intent intent = new Intent(act,MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
				
			}
			// TODO Auto-generated method stub
			dialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			txtphone = txtPhone.getText().toString();
			txtcity =txtCity.getText().toString();
			txtname =txtName.getText().toString();
			txtaddress = txtAddress.getText().toString();
			txtzipcode = txtZipcode.getText().toString();
			txtemail =txtEmail.getText().toString();
			txtusername = txtUsername.getText().toString();
			dialog = ProgressDialog.show(act, "",getString(R.string.loading), true);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
	}


}
