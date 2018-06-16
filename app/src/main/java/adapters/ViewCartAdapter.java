package adapters;

import fragments.MyCart;
import imgLoader.AnimateFirstDisplayListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import Config.ConstValue;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.snapzo.R;

import static android.content.Context.MODE_PRIVATE;

public class ViewCartAdapter extends BaseAdapter {
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	SharedPreferences.OnSharedPreferenceChangeListener listener;
	private Context context;
	private ArrayList<HashMap<String, String>> postItems;
	public SharedPreferences settings;
	public final String PREFS_NAME = "Products";
	TextView txtprice, txtqty;
	DisplayImageOptions options;
	ImageLoaderConfiguration imgconfig;
	private TextView mTxtAmountAdapter;

	public ViewCartAdapter(Context context, ArrayList<HashMap<String, String>> arraylist){
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

	@SuppressLint("InflateParams") @Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
			if (convertView == null) {
	            LayoutInflater mInflater = (LayoutInflater)
	            context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	            convertView = mInflater.inflate(R.layout.row_cart, null);	
	        }
			final HashMap<String, String> map = postItems.get(position);
			
			ImageView imgProduct = (ImageView)convertView.findViewById(R.id.proimage);
			ImageLoader.getInstance().displayImage(ConstValue.PRO_IMAGE_BIG_PATH+map.get("image"), imgProduct, options, animateFirstListener);
			
			TextView txtTitle = (TextView)convertView.findViewById(R.id.proTitle);
			txtTitle.setText(map.get("title"));
			
			
			
			 txtprice = (TextView)convertView.findViewById(R.id.txtprice);
			 txtprice.setText(map.get("price"));
			
			 TextView textDiscount = (TextView)convertView.findViewById(R.id.textDiscount);
				textDiscount.setVisibility(View.GONE);
				
				
				TextView textCurrency = (TextView)convertView.findViewById(R.id.textCurrency);
				textCurrency.setText(map.get("currency"));
				
				if(!map.get("discount").equalsIgnoreCase("") && !map.get("discount").equalsIgnoreCase("0")){
					Double discount = Double.parseDouble(map.get("discount"));
					Double price = Double.parseDouble(map.get("price"));
					Double discount_amount =  discount * price / 100;
					
					Double effected_price = price - discount_amount ; 
					//txtPrice.setBackgroundResource(R.drawable.strike_trough);
					txtprice.setPaintFlags(txtprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
					textDiscount.setVisibility(View.VISIBLE);
					textDiscount.setText(effected_price.toString());
					
				}
				
			 
		    txtqty = (TextView)convertView.findViewById(R.id.textqty);


			txtqty.setText(map.get("qty"));



	//	SharedPreferences sp = context.getSharedPreferences("item", MODE_PRIVATE);
	//	SharedPreferences.Editor editor = sp.edit();
	//	editor.putInt("qty_update", 1);
	//	editor.commit();
		//	Toast.makeText(getApplicationContext(),"edited",Toast.LENGTH_SHORT).show();






			
			 Double value1 = Double.parseDouble(txtqty.getText().toString());
			    Double value2 = Double.parseDouble(txtprice.getText().toString());
			    Double calculatedValue = value1*value2; 
			    txtprice.setText(calculatedValue.toString());
			
			TextView txtUnit = (TextView)convertView.findViewById(R.id.txtunit);
			txtUnit.setText(map.get("gmqty"));
			
			TextView txtgm = (TextView)convertView.findViewById(R.id.txtgm);
			txtgm.setText(map.get("unit"));
			
			
			
			
			ImageView imgdelete=(ImageView)convertView.findViewById(R.id.imageView1);
			imgdelete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 MyCart cart = new MyCart(context);
					 cart.remove_item_cart(position);
					 postItems = cart.get_items();
					Toast.makeText(context,context.getResources().getString(R.string.viewcartadapter_product_removed),Toast.LENGTH_SHORT).show();
					notifyDataSetChanged();

					SharedPreferences sp = context.getSharedPreferences("item", MODE_PRIVATE);
					SharedPreferences.Editor editor = sp.edit();
					editor.putInt("value", 0);
					editor.commit();
				//	Toast.makeText(context,"edited",Toast.LENGTH_SHORT).show();

				//	Intent intent = new Intent(context, ViewcartActivity.class);
					//context.startActivity(intent);

				}
			

				
			});
			
			notifyDataSetChanged();			
        return convertView;
	}


}

