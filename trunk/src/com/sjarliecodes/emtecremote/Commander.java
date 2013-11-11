package com.sjarliecodes.emtecremote;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.Map.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.*;
import android.preference.PreferenceManager;

public class Commander
{
	// Enums
	public enum eResult { SUCCESS, ERROR_WIFI, ERROR_HOST, ERROR_PORT, ERROR_GENERAL };
	
	// Singleton pattern
	private static Commander m_singleInstance;
	
	// Connection settings
	private String m_host = "192.168.2.100";
	private String m_port = "1024";
	private boolean m_autoDiscover = true;
	SharedPreferences m_settings;
	
	// Members
	private Remote m_remote;
	private DefaultHttpClient m_client;
	private StringBuilder m_uriBuilder;
	private int m_baseUriLength;
	private eResult m_result;
	
	// Variables
	private boolean m_started;
	private ArrayList<String> m_commands;
	
	static Commander GetCommander()
	{
		if (m_singleInstance == null)
		{
			m_singleInstance = new Commander();
		}
		return m_singleInstance;
	}
	
	static Commander GetCommander (Remote i_remote)
	{
		Commander commander = GetCommander();
		commander.SetRemote(i_remote);
		return commander;
	}
	
	private Commander ()
	{
		// Initialize
		m_started = false;
		m_commands = new ArrayList<String>();
		m_client = new DefaultHttpClient ();
		m_uriBuilder = new StringBuilder ();
	}
	
	protected void finalize()
	{
		
	}
	
	public Remote SetRemote (Remote i_remote)
	{
		Remote previousRemote = m_remote;
		m_remote = i_remote;
		m_settings = PreferenceManager.getDefaultSharedPreferences(m_remote);
		_settingsChanged ();
		
		return previousRemote;
	}
	
	/*!
	 * Tests the connection parameters. Returns true if the Movie Cube responds, false otherwise.
	 * Note: Connection can timeout. Call this method in a separate thread to avoid delays. 
	 */
	public void TestConnection ()
	{
		// 1. create a handler for the callback
		final Handler connectionTestCallback = new Handler ();
		
		// 2. sink in UI thread after execution
		final Runnable connectionTestResponse = new Runnable ()
		{
			public void run ()
			{
				_showInUI ();
			}
		};

		// 3. test the connection
		new Thread()
		{
			@Override
			public void run ()
			{
				// test the connection
				_TestConnection ();
				
				// if failed, try to find automatically if user wants this
				if (m_result != eResult.SUCCESS && m_autoDiscover)
				{
					_AutoDiscover ();
				}
				
				// callback to UI thread
				connectionTestCallback.post(connectionTestResponse);
			}
		}.start();
	}
	
	private void _TestConnection ()
	{
		// verify if wifi is one
		ConnectivityManager manager = (ConnectivityManager)m_remote.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo infoWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (!infoWifi.isConnected())
		{
			m_result = eResult.ERROR_WIFI;
			return;
		}
		
		// verify if the host exists
		if (_getHostIP(m_host) < 0)
		{
			m_result = eResult.ERROR_HOST;
			return;
		}
		
		// ping the host on the specified address and port
		HttpGet requestForTest = new HttpGet("http://" + m_host + ":" + m_port);
        try {
            new DefaultHttpClient().execute(requestForTest); // can last...
            m_result = eResult.SUCCESS;
            return;
        }
        catch (Exception e)
        {
        	// exception is thrown if the get method fails
        	m_result = eResult.ERROR_PORT;
        	return;
        }
	}
	
	/*!
	 * Tests the connection parameters. Returns true if the Movie Cube responds, false otherwise.
	 * Note: Connection can timeout. Call this method in a separate thread to avoid delays. 
	 */
	public void AutoDiscover ()
	{
		// 1. create a handler for the callback
		final Handler autoDiscoverCallback = new Handler ();
		
		// 2. sink in UI thread after execution
		final Runnable autoDiscoverResponse = new Runnable ()
		{
			public void run ()
			{
				_showInUI ();
			}
		};

		// 3. test the connection
		new Thread()
		{
			@Override
			public void run ()
			{
				// discover the host
				_AutoDiscover ();
				
				// callback to UI thread
				autoDiscoverCallback.post (autoDiscoverResponse);
			}
		}.start();
	}
	
