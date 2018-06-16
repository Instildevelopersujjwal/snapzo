package adapters;
 

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import imgLoader.AnimateFirstDisplayListener;
import Config.ConstValue;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.developer.snapzo.R;
 
public class SliderImageAdapter extends PagerAdapter{
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    Context context;
    
    private ArrayList<HashMap<String, String>> postItems;
    DisplayImageOptions options;
	ImageLoaderConfiguration imgconfig;
    public SharedPreferences settings;
	public final String PREFS_NAME = "slider";
  //  int[] imageId = {R.drawable.slider1, R.drawable.slider2, R.drawable.slider3, R.drawable.slider4};
     
    public SliderImageAdapter(Context context, ArrayList<HashMap<String, String>> arraylist){
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
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
         
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        final HashMap<String, String> map = postItems.get(position);
        View viewItem = inflater.inflate(R.layout.image_item, container, false);
        ImageView imageView = (ImageView) viewItem.findViewById(R.id.imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoader.getInstance().displayImage(ConstValue.SLIDER_IMAGE_BIG_PATH+map.get("image"), imageView, options, animateFirstListener);
        
        ((ViewPager)container).addView(viewItem);
         
        return viewItem;
    }
 
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
    	return postItems.size();
    }
    public Object getItem(int position) {		
		return postItems.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        // TODO Auto-generated method stub
         
        return view == ((View)object);
    }
 
 
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        ((ViewPager) container).removeView((View) object);
    }
 
}