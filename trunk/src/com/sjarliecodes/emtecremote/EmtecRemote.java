package com.sjarliecodes.emtecremote;

import android.app.Application;
import android.content.Intent;

public class EmtecRemote extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// Start services needed by the app
		startService (new Intent (EmtecRemote.this,
								  Commander.class));
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		
		// Stop services needed by the app
		stopService (new Intent (EmtecRemote.this,
				 				 Commander.class));
	}

}