	private void _AutoDiscover ()
	{
		WifiManager wifi = (WifiManager) m_remote.getSystemService (Context.WIFI_SERVICE);
		
		String myIPString = _getIpFromIntSigned (wifi.getConnectionInfo().getIpAddress());
		String gatewayString = _getIpFromIntSigned (wifi.getDhcpInfo().gateway);
		long myIPLong = _getUnsignedLongFromIp (myIPString);
		long gatewayLong = _getUnsignedLongFromIp (gatewayString);
		
		long poolStart = _getPoolStartLongUnsignedFromIp (gatewayString);
		long poolEnd   = _getPoolEndLongUnsignedFromIp   (gatewayString);
		
		InetAddress pingAddress;
		for (long i=poolStart; i<=poolEnd; ++i)
		{
			if (i != myIPLong && i != gatewayLong)
			{				
				// test connection for every i until Movie Cube has been found
				String host = _getIpFromLongUnsigned (i);
				
				boolean reachable=false;				
				
				try
				{
					pingAddress = InetAddress.getByName (host);
					reachable = pingAddress.isReachable (100);
				}
				catch (Exception e)
				{
					reachable = false;
				}
				
				if (reachable)
				{
					// try sending a command
					HttpGet requestForTest = new HttpGet("http://" + host + ":" + m_port);
			        try {
			            new DefaultHttpClient().execute(requestForTest); // can last...
			             
			            // save result in application parameters
						SharedPreferences.Editor preferencesEditor = m_settings.edit();
						preferencesEditor.putString ("host", host);
						if (!preferencesEditor.commit())
						{
							m_result = eResult.ERROR_GENERAL;
							return;
						}
						
						// apply new settings
						_settingsChanged ();
						
						m_result = eResult.SUCCESS;
						return;
			        }
			        catch (Exception e)
			        {
			        	// exception is thrown if the get method fails
			        	// try another one...
			        }
				}
			}
		}
		
		// scan did not find Movie Cube
		m_result = eResult.ERROR_HOST;
	}
	
	public void Execute (final String command)
	{
		// Add command to command list
		m_commands.add(command);
		
		// If there is no thread active, start one
		if (!m_started)
		{
			m_started = true;
			
			// Create a handler for UI callbacks
			final Handler uiThreadCallback = new Handler ();
			
			// Perform actions on the UI thread after execution
			final Runnable runInUIThread = new Runnable ()
			{
				public void run ()
				{
					_showInUI ();
				}
			};
			
			// Create the thread
			new Thread ()
			{
				// The run actions of the thread
				@Override
				public void run ()
				{
					// As long as there are commands to post, the thread must run.
					while (!m_commands.isEmpty())
					{
						// Post the command in background
						_postData (m_commands.get(0));
						// Remote the command from the list
						m_commands.remove(0);
						// When finished, post callback
						uiThreadCallback.post(runInUIThread);
					}
					
					// If there are no more commands in the list, the thread runs dry.
					m_started = false;
				}
			}.start();
		}
	}

