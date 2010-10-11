package borseth.owen.flagsoftheworld;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FlagsOfTheWorld extends ListActivity 
{	
    public HashMap<String, HashMap<String, String>> _countryInfoMap = new HashMap<String, HashMap<String, String>>();
    public ArrayList <HashMap<String, String>> _countryInfoList = new ArrayList <HashMap<String, String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        String[] countryInfo = getResources().getStringArray(R.array.country_info);
        
        for(int i = 0; i < countryInfo.length; i++)
        {
        	HashMap<String, String> tmpMap = new HashMap<String, String>();
        	String[] countryInfoArray = countryInfo[i].split(":");
        	
        	tmpMap.put("countryName", countryInfoArray[0]);
        	tmpMap.put("fipsCode", countryInfoArray[1].toLowerCase());
        	tmpMap.put("capital", countryInfoArray[2]);
        	tmpMap.put("population", countryInfoArray[3]);
        	tmpMap.put("tld", countryInfoArray[4]);
        	tmpMap.put("currencyCode", countryInfoArray[5]);
        	tmpMap.put("currencyName", countryInfoArray[6]);
        	tmpMap.put("calling", countryInfoArray[7]);
        	tmpMap.put("currency", countryInfoArray[6]+" ("+countryInfoArray[5]+")");
        	
        	String flagDrawableName = countryInfoArray[1].toLowerCase()+"_flag_small";
        	String mapDrawableName = countryInfoArray[1].toLowerCase()+"_map_small";
        	
        	tmpMap.put("flagDrawableName", flagDrawableName);
        	tmpMap.put("mapDrawableName", mapDrawableName);
        	
        	_countryInfoMap.put(countryInfoArray[0], tmpMap);
        	_countryInfoList.add(tmpMap);
        }
        
        setListAdapter(new CountryListAdapter(this, R.layout.list_item, _countryInfoList));

        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        
        lv.setOnItemClickListener(new OnItemClickListener() 
        {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
        	{
        		Context context = view.getContext();
        		
        		TextView listCountry = (TextView)view.findViewById(R.id.listCountry);
	        	String countryName = (String)listCountry.getText();
	        	HashMap<String, String> countryInfo = _countryInfoMap.get(countryName);

	        	String fipsCode = countryInfo.get("fipsCode");
	        	String capital = countryInfo.get("capital");
	        	String population = countryInfo.get("population");
	        	String tld = countryInfo.get("tld");
	        	String currencyCode = countryInfo.get("currencyCode");
	        	String currencyName = countryInfo.get("currencyName");
	        	String calling = countryInfo.get("calling");
	        	String currency = countryInfo.get("currency");
	        	String flagDrawableName = countryInfo.get("flagDrawableName");
	        	String mapDrawableName = countryInfo.get("mapDrawableName");
	        	
	        	int flagResID = getResources().getIdentifier(flagDrawableName , "drawable", getPackageName());
	        	int mapResID = getResources().getIdentifier(mapDrawableName , "drawable", getPackageName());
	        	
        		Dialog d = new Dialog(context);
        		d.setTitle(countryName);
        		d.setContentView(R.layout.country_dialog);
        		
        		TextView capitalText = (TextView)d.findViewById(R.id.capital);
        		capitalText.setText(capital);
        		TextView populationText = (TextView)d.findViewById(R.id.population);
        		populationText.setText(population);
        		TextView currencyText = (TextView)d.findViewById(R.id.currency);
        		currencyText.setText(currency);
        		TextView callingText = (TextView)d.findViewById(R.id.calling);
        		callingText.setText(calling);
        		TextView tldText = (TextView)d.findViewById(R.id.tld);
        		tldText.setText(tld);
        		
	        	ImageView flagImage = (ImageView)d.findViewById(R.id.flagImage);
	        	flagImage.setImageResource(flagResID);
	        	
	        	ImageView mapImage = (ImageView)d.findViewById(R.id.mapImage);
	        	mapImage.setImageResource(mapResID);
	        	
	        	LayoutParams params = d.getWindow().getAttributes();
	        	params.height = LayoutParams.FILL_PARENT;
	        	params.width = LayoutParams.FILL_PARENT;
	        	d.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        		d.show();
        	}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        switch (item.getItemId()) {
        case R.id.show_keyboard: 
        	InputMethodManager imm = (InputMethodManager)this.getWindow().getContext().getSystemService (Context.INPUT_METHOD_SERVICE);
        	imm.toggleSoftInput (0, 0);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}