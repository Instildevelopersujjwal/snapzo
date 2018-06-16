package fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import util.ConnectionDetector;
import util.ObjectSerializer;
import Config.ConstValue;
import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.developer.snapzo.CheckoutActivity;
import com.developer.snapzo.ViewcartActivity;

public class MyCart {
	Context mContext;
	public SharedPreferences settings;
	public ConnectionDetector cd;
	ArrayList<HashMap<String, String>> cart_array;
	 public MyCart(Context context) 
	    {
	        this.mContext = context;
	        settings = context.getSharedPreferences(ConstValue.CART_PREF, 0);
			cd=new ConnectionDetector(context);
			
			
	        
	    }
	 @SuppressWarnings("unchecked")
	public ArrayList<HashMap<String, String>> get_items(){
		 cart_array = new ArrayList<HashMap<String,String>>();
			try {
				cart_array = (ArrayList<HashMap<String,String>>) ObjectSerializer.deserialize(settings.getString("cart_items", ObjectSerializer.serialize(new ArrayList<HashMap<String,String>>())));		
			}catch (IOException e) {
				    e.printStackTrace();
			}
			return cart_array;
	 }
	 public void empty_cart(){
		 settings.edit().clear().commit();
		

	 }
	public double get_order_total(){
		Double total = 0.0;
		ArrayList<HashMap<String, String>> items = get_items();
		HashMap<String, String> selected_product = new HashMap<String, String>();
		for (int i = 0; i < items.size(); i++) {
			selected_product = items.get(i);
			Double effected_price =  Double.parseDouble(selected_product.get("price"));
			if(!selected_product.get("discount").equalsIgnoreCase("") && !selected_product.get("discount").equalsIgnoreCase("0") )
			{
			Double discount = Double.parseDouble(selected_product.get("discount"));
			Double price = Double.parseDouble(selected_product.get("price"));
			Double discount_amount =  discount * price / 100;
			
			effected_price = price - discount_amount ; 
			}
			effected_price = effected_price * Double.parseDouble(selected_product.get("qty")); 
			total += effected_price;
		}
		
		return total;
	}
	public void put_items_to_session(ArrayList<HashMap<String, String>> products){
		 	try {
				settings.edit().putString("cart_items",ObjectSerializer.serialize(products)).commit();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 }
	 public void remove_item_cart(int position){
		 ArrayList<HashMap<String, String>> products = get_items();
		 products.remove(position);

		 put_items_to_session(products);


	 }
	 
	 
	
	
	
	public void update_item_cart(HashMap<String, String> map, int position){
		 ArrayList<HashMap<String, String>> products = get_items();
		 products.set(position, map);
		 put_items_to_session(products);
	 }
	public int get_total_items(){
		ArrayList<HashMap<String, String>> items = get_items();
		ArrayList<String> itemsCArray = new ArrayList<String>();
		if (items != null) {
			//int total_items = 0;
			HashMap<String, String> pMap = new HashMap<String, String>();
			for(int i = 0 ; i < items.size() ; i ++){
				pMap = items.get(i);
				boolean found = false;
				for (int j = 0; j < itemsCArray.size() ; j++){
					if (itemsCArray.get(j).equalsIgnoreCase(pMap.get("product_id"))){
						found = true;
					}
				}
				if (!found){
					itemsCArray.add(pMap.get("product_id"));
				}
				//int qty = Integer.parseInt(pMap.get("qty")) ;
				//total_items = total_items + qty;
			}
			return itemsCArray.size();
		}
		else
			return 0;
	}
	 public void add_to_cart(HashMap<String, String> map){
		 ArrayList<HashMap<String, String>> products = get_items();
		 if(products != null && products.size() > 0){
			 HashMap<String, String> pMap = new HashMap<String, String>();
		int totalproducts = products.size();
		boolean item_exist = false;
		 for(int i = 0 ; i < totalproducts ; i ++){
			 pMap = products.get(i);
			 	if(pMap.get("id").equalsIgnoreCase(map.get("id"))){
			 		item_exist = true;
			 		//float qty = Float.parseFloat(map.get("qty")) +  Float.parseFloat(pMap.get("qty"));  
			 		int qty = Integer.parseInt(map.get("qty")) + Integer.parseInt(map.get("qty"));
			 		pMap.put("qty", ""+qty);
			 		products.set(i, pMap);
			 	}
 		 }
		 if(!item_exist){
			 products.add(map);
		 }
		 }else{
			 products.add(map);
		 }
		 put_items_to_session(products);
		
	 }
	
	 
	 
}
