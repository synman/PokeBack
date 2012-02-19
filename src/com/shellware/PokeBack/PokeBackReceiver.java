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
    private static final String POKED_KEYPHRASE = "You have been poked on Facebook by ";
    
	@Override
	public void onReceive(Context ctx, Intent intent) {
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        
		if (!prefs.getBoolean("receiver_active", true)) {
        	Log.d(TAG, "Service Disabled");
			return;
		}
		
		
        Bundle bundle = intent.getExtras();        
        SmsMessage[] msgs = null;

        if (bundle != null) {
        	final long lastMessageTimestamp = prefs.getLong("last_message_timestamp", 0);
        	final long totalMessageCount = prefs.getLong("total_message_count", 0);
        	
        	Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];            

            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
            	final String msg = msgs[i].getMessageBody();

                if (msgs[i].getMessageBody() != null && msg.startsWith(POKED_KEYPHRASE) &&
                		msgs[i].getTimestampMillis() > lastMessageTimestamp) {
                	
                	SmsManager sm = SmsManager.getDefault();
                	sm.sendTextMessage(msgs[i].getOriginatingAddress(), null, "P", null, null);
                	       
                	String senderName = msg.replace(POKED_KEYPHRASE, "");
                	senderName = senderName.substring(0, senderName.indexOf("."));
                	
                	Editor edit = prefs.edit();
                	edit.putLong("last_message_timestamp", msgs[i].getTimestampMillis());
                	edit.commit();
                	
                	edit.putLong("total_message_count", totalMessageCount + 1);
                	edit.commit();
                	
                	edit.putInt("stats_for_" + senderName.replace(" ", "_"), prefs.getInt("stats_for_" + senderName.replace(" ", "_"), 0) + 1);
                	edit.commit();
                	
                	Log.d(TAG, "poked by " + senderName + " timestamp: " + msgs[i].getTimestampMillis());
                }
            }
        }                         		
	}
}
