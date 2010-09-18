package borseth.owen.flagsoftheworld;

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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FlagsOfTheWorld extends ListActivity 
{	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        String[] countries = getResources().getStringArray(R.array.country_array);
        String[] countryInfo = getResources().getStringArray(R.array.country_info);
        
        final HashMap<String, String> countryInfoMap = new HashMap<String, String>();
        
        for(int i = 0; i < countries.length; i++)
        {
        	countryInfoMap.put(countries[i], countryInfo[i]);
        }
        
        setListAdapter(new ArrayAdapter<String>(this, R.layout.main, countries));

        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        
        lv.setOnItemClickListener(new OnItemClickListener() 
        {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
        	{
        		Context context = view.getContext();
        		
	        	String countryName = (String)((TextView) view).getText();
	        	String countryInfo = countryInfoMap.get(countryName);

	        	String[] countryInfoArray = countryInfo.split(":");
	        	// String[] countryInfoArray = StringUtils.splitPreserveAllTokens(countryInfo, ":");
	        	
	        	String fipsCode = countryInfoArray[1].toLowerCase();
	        	String capital = countryInfoArray[2];
	        	String population = countryInfoArray[3];
	        	String tld = countryInfoArray[4];
	        	String currencyCode = countryInfoArray[5];
	        	String currencyName = countryInfoArray[6];
	        	String calling = countryInfoArray[7];
	        	
	        	String currency = currencyName+" ("+currencyCode+")";
	        	
	        	String mDrawableName = fipsCode+"_flag_small";
	        	int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
	        	
        		Dialog d = new Dialog(context);
        		d.setTitle(countryName);
        		d.setContentView(R.layout.image);
        		
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
        		
	        	ImageView image = (ImageView)d.findViewById(R.id.image);
	        	image.setImageResource(resID);
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