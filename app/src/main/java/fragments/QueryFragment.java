package fragments;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.developer.snapzo.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Config.ConstValue;
import util.Common;
import util.ConnectionDetector;

/**
 * Created by shreehari on 9/17/2016.
 */
public class QueryFragment extends Fragment {
    Activity act;
    public SharedPreferences settings;
    public ConnectionDetector cd;
    EditText txtName,txtEmail,txtComment;
    Button btnupadate;
    JSONObject data;
    ProgressDialog dialog;
    Common common;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_query, container, false);
        act =getActivity();

        common = new Common();


        settings = act.getSharedPreferences(ConstValue.MAIN_PREF, 0);
        cd=new ConnectionDetector(act);


        txtName = (EditText)rootView.findViewById(R.id.txtName);
        txtEmail = (EditText)rootView.findViewById(R.id.txtEmail);
        txtComment = (EditText)rootView.findViewById(R.id.txtComment);

        txtName.setText(settings.getString("user_name", ""));
        txtEmail.setText(settings.getString("user_email", ""));




        btnupadate = (Button)rootView.findViewById(R.id.btnsave);
        btnupadate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new ProfileupdateTask().execute(true);
            }
        });



        return rootView;
    }
    class ProfileupdateTask extends AsyncTask<Boolean, Void, String> {
        String name,email,comment;

        @Override
        protected String doInBackground(Boolean... params) {
            String responceString = null;

            List<NameValuePair> nameVapluePairs = new ArrayList<NameValuePair>(2);
            nameVapluePairs.add(new BasicNameValuePair("name",name));
            nameVapluePairs.add(new BasicNameValuePair("email",email));
            nameVapluePairs.add(new BasicNameValuePair("comments",comment));
            nameVapluePairs.add(new BasicNameValuePair("id",settings.getString("userid","")));

            JSONObject jObj = common.sendJsonData(ConstValue.SATISFACTION, nameVapluePairs);
            try{
                if(jObj.getString("responce").equalsIgnoreCase("success")){
                      data = jObj.getJSONObject("data");

                }
                else{
                    responceString = jObj.getString("data");
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
                Toast.makeText(act, "Your Query Send Succesfull", Toast.LENGTH_LONG).show();
                txtName.setText("");
                txtEmail.setText("");
                txtComment.setText("");
            }else{
                Toast.makeText(act, result, Toast.LENGTH_LONG).show();
            }
            // TODO Auto-generated method stub
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            name=txtName.getText().toString();
            email=txtEmail.getText().toString();
            comment=txtComment.getText().toString();
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
