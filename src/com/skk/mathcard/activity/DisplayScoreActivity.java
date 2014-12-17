package com.skk.mathcard.activity;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.skk.mathcard.R;
import com.skk.mathcard.adapter.ScoreViewAdapter;
import com.skk.mathcard.database.MyMathDbHelper;
import com.skk.mathcard.misc.FilterCursorWrapper;
import com.skk.mathcard.misc.MathConstants;
public class DisplayScoreActivity extends Activity 
//implements OnCreateContextMenuListener
{
	//List  mSelectedItems;
	 MyMathDbHelper mdb ;
	 String mStrLevel = null;
	 ScoreViewAdapter mScoreAdapter ;
	 Cursor mConstantsCursor;
	 FilterCursorWrapper mCursorWrapper ;
	 final boolean[] myFlags = new boolean[4];
	 List<String> mFilterList = new ArrayList<String>();
	//SimpleCursorAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myFlags[0]= true;
		myFlags[1]= true;
		myFlags[2]= true;
		myFlags[3]= true;
		 setContentView(R.layout.activity_display_score);
		 this.setTitle("My Scoreboard");
		
		 mdb =  MyMathDbHelper.getInstance(this);
		
		 mFilterList.add("+");
		 mFilterList.add("-");
		 mFilterList.add("*");
		 mFilterList.add("/");
		 SharedPreferences sharedPreferences = getSharedPreferences(MathConstants.MATH_APP_SHARED_PREFERENCE, 0);
			
		 int level = sharedPreferences.getInt(MathConstants.PREF_LEVEL, 0);
		 
		 if (level == 0) {
			 mStrLevel =  MathConstants.BEGINNER;
			} else if (level == 1) {
				mStrLevel =  MathConstants.INTERMMEDIATE;
			} else if (level == 2) {
				mStrLevel = MathConstants.Expert;
				
			}
		 mConstantsCursor = mdb.getAllScoresForLevel(mStrLevel);
		 mCursorWrapper =  new FilterCursorWrapper(mConstantsCursor,mFilterList,1);
		 mScoreAdapter = new ScoreViewAdapter(this,mCursorWrapper,0);
		 ListView listView = (ListView) findViewById(R.id.listViewScores);
		 listView.setAdapter(mScoreAdapter);
	
		}
	
	private void showAll() {
		mCursorWrapper =  new FilterCursorWrapper(mConstantsCursor,mFilterList,1);
		 mScoreAdapter = new ScoreViewAdapter(this,mCursorWrapper,0);
		 ListView listView = (ListView) findViewById(R.id.listViewScores);
		 listView.setAdapter(mScoreAdapter);
	}
	 
	@Override
	 public boolean onContextItemSelected(MenuItem item) {
	        mdb.deleteScore(item.getItemId());
	        Toast.makeText(getApplicationContext(), "Score has been deleted",
		                Toast.LENGTH_SHORT).show();
	        mConstantsCursor = mdb.getAllScoresForLevel(mStrLevel);
	        showAll() ;
	        return super.onContextItemSelected(item);
	        
	 }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_score, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.Filter) {
			showFilterDialog();
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private void  showFilterDialog() {
		Dialog alertDialog =null;
		AlertDialog.Builder builder = new AlertDialog.Builder(DisplayScoreActivity.this);
		
		builder.setMultiChoiceItems(R.array.score_filter_array, myFlags, 
				
			new DialogInterface.OnMultiChoiceClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int which,
	                       boolean isChecked) {
	                   if (isChecked) {
	                	   myFlags[which] = true;
	                       // If the user checked the item, add it to the selected items
	                	   if (which == 0) {
	                		   mFilterList.add("+");
		                      
		                	   } else if (which == 1){
		                		   mFilterList.add("-");
				                     
		                	   } else if (which == 2){
		                		   mFilterList.add("*");
				                     
		                	   }
		                	   else if (which == 3){
		                		   mFilterList.add("/");
				                     
		                	   }
	                   } else {
	                	   if (which == 0) {
		                       mFilterList.remove("+");
		                	   } else if (which == 1){
		                		   mFilterList.remove("-");
		                	   } else if (which == 2){
		                		   mFilterList.remove("*");
		                	   }
		                	   else if (which == 3){
		                		   mFilterList.remove("/");
		                	   }
	                       // Else, if the item is already in the array, remove it 
	                	   
	                      
	                	   myFlags[which] = false;
	                   }
	               }
	           })
	    // Set the action buttons
	           .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                   showAll();
	                   mScoreAdapter.notifyDataSetChanged();
	               }
	           })
	           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                  
	               }
	           });

		builder.setTitle("Filter Score");
		alertDialog = builder.create();
		alertDialog.show();
		
	}
	
}

