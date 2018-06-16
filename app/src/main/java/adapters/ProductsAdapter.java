package adapters;

import imgLoader.AnimateFirstDisplayListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.developer.snapzo.ProductCategory;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import Config.ConstValue;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.snapzo.R;

public class ProductsAdapter extends BaseAdapter {
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	private Context context;
	private ArrayList<HashMap<String, String>> postItems;
	public SharedPreferences settings;
	public final String PREFS_NAME = "Products";
	
	DisplayImageOptions options;
	ImageLoaderConfiguration imgconfig; 
	
	public ProductsAdapter(Context context, ArrayList<HashMap<String, String>> arraylist){
		this.context = context;
		
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
	            convertView = mInflater.inflate(R.layout.row_products, null);	
	        }
			final HashMap<String, String> map = postItems.get(position);
			
			ImageView imgProduct = (ImageView)convertView.findViewById(R.id.proimage);
			ImageLoader.getInstance().displayImage(ConstValue.PRO_IMAGE_BIG_PATH+map.get("image"), imgProduct, options, animateFirstListener);
			
			TextView txtTitle = (TextView)convertView.findViewById(R.id.proTitle);
			txtTitle.setText(map.get("title"));
			
			TextView txtPrice = (TextView)convertView.findViewById(R.id.txtprice);
			txtPrice.setText(map.get("price"));
			
			TextView textDiscount = (TextView)convertView.findViewById(R.id.textDiscount);
			textDiscount.setVisibility(View.GONE);
			
			TextView txtDiscountFlag = (TextView)convertView.findViewById(R.id.textDiscountFlag);
			txtDiscountFlag.setVisibility(View.GONE);
			
			TextView textCurrency = (TextView)convertView.findViewById(R.id.textCurrency);
			textCurrency.setText(map.get("currency"));
			
			if(!map.get("discount").equalsIgnoreCase("") && !map.get("discount").equalsIgnoreCase("0")){
				Double discount = Double.parseDouble(map.get("discount"));
				Double price = Double.parseDouble(map.get("price"));
				Double discount_amount =  discount * price / 100;
				
				Double effected_price = price - discount_amount ; 
				//txtPrice.setBackgroundResource(R.drawable.strike_trough);
				txtPrice.setPaintFlags(txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
				textDiscount.setVisibility(View.VISIBLE);
				textDiscount.setText(effected_price.toString());
				
				txtDiscountFlag.setVisibility(View.VISIBLE);
				txtDiscountFlag.setText(discount+"% off");
			}
			
			TextView txtUnit = (TextView)convertView.findViewById(R.id.txtunit);
			txtUnit.setText(map.get("gmqty"));
			
			TextView txtgm = (TextView)convertView.findViewById(R.id.txtgm);
			txtgm.setText(map.get("unit"));
			
			int a = Integer.parseInt(map.get("total_qty_stock"));
			int b = Integer.parseInt(map.get("consume_qty_stock"));
		  //  int a = 5;
		  //  int b = 5 ;
			int result = a - b;
			
			TextView txtstock = (TextView)convertView.findViewById(R.id.stock);
			txtstock.setText(String.valueOf(result)+" "+map.get("type")+" In Stock");
			//txtstock.setText(map.get("stock")+" "+map.get("type")+" In Stock");
			
						
        return convertView;
	}


	public void setClickListener(ProductCategory productCategory) {
	}
}

