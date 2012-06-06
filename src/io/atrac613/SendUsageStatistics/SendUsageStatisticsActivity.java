package io.atrac613.SendUsageStatistics;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class SendUsageStatisticsActivity extends Activity {
	/** Google Analytics */
    GoogleAnalyticsTracker tracker;
	
	public static final String SEND_USAGE_STATISTICS_ALERT_KEY = "send_usage_statistics_alert_key";
	public static final String SEND_USAGE_STATISTICS_KEY = "send_usage_statistics_key";
	public static final String SEND_USAGE_STATISTICS_PREF = "send_usage_statistics_pref";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        tracker = GoogleAnalyticsTracker.getInstance();
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	boolean send_usage_statistics = prefs.getBoolean(SEND_USAGE_STATISTICS_KEY, false);
        
    	if (send_usage_statistics) {
    		// Start the tracker in manual dispatch mode...
            tracker.startNewSession(AppConstants.GOOGLE_ANALYTICS_TRACKING_ID, AppConstants.GOOGLE_ANALYTICS_DISPATCH_INTERVAL, this);
            
            tracker.trackPageView("/SendUsageStatisticsActivity");
    	}
        
        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	showSendUsageStatisticsAlert(true);
            }
        });
        
        showSendUsageStatisticsAlert(false);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent intent = new Intent(SendUsageStatisticsActivity.this, SettingsActivity.class);
    	intent.setAction(Intent.ACTION_VIEW);
    	startActivity(intent);
    	
        return true;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    
    private void showSendUsageStatisticsAlert(boolean force){
    	final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	boolean send_usage_statistics_alert = prefs.getBoolean(SEND_USAGE_STATISTICS_ALERT_KEY, false);
    	
    	if (!send_usage_statistics_alert || force) {
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(android.R.drawable.ic_dialog_info);
            builder.setMessage(R.string.message_send_usage_statistics);
            
            builder.setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                	SharedPreferences.Editor editor = prefs.edit();
                	editor.putBoolean(SEND_USAGE_STATISTICS_ALERT_KEY, true);
                    editor.putBoolean(SEND_USAGE_STATISTICS_KEY, true);
                    editor.commit();
                }
            });
            builder.setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                	SharedPreferences.Editor editor = prefs.edit();
                	editor.putBoolean(SEND_USAGE_STATISTICS_ALERT_KEY, true);
                    editor.putBoolean(SEND_USAGE_STATISTICS_KEY, false);
                    editor.commit();
                }
            });
            builder.create().show();
    	}
    }
}