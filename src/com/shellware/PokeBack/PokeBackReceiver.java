package com.shellware.PokeBack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class PokeBackReceiver extends BroadcastReceiver {
    private static final String TAG = "PokeBack";
    private static final String POKED_KEYWORD = "You have been poked on Facebook";
    
	@Override
	public void onReceive(Context ctx, Intent intent) {
        Bundle bundle = intent.getExtras();        
        SmsMessage[] msgs = null;

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		        
        if (bundle != null) {
        	final long lastMessageTimestamp = prefs.getLong("last_message_timestamp", 0);
        	
        	Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];            

            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                
                if (msgs[i].getMessageBody() != null && msgs[i].getMessageBody().startsWith(POKED_KEYWORD) &&
                		msgs[i].getTimestampMillis() > lastMessageTimestamp) {
                	
                	SmsManager sm = SmsManager.getDefault();
                	sm.sendTextMessage(msgs[i].getOriginatingAddress(), null, "P", null, null);
             
                	Editor edit = prefs.edit();
                	edit.putLong("last_message_timestamp", msgs[i].getTimestampMillis());
                	edit.commit();
                	
                	Log.d(TAG, "poked  timestamp: " + msgs[i].getTimestampMillis());
                }
            }
        }                         		
	}
}
