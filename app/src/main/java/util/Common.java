package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import Config.ConstValue;



public class Common {
	public JSONObject sendJsonData(String STRURL,List<NameValuePair> nameValuePairs){
		JSONObject objReturn = null;
		String errorString = null; 
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(STRURL);
        try {
        
    	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // Making server call
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
            	InputStream is = r_entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
    					is, "iso-8859-1"), 8);
    			StringBuilder sb = new StringBuilder();
    			String line = null;
    			while ((line = reader.readLine()) != null) {
    				sb.append(line + "\n");
    			}
    			is.close();
    			String json = sb.toString();
    			objReturn = new JSONObject(json);
    			
            } else {
            	objReturn = new JSONObject("{error:'Error occurred! Http Status Code:'}");
            }

        } catch (ClientProtocolException e) {
        	errorString = e.toString();
        	 
        } catch (IOException e) {
        	errorString = e.toString();
        }catch (JSONException e) {
        	errorString = e.toString();
		}

        if(errorString!=null){
			try {
				objReturn = new JSONObject("{error:'"+errorString+"'}");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        return objReturn;
	}
	public JSONObject sendMultiPartData(String STRURL,AndroidMultiPartEntity entity){
		JSONObject objReturn = null;
		String errorString = null; 
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(STRURL);

		try {
			
			httppost.setEntity(entity);

			// Making server call
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity r_entity = response.getEntity();

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
                // Server response
            	InputStream is = r_entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
    					is, "iso-8859-1"), 8);
    			StringBuilder sb = new StringBuilder();
    			String line = null;
    			while ((line = reader.readLine()) != null) {
    				sb.append(line + "\n");
    			}
    			is.close();
    			String json = sb.toString();
    			objReturn = new JSONObject(json);
    			
            } else {
            	objReturn = new JSONObject("{error:'Error occurred! Http Status Code:'}");
            }

        } catch (ClientProtocolException e) {
        	errorString = e.toString();
        	 
        } catch (IOException e) {
        	errorString = e.toString();
        }catch (JSONException e) {
        	errorString = e.toString();
		}

        if(errorString!=null){
			try {
				objReturn = new JSONObject("{error:'"+errorString+"'}");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        return objReturn;

	}
}
