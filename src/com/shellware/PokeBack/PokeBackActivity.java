package com.shellware.PokeBack;

import java.util.Map;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.GridView;

public class PokeBackActivity extends Activity {

	private SharedPreferences prefs;
	private GridView statsGrid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		statsGrid = (GridView) findViewById(R.id.statsGrid);
		int totalStats = 1; 
		
		Map<String,?> prefsMap = prefs.getAll();
		
		for(Map.Entry<String,?> entry : prefsMap.entrySet()) {
			if (entry.getKey().startsWith("stats_for_")) {
				totalStats++;
			}
		}
		
		String[] gridItems = new String[totalStats * 2];
		
		gridItems[0] = "Grand Total";
		gridItems[1] = ((Long) prefs.getLong("total_message_count", 0)).toString();
		
		int counter = 1;
		for(Map.Entry<String,?> entry : prefsMap.entrySet()) {
			if (entry.getKey().startsWith("stats_for_")) {
				counter++;
				gridItems[counter] = entry.getKey().replace("stats_for_", "").replace("_", " ");
				counter++;
				gridItems[counter] = ((Integer) entry.getValue()).toString();
			}
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, gridItems);
		statsGrid.setAdapter(adapter);
	}

	
}
