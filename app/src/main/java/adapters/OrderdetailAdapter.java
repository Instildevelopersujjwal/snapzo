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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.developer.snapzo.R;


@SuppressLint({ "SimpleDateFormat", "InflateParams" }) public class OrderdetailAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<HashMap<String, String>> postItems;
	public SharedPreferences settings;
	public final String PREFS_NAME = "OrderDetail";
	Common common;
	DisplayImageOptions options;
	ImageLoaderConfiguration imgconfig; 
	ProgressDialog dialog;
	OnDataChangeListener mOnDataChangeListener;
	public Double total_amount = 0.0; 
	public OrderdetailAdapter(Context context, ArrayList<HashMap<String, String>> arraylist){
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
	public Double getTotal(){
		Double total = 0.0;
		for (int i = 0; i < getCount(); i++) {
			HashMap<String, String> map = postItems.get(i);
			int qty = Integer.parseInt(map.get("qty"));
			Double price = Double.parseDouble(map.get("price"));
			total = total + (qty * price);
		}
		return total;
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

	@SuppressLint("CutPasteId") @Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
			if (convertView == null) {
	            LayoutInflater mInflater = (LayoutInflater)
	            context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	            convertView = mInflater.inflate(R.layout.row_orderdetail, null);	
	            
	        }
			final HashMap<String, String> map = postItems.get(position);
			
			
				
			TextView txtqty = (TextView)convertView.findViewById(R.id.qty);
			txtqty.setText(map.get("qty"));
			TextView txtdesign = (TextView)convertView.findViewById(R.id.proname);
			txtdesign.setText(map.get("title"));

			TextView txtrate = (TextView)convertView.findViewById(R.id.price);
			txtrate.setText(map.get("price"));
			
			TextView txttype = (TextView)convertView.findViewById(R.id.unitype);
			txttype.setText(map.get("gmqty")+map.get("type"));
		 
			int a = Integer.parseInt(txtqty.getText().toString().trim());
			Double b = Double.parseDouble(txtrate.getText().toString().trim());
			
		
			Double result = a * b;
			total_amount  += result;
			TextView txtamount = (TextView)convertView.findViewById(R.id.amount);
			txtamount.setText(String.valueOf(result));
			
			
	  
        return convertView;
	}
	
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		if(mOnDataChangeListener != null){
	        mOnDataChangeListener.onDataChanged(getTotal());
	    }
		super.notifyDataSetChanged();
	}

	public interface OnDataChangeListener{
	    public void onDataChanged(Double total);
	}
	public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener){
	    mOnDataChangeListener = onDataChangeListener;
	}
	
}

