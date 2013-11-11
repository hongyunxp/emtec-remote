package com.sjarliecodes.emtecremote;

import java.util.LinkedList;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class Log extends ListActivity
{
	//private static Vector<String> s_logMessages;
	private static LinkedList<String> s_logMessages;
	private static int s_maxSize = 100;
	private static int s_count;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		String[] items = new String[s_logMessages.size()];
		s_logMessages.toArray(items);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.log, items));
	}
	
	public static void Initialize ()
	{
		s_logMessages = new LinkedList<String> ();
		s_count = 0;
	}
	
	public static void Message (String i_message)
	{
		s_count = s_count + 1;
		s_logMessages.addFirst(s_count + ": " + i_message);
		if (s_logMessages.size() > s_maxSize)
		{
			s_logMessages.removeLast();
		}
	}
}