	// Do a series of post commands to the Emtec MC.
	// See: http://androidsnippets.com/executing-a-http-post-request-with-httpclient
	private void _postData(String command) {
		
		Hashtable<String, String> map = new Hashtable<String, String> ();
		map.put("id", "1");
		map.put("cmd", command);
		
	    try {
	    	// Parse request parameters
		    for (Entry<String, String> entry : map.entrySet())
		    {
		    	m_uriBuilder.append(entry.getKey()).append("=")
		    	.append(entry.getValue()).append("&");
		    }
		    m_uriBuilder.deleteCharAt(m_uriBuilder.length()-1);
	    	
		    // Create the post
		    HttpPost postMethod = new HttpPost(m_uriBuilder.toString());
		    
		    // Reset the URI builder
		    m_uriBuilder.delete(m_baseUriLength, m_uriBuilder.length());
		    
		    // Execute HTTP Post Request
		    m_client.execute(postMethod, new ResponseHandler<Void> ()
	        {
				public Void handleResponse(HttpResponse response)
					   throws ClientProtocolException, IOException
				{
					// Get post status
					StatusLine status = response.getStatusLine();
					
					// build message
					String message = "Result: " + status.getStatusCode();
					if (status.getReasonPhrase() != null)
					{
						message += " => " + status.getReasonPhrase();
					}
					
					// log message
					Log.Message(message);
					
					// set the result to successful
					m_result = eResult.SUCCESS;
					
					return null;
				}
	        });
	    }
	    catch (Exception e)
	    {
	    	// build message
	    	String message = "Error: " + e.getMessage();
	    	
	    	// log message
	    	Log.Message(message);
	    	
	    	// set the result to failure
	    	m_result = eResult.ERROR_GENERAL;
	    }
	}
	
	/*!
	 * \brief Gets the connection parameters from the shared preferences
	 * and rebuilds the commands base Uri
	 */
	private void _settingsChanged ()
	{
		m_host 			= m_settings.getString  ("host", "192.168.2.1");
		m_port 			= m_settings.getString  ("port", "1024");
		m_autoDiscover 	= m_settings.getBoolean ("auto_discover", true);
		
	    // Rebuild the URI
	    if (m_uriBuilder.length() > 0)
	    {
	    	m_uriBuilder.delete(0, m_uriBuilder.length());
	    }
	    m_uriBuilder.append("http://").append(m_host).append(":").append(m_port).append("/cgi-bin/cubermctrl.cgi?");
	    m_baseUriLength = m_uriBuilder.length();
	}
	
	private void _showInUI ()
	{
		if (m_remote != null)
		{
			m_remote.CommanderResult(m_result);
		}
	}
	
	/*!
     * \brief Translates a hostname to integer IP
     */
    private int _getHostIP (String i_hostname) {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(i_hostname);
        } catch (UnknownHostException e) {
            return -1;
        }
        byte[] addrBytes;
        int addr;
        addrBytes = inetAddress.getAddress();
        addr = ((addrBytes[3] & 0xff) << 24)
                | ((addrBytes[2] & 0xff) << 16)
                | ((addrBytes[1] & 0xff) << 8)
                |  (addrBytes[0] & 0xff);
        return addr;
    }
    
    private static int _getPoolStartLongUnsignedFromIp (String i_ipAddress)
    {
    	String[] a = i_ipAddress.split("\\.");
		return (Integer.parseInt(a[0]) * 16777216 + Integer.parseInt(a[1]) * 65536
                + Integer.parseInt(a[2]) * 256 + 1);
    }
    
    private static int _getPoolEndLongUnsignedFromIp (String i_ipAddress)
    {
    	String[] a = i_ipAddress.split("\\.");
		return (Integer.parseInt(a[0]) * 16777216 + Integer.parseInt(a[1]) * 65536
                + Integer.parseInt(a[2]) * 256 + 255);
    }
    
    private static long _getUnsignedLongFromIp (String i_ipAddress) {
        String[] a = i_ipAddress.split("\\.");
        return (Integer.parseInt(a[0]) * 16777216 + Integer.parseInt(a[1]) * 65536
                + Integer.parseInt(a[2]) * 256 + Integer.parseInt(a[3]));
    }

    private static String _getIpFromIntSigned (int i_ipInt) {
        String ip = "";
        for (int k = 0; k < 4; k++) {
            ip = ip + ((i_ipInt >> k * 8) & 0xFF) + ".";
        }
        return ip.substring(0, ip.length() - 1);
    }

    private static String _getIpFromLongUnsigned (long i_ipLong) {
        String ip = "";
        for (int k = 3; k > -1; k--) {
            ip = ip + ((i_ipLong >> k * 8) & 0xFF) + ".";
        }
        return ip.substring(0, ip.length() - 1);
    }
}
