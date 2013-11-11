package com.sjarliecodes.emtecremote;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class Remote extends Activity {
	
	private Commander m_commander;
	
	private ProgressDialog m_progressDialog;
	private AlertDialog m_alertDialog;
	private Vibrator m_vibrator;
	
	private VerticalView.OnClickListener m_verticalViewListener = new VerticalView.OnClickListener()
	{	
		public void onClick(View i_view, VerticalView.eButton i_button)
		{
			if (i_button != VerticalView.eButton.NONE)
			{
				// vibrate shortly
		    	m_vibrator.vibrate(50);
			}
	    	
	    	// do something when the button is clicked
			switch (i_button)
			{
			case UP:
				m_commander.Execute(getString (R.string.up));
				break;
			case LEFT:
				m_commander.Execute(getString (R.string.left));
				break;
			case DOWN:
				m_commander.Execute(getString (R.string.down));
				break;
			case RIGHT:
				m_commander.Execute(getString (R.string.right));
				break;
			case OK:
				m_commander.Execute(getString (R.string.select));
				break;
			case MUTE:
				m_commander.Execute(getString (R.string.mute));
				break;
			case INFO:
				m_commander.Execute(getString (R.string.display));
				break;
			case REWIND:
				m_commander.Execute(getString (R.string.fast_rewind));
				break;
			case FORWARD:
				m_commander.Execute(getString (R.string.fast_forward));
				break;
			case PREVIOUS:
				m_commander.Execute(getString (R.string.previous));
				break;
			case STOP:
				m_commander.Execute(getString (R.string.stop));
				break;
			case PLAYPAUSE:
				m_commander.Execute(getString (R.string.play_pause));
				break;
			case RECORD:
				m_commander.Execute(getString (R.string.record));
				break;
			case NEXT:
				m_commander.Execute(getString (R.string.next));
				break;
			case ZERO:
				m_commander.Execute(getString (R.string.zero));
				break;
			case ONE:
				m_commander.Execute(getString (R.string.one));
				break;
			case TWO:
				m_commander.Execute(getString (R.string.two));
				break;
			case THREE:
				m_commander.Execute(getString (R.string.three));
				break;
			case FOUR:
				m_commander.Execute(getString (R.string.four));
				break;
			case FIVE:
				m_commander.Execute(getString (R.string.five));
				break;
			case SIX:
				m_commander.Execute(getString (R.string.six));
				break;
			case SEVEN:
				m_commander.Execute(getString (R.string.seven));
				break;
			case EIGHT:
				m_commander.Execute(getString (R.string.eight));
				break;
			case NINE:
				m_commander.Execute(getString (R.string.nine));
				break;
			case RED:
				m_commander.Execute(getString (R.string.red));
				break;
			case GREEN:
				m_commander.Execute(getString (R.string.epg));
				break;
			case YELLOW:
				m_commander.Execute(getString (R.string.teletext));
				break;
			case BLUE:
				m_commander.Execute(getString (R.string.subtitles));
				break;
			case HOME:
				m_commander.Execute(getString (R.string.home));
				break;
			case MENU:
				openOptionsMenu ();
				break;
			case BACK:
				m_commander.Execute(getString (R.string.back));
				break;
			}
		}
	};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
        super.onCreate(savedInstanceState);
        
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
    	Log.Initialize();
    	
     	setContentView(R.layout.remote);
     	
     	getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	
        m_vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        
    	// Capture our buttons from layout
        // and register the onClick listener with the implementation above
        ((VerticalView)findViewById(R.id.verticalView1)).setOnClickListener (m_verticalViewListener);
        
        // check if this is the first time the application is ran
        if (!_checkFirstRun ())
        {
        	// show progress dialog (hidden by action from onResume)
        	m_progressDialog = ProgressDialog.show(this, "Connection", "Connecting to your Movie Cube", true, false);
        }
    }
    
    @Override
	protected void onResume() {
		super.onResume();
		
		// connect to the communication service
		m_commander = Commander.GetCommander(this);
		
		// test the service to verify if settings are correct
		m_commander.TestConnection ();
	}
    
    @Override
    public boolean dispatchKeyEvent (KeyEvent event)
    {
    	int action = event.getAction();
    	int keyCode = event.getKeyCode();
    	switch (keyCode)
    	{
    	case KeyEvent.KEYCODE_VOLUME_UP:
    		if (action == KeyEvent.ACTION_DOWN)
    		{
    			m_commander.Execute(getString(R.string.volume_up));
    		}
    		return true;
    	case KeyEvent.KEYCODE_VOLUME_DOWN:
    		if (action == KeyEvent.ACTION_DOWN)
    		{
    			m_commander.Execute(getString(R.string.volume_down));
    		}
    		return true;
    	case KeyEvent.KEYCODE_DPAD_CENTER:
    		if (action == KeyEvent.ACTION_DOWN)
    		{
    			m_commander.Execute(getString(R.string.select));
    		}
    		return true;
    	case KeyEvent.KEYCODE_DPAD_DOWN:
    		if (action == KeyEvent.ACTION_DOWN)
    		{
    			m_commander.Execute(getString(R.string.down));
    		}
    		return true;
    	case KeyEvent.KEYCODE_DPAD_LEFT:
    		if (action == KeyEvent.ACTION_DOWN)
    		{
    			m_commander.Execute(getString(R.string.left));
    		}
    		return true;
    	case KeyEvent.KEYCODE_DPAD_RIGHT:
    		if (action == KeyEvent.ACTION_DOWN)
    		{
    			m_commander.Execute(getString(R.string.right));
    		}
    		return true;
    	case KeyEvent.KEYCODE_DPAD_UP:
    		if (action == KeyEvent.ACTION_DOWN)
    		{
    			m_commander.Execute(getString(R.string.up));
    		}
    		return true;
    	default:
    		return super.dispatchKeyEvent(event);
    	}
    }
    
    /*public void onStart()
    {
    	
    }
    
    public void onRestart()
    {
    	
    }
    
    public void onResume()
    {
    	
    }
    
    public void onPause()
    {
    	
    }*/
    
    @Override
    public void onStop()
    {
    	super.onStop();
    }
    
    public void onDestroy()
    {
    	super.onDestroy();
    }
    
    /** On creating the menu for the first time, we inflate
     *  it to add behavior to it. */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
    	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	// Handle item selection
    	switch (item.getItemId())
    	{
    	case R.id.settings:
    		showSettings();
    		return true;
    	case R.id.checklist:
    		showChecklist();
    		return true;
    	case R.id.log:
    		showLog();
    		return true;
        default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    public void AutoDiscoverMovieCube ()
    {
    	// show dialog
    	m_progressDialog = ProgressDialog.show(this, "Connection", "Searching your Movie Cube.", true, false);
    	
    	// tell commander to discover the IP address
    	m_commander.AutoDiscover();
    }
    
    public void showSettings()
    {
    	Intent intent = new Intent(this, Settings.class);
    	startActivity(intent);
    }
    
    public void showChecklist()
    {
    	Intent intent = new Intent(this, ConfigurationChecklist.class);
    	startActivity(intent);
    }
    
    public void showLog()
    {    	
    	Intent intent = new Intent(this, Log.class);
    	startActivity(intent);
    }
    
    /*!
     *  Sink for callbacks from the commander
     */
    public void CommanderResult (Commander.eResult i_result)
    {
    	boolean withDialog = false;
    	if (m_progressDialog != null && m_progressDialog.isShowing ())
    	{
    		m_progressDialog.dismiss ();
    		withDialog = true;
    	}
    	
    	AlertDialog.Builder dialogBuilder = null;
    	
    	switch (i_result)
    	{
    	case SUCCESS:
    		if (withDialog)
    		{
    			Toast.makeText (Remote.this, "Connected!", Toast.LENGTH_SHORT).show();
    		}
    		break;
    	case ERROR_WIFI:
    		// host cannot be reached, let user choose what to do
    		if (m_alertDialog == null || !m_alertDialog.isShowing())
    		{
				dialogBuilder = new AlertDialog.Builder (this);
				dialogBuilder.setMessage("You must connect to your home network to use the remote.")
						     .setCancelable(false)
						     .setTitle("Connection")
						     .setPositiveButton("Setup wifi", new DialogInterface.OnClickListener() {
						    	 public void onClick(DialogInterface dialog, int id) {
						    		 startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
						         }
						     })
					         .setNegativeButton("Don't mind", new DialogInterface.OnClickListener() {
					        	 public void onClick(DialogInterface dialog, int id) {}
						     });
				m_alertDialog = dialogBuilder.create();
				m_alertDialog.show();
    		}
    		break;
    	case ERROR_HOST:
    	case ERROR_PORT:
    		// host cannot be reached, let user choose what to do
    		if (m_alertDialog == null || !m_alertDialog.isShowing())
    		{
				dialogBuilder = new AlertDialog.Builder (this);
				dialogBuilder.setMessage("Could not connect to your Movie Cube.\nMake sure that:\n1. Wifi is on\n2. The IP address and port are set\n3. Remote is enabled on your Movie Cube")
			       .setCancelable(false)
			       .setTitle("Connection")
			       .setPositiveButton("Auto discover", new DialogInterface.OnClickListener() {
			           public void onClick (DialogInterface dialog, int id) {
			        	   AutoDiscoverMovieCube ();
			           }
			       })
			       .setNeutralButton("Preferences", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   showSettings();
			           }
			       })
		           .setNegativeButton("Don't mind", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			           }
			       });
				m_alertDialog = dialogBuilder.create();
				m_alertDialog.show();
    		}
    		break;
    	default:
    		Toast.makeText (this, "Error: verify connection!", Toast.LENGTH_SHORT).show();	
    	}
    }
    
    /*!
     * Looks in the settings if this is the first time the application is run.
     * If so, we show the checklist to ensure the user knows how to configure the app.
     */
    private boolean _checkFirstRun ()
    {
    	
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    	boolean firstRun = preferences.getBoolean("firstRun", true);
    	if (firstRun)
    	{
    		showChecklist();
    		
    		SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean("firstRun", false);
            edit.commit();
    	}
    	
        return firstRun;
    }
}