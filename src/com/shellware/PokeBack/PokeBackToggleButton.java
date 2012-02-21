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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

public class PokeBackToggleButton extends android.widget.ToggleButton {

private SharedPreferences prefs;
private Context ctx;

	public PokeBackToggleButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		 
		ctx = context;
		prefs = PreferenceManager.getDefaultSharedPreferences(ctx);

		if (prefs != null) this.setChecked(prefs.getBoolean("receiver_active", true));
	}
	
	public PokeBackToggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		 
		ctx = context;
		prefs = PreferenceManager.getDefaultSharedPreferences(ctx);

		if (prefs != null) this.setChecked(prefs.getBoolean("receiver_active", true));
	}

	public PokeBackToggleButton(Context context) {
		super(context);
 
		ctx = context;
		prefs = PreferenceManager.getDefaultSharedPreferences(ctx);

		if (prefs != null) this.setChecked(prefs.getBoolean("receiver_active", true));
	}

	@Override
	public void toggle() {
		super.toggle();
		
		Editor edit = prefs.edit();
		
		edit.putBoolean("receiver_active", this.isChecked());
		edit.commit();
	}

	
}
