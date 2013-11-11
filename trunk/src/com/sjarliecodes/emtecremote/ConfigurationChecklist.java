package com.sjarliecodes.emtecremote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ConfigurationChecklist extends Activity
{	
	private Button m_wifiButton;
	private Button m_preferenceButton;
	private Button m_closeButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Load the GUI    	
     	setContentView(R.layout.configuration_checklist);
		
     	m_wifiButton = (Button)findViewById (R.id.buttonWifi);
     	m_wifiButton.setOnClickListener(new OnClickListener () {
			public void onClick(View i_view) {
				startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
			}
     	});
     	
     	m_preferenceButton = (Button)findViewById (R.id.buttonPreferences);
     	m_preferenceButton.setOnClickListener (new OnClickListener () {
			public void onClick (View i_view) {
				_showSettings ();
			}
     	});
     	
     	m_closeButton = (Button)findViewById (R.id.buttonClose);
     	m_closeButton.setOnClickListener (new OnClickListener () {
			public void onClick (View i_view) {
				finish ();
			}
     	});
	}
	
	private void _showSettings()
    {
    	Intent intent = new Intent(this, Settings.class);
    	startActivity(intent);
    }
}
