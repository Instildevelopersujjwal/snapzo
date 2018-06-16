package Config;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.R.string;

import com.paypal.android.sdk.payments.PayPalConfiguration;

public class ConstValue {

	//---Client ID for Paypal. Create your own app and add client id here. https://developer.paypal.com/developer/applications/
	public static final String CONFIG_CLIENT_ID = "Aa4VQ8QL_oWS78tLrR9voNg-Pqi0lu3bcCMisE1Lez_OKwXQgo4t5mpP5dSxauY8JVkaowHc8AVFfJxX";
	/**
	 * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
	 *
	 * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test credentials
	 * from https://developer.paypal.com
	 *
	 * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
	 * without communicating to PayPal's servers.
	 */
	public static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
	public static final String CURRENCY = "₹";

	public static String SITE_URL = "http://app.snapzo.in";
	public static String JSON_LOGIN = SITE_URL+"/index.php?component=json&action=login";
	public static  String JSON_FORGOT = SITE_URL+"/index.php?component=json&action=forgot";
	public static String JSON_REGISTER=SITE_URL+"/index.php?component=json&action=signup";
	public static String JSON_CITY=SITE_URL+"/index.php?component=json&action=get_city";
	public static String JSON_CATEGORY=SITE_URL+"/index.php?component=json&action=get_categories";
	public static String JSON_CATEGORY_A=SITE_URL+"/index.php?component=json&action=get_categories_a";

	public static String JSON_PRODUCTS=SITE_URL+"/index.php?component=json&action=get_products_by_category";
	public static String JSON_PRODUCTS2=SITE_URL+"/index.php?component=json&action=get_products_by_category2";

	public static String JSON_PRODUCTS_DETAIL=SITE_URL+"/index.php?component=json&action=get_product_detail";
	public static String JSON_PROFILE_UPDATE=SITE_URL+"/index.php?component=json&action=profile_update";
	public static String JSON_ADD_ORDER=SITE_URL+"/index.php?component=json&action=add_order";
	public static String JSON_LIST_ORDER=SITE_URL+"/index.php?component=json&action=list_order";
	public static String JSON_ORDER_DETAIL=SITE_URL+"/index.php?component=json&action=order_detail";
	public static String JSON_SLIDER_IMAGE=SITE_URL+"/index.php?component=json&action=get_slider_image";
	public static String JSON_CANCLE_ORDER=SITE_URL+"/index.php?component=json&action=cancle_order";

	public static String JSON_SETTINGS=SITE_URL+"/index.php?component=json&action=get_settings";
	
	public static String CAT_IMAGE_BIG_PATH=SITE_URL+"/userfiles/contents/big/";
	public static String CAT_IMAGE_SMALL_PATH=SITE_URL+"/userfiles/contents/small/";
	public static String CAT_IMAGE_ICON_PATH=SITE_URL+"/userfiles/contents/icon/";
	
	public static String SLIDER_IMAGE_BIG_PATH=SITE_URL+"/userfiles/contents/big/";
	public static String SLIDER_IMAGE_SMALL_PATH=SITE_URL+"/userfiles/contents/small/";
	public static String SLIDER_IMAGE_ICON_PATH=SITE_URL+"/userfiles/contents/icon/";
	
	public static String PRO_IMAGE_BIG_PATH=SITE_URL+"/userfiles/products/big/";
	public static String PRO_IMAGE_SMALL_PATH=SITE_URL+"/userfiles/products/small/";
	public static String PRO_IMAGE_ICON_PATH=SITE_URL+"/userfiles/products/icon/";
	
	public static String IMAGE_PROFILE_PATH=SITE_URL+"/userfiles/profile/big/";
	
	public static String GCM_SENDER_ID = "720391900040";
	
	public static String MAIN_PREF = "snapzo";
	public static String CART_PREF = "snapzoCart";
	public static String PREFS_MAIN_CAT = "prefs_main_category";
	
	public static HashMap<String, String> selected_service; 
	public static HashMap<String, String> selected_job;
	public static HashMap<String, String> selected_city;
	
	public static JSONObject selected_clinic;

	public static final String IMAGE_DIRECTORY_NAME = "snapzo";
	
	public static String SIGNUP_SERVICE =SITE_URL+"/index.php?component=json&action=register_gcm";

	public static String SATISFACTION=SITE_URL+"/index.php?component=json&action=add_satisfaction";

	
	
}
