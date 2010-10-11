package borseth.owen.flagsoftheworld;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class CountryListAdapter extends ArrayAdapter implements Filterable
{
    private ArrayList <HashMap <String, String>> _originalItems;
    private ArrayList <HashMap <String, String>> _items;
    private Context _context;
    private final Object mLock = new Object();
    public ItemsFilter mFilter;

    public CountryListAdapter(Context context, int textViewResourceId, ArrayList <HashMap <String, String>> items) 
    {
            super(context, textViewResourceId, items);
            
            _items = items;
            _context = context;
            _originalItems = _items;
    }
    
    @Override
    public int getCount() 
    {
    	return _items.size();
    }
    
    @Override
    public HashMap <String, String> getItem(int position) 
    {
        return _items.get(position);
    }
    
    @Override
    public long getItemId(int position) 
    {
        return position;
    }

    public Filter getFilter() 
    {
        if (mFilter == null) 
        {
            mFilter = new ItemsFilter();
        }
        return mFilter;
    }
    
    private class ItemsFilter extends Filter 
    {
        protected FilterResults performFiltering(CharSequence prefix) 
        {
        	// Initiate our results object
        	FilterResults results = new FilterResults();
        	// If the adapter array is empty, check the actual items array and use it
            if (_items == null || true) 
            {
                synchronized (mLock) 
                { // Notice the declaration above
                	_items = _originalItems;
                }
            }
            // No prefix is sent to filter by so we're going to send back the original array
        	if (prefix == null || prefix.length() == 0) 
        	{
        		synchronized (mLock) 
        		{
        			results.values = _originalItems;
                    results.count = _originalItems.size();
        		}
        	} 
        	else 
        	{
                 // Compare lower case strings
        		String prefixString = prefix.toString().toLowerCase();
        		
        		// Local to here so we're not changing actual array
                final ArrayList <HashMap <String, String>> localItems = _items;
                final int count = localItems.size();
                final ArrayList <HashMap <String, String>> newItems = new ArrayList <HashMap <String, String>>();

                for (int i = 0; i < count; i++) 
                {
                    final HashMap <String, String> item = _items.get(i);
                    final String itemName = item.get("countryName").toString().toLowerCase();
                    
                    // First match against the whole, non-splitted value
                    if(itemName.startsWith(prefixString)) 
                    {
                        newItems.add(item);
                    } 
                    else 
                    {
                    } 
                }
                
                // Set and return
                results.values = newItems;
                results.count = newItems.size();
            }
        	
        	return results;
        }
        
        protected void publishResults(CharSequence prefix, FilterResults results) 
        {
        	_items = (ArrayList <HashMap <String, String>>) results.values;
        	
        	// Let the adapter know about the updated list
            if (results.count > 0) 
            {
                notifyDataSetChanged();
            } 
            else 
            {
                notifyDataSetInvalidated();
            }
        }
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
            View view = convertView;
            if(view == null) 
            {
                LayoutInflater vi = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R.layout.list_item, null);
            }
            
            HashMap <String, String> itemInfo = _items.get(position);
            
            TextView listCountry = (TextView) view.findViewById(R.id.listCountry);
            listCountry.setText((CharSequence) itemInfo.get("countryName"));
            
        	ImageView flagImage = (ImageView)view.findViewById(R.id.listFlagImage);
        	
        	int flagResID = _context.getResources().getIdentifier(itemInfo.get("flagDrawableName"), "drawable", _context.getPackageName());
        	
        	if(flagResID > 0)
        	{
	        	Bitmap flagBitMap = BitmapFactory.decodeResource(_context.getResources(), flagResID);
	        	
	        	float ratio = Math.min(100 / (float)flagBitMap.getWidth(), 20 / (float)flagBitMap.getHeight());
	        	float width = ratio * flagBitMap.getWidth();
	        	float height = ratio * flagBitMap.getHeight();
	        	
	        	Bitmap flagBitMapScaled = Bitmap.createScaledBitmap(flagBitMap, (int)width, (int)height, true);
	        	
	        	flagImage.setImageBitmap(flagBitMapScaled);
        	}
        	else
        	{
        		flagImage.setImageResource(0);
        	}
            
            return view;
    }
}