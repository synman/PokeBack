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
