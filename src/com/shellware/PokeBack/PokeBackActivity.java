/*
 *   Copyright 2011-2012 Shell M. Shrader
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.shellware.PokeBack;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class PokeBackActivity extends Activity {

	private SharedPreferences prefs;
	private Resources res;
	
	private GridView statsGrid;
	
//	private Menu myMenu = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		res = getResources();
		
		statsGrid = (GridView) findViewById(R.id.statsGrid);
		int totalStats = 1; 
		
		ImageView infoImage = (ImageView) findViewById(R.id.infoImage);
		infoImage.setOnTouchListener( new  View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				if( event.getAction() == MotionEvent.ACTION_DOWN ) {
					v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
					v.playSoundEffect(SoundEffectConstants.CLICK);
	        		Intent helpIntent = new Intent()
	        			.setAction(Intent.ACTION_VIEW)
	        			.setData(Uri.parse(Html.fromHtml(res.getString(R.string.help_url)).toString()));
        		startActivity(helpIntent);
				}
				return false;
			}
		});
		
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
		
        ChangeLog cl = new ChangeLog(this);
        if (cl.firstRun()) {
            cl.getLogDialog().show();
        }
	}

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
//		myMenu = menu;
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		
        // Handle item selection
        switch (item.getItemId()) {
	        case android.R.id.home:
	        	ChangeLog cl = new ChangeLog(this);
	        	cl.getFullLogDialog().show();
	        	return true;
        	case R.id.helpMenuItem:
        		Intent helpIntent = new Intent()
        			.setAction(Intent.ACTION_VIEW)
        			.setData(Uri.parse(Html.fromHtml(res.getString(R.string.help_url)).toString()));
        		startActivity(helpIntent);
        		return true;
        	case R.id.quitMenuItem:
	           	System.exit(0);
	           	return true;
        	default:
                return super.onOptionsItemSelected(item);
        }
    }    

	
}
