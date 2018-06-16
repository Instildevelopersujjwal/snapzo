package adapters;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import util.Common;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.developer.snapzo.R;
 

@SuppressLint({ "SimpleDateFormat", "InflateParams" }) public class ListorderAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<HashMap<String, String>> postItems;
	public SharedPreferences settings;
	public final String PREFS_NAME = "OrderItem";
	Common common;
	DisplayImageOptions options;
	ImageLoaderConfiguration imgconfig; 
	ProgressDialog dialog;
	//Button cancle, detail;
	int clicked_index;
	public ListorderAdapter(Context context, ArrayList<HashMap<String, String>> arraylist){
		this.context = context;
		common = new Common();
		File cacheDir = StorageUtils.getCacheDirectory(context);
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.progress)
		.showImageForEmptyUri(R.drawable.progress)
		.showImageOnFail(R.drawable.progress)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new SimpleBitmapDisplayer())
		.imageScaleType(ImageScaleType.EXACTLY)
		.build();
		
		imgconfig = new ImageLoaderConfiguration.Builder(context)
		.build();
		ImageLoader.getInstance().init(imgconfig);
			
		postItems = arraylist;
		settings = context.getSharedPreferences(PREFS_NAME, 0);		
		
	}

	@Override
	public int getCount() {
		return postItems.size();
	}
	@Override
	public Object getItem(int position) {		
		return postItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
			if (convertView == null) {
	            LayoutInflater mInflater = (LayoutInflater)
	            context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	            convertView = mInflater.inflate(R.layout.row_orderitem, null);	
	            
	        }
			final HashMap<String, String> map = postItems.get(position);
			
			TextView txtdate = (TextView)convertView.findViewById(R.id.ondate);
			txtdate.setText(map.get("order_date"));
			
			/*cancle = (Button)convertView.findViewById(R.id.cancle);
			cancle.setContentDescription(map.get("id"));
			cancle.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new statusupdateTask().execute(v.getContentDescription().toString());
					clicked_index = position;
					
				}
			});
			cancle.setVisibility(View.GONE); */
				
			TextView txtrecipt = (TextView)convertView.findViewById(R.id.reciptno);
			txtrecipt.setText(map.get("recipt_no"));
			
			TextView txtstatus = (TextView)convertView.findViewById(R.id.status);
			if(map.get("status").equalsIgnoreCase("0")){
				
				txtstatus.setText("Pending");	
				txtstatus.setBackgroundResource(R.drawable.xml_gray_button);
				
			}
			else if(map.get("status").equalsIgnoreCase("1")){
				
				txtstatus.setText("Confirm");	
				txtstatus.setBackgroundResource(R.drawable.xml_green_button);
			}
			else if(map.get("status").equalsIgnoreCase("2")){
				txtstatus.setText("Cancle");	
				txtstatus.setBackgroundResource(R.drawable.xml_red_button);
				txtstatus.setTextColor(Color.WHITE);
			}
			else if(map.get("status").equalsIgnoreCase("3")){
				txtstatus.setText("Delevered");	
				txtstatus.setBackgroundResource(R.drawable.xml_green_button);
			}
			else if(map.get("status").equalsIgnoreCase("4")){
				txtstatus.setText("Completed");	
				txtstatus.setBackgroundResource(R.drawable.xml_white_button);
			}
		 
			 
			
		
		
		/*detail = (Button)convertView.findViewById(R.id.detail);
		detail.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,OrderDetailActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra("order_id", map.get("id"));
				context.startActivity(intent);
			}
 

			 
		});
			*/ 
        return convertView;
	}
	

	 	

}

