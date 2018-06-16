package adapters;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.developer.snapzo.R;

public class CityAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<HashMap<String, String>> postItems;
	public SharedPreferences settings;
	public final String PREFS_NAME = "City";
	DisplayImageOptions options;
	ImageLoaderConfiguration imgconfig; 
	int count = 0;
	TextView txtLikes;
	TextView txtContent;
	TextView txtChild;
	
	public CityAdapter(Context context, ArrayList<HashMap<String, String>> arraylist){
		this.context = context;
		
		@SuppressWarnings("unused")
		File cacheDir = StorageUtils.getCacheDirectory(context);
		options = new DisplayImageOptions.Builder()
		//.showImageOnLoading(R.drawable.loading)
		//.showImageForEmptyUri(R.drawable.loading)
	//	.showImageOnFail(R.drawable.loading)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new SimpleBitmapDisplayer())
		.build();
		
			
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
	            convertView = mInflater.inflate(R.layout.row_city, null);	
	        }
			final HashMap<String, String> map = postItems.get(position);
			
			
			TextView txtTitle = (TextView)convertView.findViewById(R.id.textView1);
			txtTitle.setText(map.get("name"));
			
			
        return convertView;
	}
		
		
		
		

}

