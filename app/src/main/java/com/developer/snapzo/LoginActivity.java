package com.developer.snapzo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import util.Common;
import util.ConnectionDetector;
import Config.ConstValue;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@SuppressLint("NewApi") 
public class LoginActivity extends ActionBarActivity {
	public SharedPreferences settings;
	public ConnectionDetector cd;
	EditText txtPhone, txtPassword;
	Button btnRegister,btnLogin;
	String deviceid;
	Common common;
	ProgressDialog dialog;
	AsyncTask<Void, Void, Void> mRegisterTask;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

       common = new Common();
		
		settings = getSharedPreferences(ConstValue.MAIN_PREF, 0);
		cd=new ConnectionDetector(this);
		
		txtPhone = (EditText)findViewById(R.id.editPhone);
		txtPassword = (EditText)findViewById(R.id.editPassword);
		//txtPhone.setText("9452105594");
		//txtPassword.setText("zxcvbnm");
		btnRegister = (Button)findViewById(R.id.buttonRegister);
		btnRegister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
				startActivity(intent);
				
			}
		});

		Button btnneed = (Button)findViewById(R.id.btnneed);
		btnneed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this, ForgotActivity.class);
				startActivity(intent);

			}
		});

		btnLogin = (Button)findViewById(R.id.buttonLogin);
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(txtPhone.getText().toString().length()==0)
				{
					txtPhone.setError(getString(R.string.forgotactivity_mobile_no));
				}
				else if (txtPassword.getText().toString().length()==0) {
					txtPassword.setError(getString(R.string.forgotactivity_enter_password));
				}
				else{
						new loginTask().execute(true);
					}
				// TODO Auto-generated method stub
				
				
			}
		});


	}



	class loginTask extends AsyncTask<Boolean, Void, String>{
		String phone,password;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			phone=txtPhone.getText().toString();
			password=txtPassword.getText().toString();
			dialog = ProgressDialog.show(LoginActivity.this, "",getString(R.string.loading), true);
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String result) {
			if(result != null){
				Toast.makeText(LoginActivity.this, "Enter correct Credentials", Toast.LENGTH_LONG).show();
			}else{
			//	Toast.makeText(LoginActivity.this, "sccess", Toast.LENGTH_LONG).show();
				//startgcmregistration();
				Intent intent = new Intent(LoginActivity.this,VerificationActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra("phone",phone );


				startActivity(intent);
				finish();
			}
			// TODO Auto-generated method stub
			dialog.dismiss();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
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
		protected String doInBackground(Boolean... params) {
			String responceString = null;
			List<NameValuePair> nameVapluePairs = new ArrayList<NameValuePair>(2);
			nameVapluePairs.add(new BasicNameValuePair("mobile", phone));
			nameVapluePairs.add(new BasicNameValuePair("password",password));
			
			JSONObject jObj = common.sendJsonData(ConstValue.JSON_LOGIN, nameVapluePairs);
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
		
				
	}
	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.mainactivity_mainactivity_are_exit))
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						LoginActivity.this.finish();
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();

	}
	public void startgcmregistration(){

	}

	}

