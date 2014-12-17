package com.skk.mathcard.activity;

import java.util.Arrays;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import com.skk.mathcard.R;

public class SettingsActivity extends Activity {
Spinner mNoOfGamesSpinner;
Switch mSoundSwitch;
String[] mGamesCount = {"5","10"};

SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPreferences = getSharedPreferences("mathAppSettings", 0);
		setContentView(R.layout.activity_settings);
		mSoundSwitch = (Switch) findViewById(R.id.switchSound);
		mSoundSwitch.setChecked( sharedPreferences.getBoolean("Sounds", false));
		showSpinner();
	}
	private void showSpinner() {
		
		String numberOfGames = sharedPreferences.getString("NoOfGames", "5");
		mNoOfGamesSpinner = (Spinner) findViewById(R.id.spinnerNoGames);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.games_count_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		
		mNoOfGamesSpinner.setAdapter(adapter);
		int index = Arrays.asList(mGamesCount).indexOf(numberOfGames);
		mNoOfGamesSpinner.setSelection(index);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		 sharedPreferences = getSharedPreferences("mathAppSettings", 0);
	   	 
		 SharedPreferences sharedPreferences = getSharedPreferences("mathAppSettings", 0);
	   	 
		   	final Editor editor = sharedPreferences.edit();
		   	  
			mSoundSwitch = (Switch) findViewById(R.id.switchSound);
			String value  = mNoOfGamesSpinner.getSelectedItem().toString();
			editor.putString("NoOfGames",value );
			editor.putBoolean("Sounds", mSoundSwitch.isChecked());
			editor.commit();
		
		super.onSaveInstanceState(outState);
	}
	@Override
	public void onBackPressed() {
		SharedPreferences sharedPreferences = getSharedPreferences("mathAppSettings", 0);
	   	 
	   	final Editor editor = sharedPreferences.edit();
	   	  
		mSoundSwitch = (Switch) findViewById(R.id.switchSound);
		String value  = mNoOfGamesSpinner.getSelectedItem().toString();
		editor.putString("NoOfGames",value );
		editor.putBoolean("Sounds", mSoundSwitch.isChecked());
		editor.commit();
		super.onBackPressed();
	}
	
	
}
