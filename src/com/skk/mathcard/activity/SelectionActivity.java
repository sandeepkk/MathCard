package com.skk.mathcard.activity;



import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.skk.mathcard.R;
import com.skk.mathcard.adapter.MainViewAdapter;
import com.skk.mathcard.misc.ListOperation;
import com.skk.mathcard.misc.MathConstants;

public class SelectionActivity extends Activity implements OnItemClickListener {
	final String myList[] = { "Novice", "Intermediate", "Expert" };
	private List<ListOperation> createData() {
		List<ListOperation> list  = new ArrayList<ListOperation>();
		
		ListOperation lo1 = new ListOperation();
		lo1.setIcon(R.drawable.image_add);
		lo1.setAction("Addition");
		ListOperation lo2 = new ListOperation();
		lo2.setIcon(R.drawable.image_sub);
		lo2.setAction("Subtract");
		ListOperation lo3 = new ListOperation();
		lo3.setIcon(R.drawable.image_mul);
		lo3.setAction("Multiply");
		ListOperation lo4 = new ListOperation();
		lo4.setIcon(R.drawable.image_div);
		lo4.setAction("Division");
		list.add(lo1);
		list.add(lo2);
		list.add(lo3);
		list.add(lo4);
		return list;
		
	}
	String[] action = { "Sum", "Subtraction",
			"Multiplication","Division"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selection);
		this.setTitle("Let's Do Math!");
		List<ListOperation> list = createData();
		MainViewAdapter adapter = new MainViewAdapter(this,
				R.layout.operations_row, list);

		ListView mylist = (ListView) findViewById(R.id.listviewActions);
		
		mylist.setAdapter(adapter);
		mylist.setOnItemClickListener(this);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent secondActivity = new Intent(this,MainActivity.class);
		secondActivity.putExtra(MathConstants.OPERATIONS, arg2);
		startActivity(secondActivity);

		
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent activity = null;
	   
		int id = item.getItemId();
		switch(id) {
		case R.id.homeHistory:
			activity = new Intent(this,DisplayScoreActivity.class);
			startActivity(activity);
			break;
		case R.id.homeSettings:
			activity = new Intent(this,SettingsActivity.class);
			startActivity(activity);
			break;
		case R.id.homeLevel:
			showLevelNew();
			break;
			
		}
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		return super.onOptionsItemSelected(item);
	}
	private void  showLevelNew() {
		SharedPreferences sharedPreferences = getSharedPreferences("mathAppSettings", 0);
   	 
   	   final Editor editor = sharedPreferences.edit();
   	   int level = sharedPreferences.getInt("level", 0);
		Dialog alertDialog =null;
		AlertDialog.Builder builder = new AlertDialog.Builder(SelectionActivity.this);
		builder.setSingleChoiceItems(R.array.Game_Level_array, level, new DialogInterface.OnClickListener() {

			   @Override
			   public void onClick(DialogInterface arg0, int arg1) {
					 editor.putInt("level", arg1);
				   	 editor.commit();
				   	 arg0.dismiss();
			  			   }
			  });
		
        

		builder.setTitle("Level");
		alertDialog = builder.create();
		alertDialog.show();
		
	}
	
}
