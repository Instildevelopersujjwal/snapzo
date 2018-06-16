package com.developer.snapzo;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Config.ConstValue;
import util.Common;
import util.ConnectionDetector;



public class SignUpActivity extends ActionBarActivity {
	public SharedPreferences settings;
	public ConnectionDetector cd;
	EditText txtPassword,txtPhone;
	Button btnregister;
	ProgressDialog dialog;
	String deviceid;
	Common common;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		//getSupportActionBar().setHomeButtonEnabled(true);
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg_a));

		common = new Common();

		txtPassword = (EditText)findViewById(R.id.editPassword);
		txtPhone = (EditText)findViewById(R.id.edit_phone);
		btnregister = (Button)findViewById(R.id.button1);


		btnregister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if((txtPhone.getText()).toString().length()== 0)
				{

					txtPhone.setError(getString(R.string.signupactivity_mobile_no));
				}

				else if (txtPassword.getText().toString().length()== 0) {

					txtPassword.setError(getString(R.string.signupactivity_enter_password));
				}
				else
				{
				new registerTask().execute(true);
				}
				// TODO Auto-generated method stub

			}
		});

	}
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
	//	if (id == R.id.action_settings) {
		//	return true;
		//}else
		if(id == android.R.id.home){
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
*/
	class registerTask extends AsyncTask<Boolean, Void, String> {

		String txtphone,txtpassword;

		@Override
		protected String doInBackground(Boolean... params) {
			// TODO Auto-generated method stub

			String responseString = null;

			try {


				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("mobile",txtphone));
				nameValuePairs.add(new BasicNameValuePair("password",txtpassword));
				JSONObject jObj = common.sendJsonData(ConstValue.JSON_REGISTER, nameValuePairs) ;
				if(jObj.getString("responce").equalsIgnoreCase("success")){

				}else{
					responseString = jObj.getString("error");
				}


			} catch (JSONException e) {
				//Log.e("JSON Parser", "Error parsing data " + e.toString());
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
				Toast.makeText(SignUpActivity.this, result, Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(SignUpActivity.this,getString(R.string.signupactivity_your_registration), Toast.LENGTH_LONG).show();
				Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
				finish();
			}
			// TODO Auto-generated method stub
			dialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			txtphone =txtPhone.getText().toString();
			txtpassword =txtPassword.getText().toString();
			dialog = ProgressDialog.show(SignUpActivity.this, "",getString(R.string.loading), true);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

	}

}