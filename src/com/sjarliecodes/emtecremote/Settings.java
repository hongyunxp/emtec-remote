package com.sjarliecodes.emtecremote;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Settings extends PreferenceActivity
{
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Load the XML preferences file
		addPreferencesFromResource(R.xml.settings);
	}
}
