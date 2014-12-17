package com.skk.mathcard.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.skk.mathcard.R;
import com.skk.mathcard.database.MyMathDbHelper;
import com.skk.mathcard.misc.MathAppBean;
import com.skk.mathcard.misc.MathConstants;

public class FinalScoreActivity extends Activity implements OnClickListener {
	int mTotalCorrect = 0;
	int mTotalQuestion = 0;
	
	String mLevel;
	String mOperationStr;
	TextView mTvCongratulate;
	TextView mTvNewHigScore;
	TextView mTvScore;
	int mOperation;
	RatingBar mRatingBar;

	Button mNewGameButton;
	Button mScoreBoardButton ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		int higestScore = 0;
		boolean congrats = false;
		this.setTitle("Score");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_final_score);
		mOperation = getIntent().getIntExtra(MathConstants.OPERATIONS, 0);
		mOperationStr = getIntent().getStringExtra(MathConstants.OPERATIONS_STRING);
		mTotalCorrect = getIntent().getIntExtra(MathConstants.SCORE, 0);
		mTotalQuestion = getIntent().getIntExtra(MathConstants.TOTAL_QUESTION,
				0);
		
		mLevel = getIntent().getStringExtra(MathConstants.LEVEL);
		mRatingBar = (RatingBar) findViewById(R.id.fs_ratingBar);
		mRatingBar.setVisibility(View.INVISIBLE);
		mRatingBar.setMax(5);
		mRatingBar.setStepSize(0.5F);
		mRatingBar.setVisibility(View.VISIBLE);
		int percentage = (mTotalCorrect * 100 / mTotalQuestion);
		float rating = (float) (percentage / 20);
		mRatingBar.setRating(rating);

		MathAppBean bean = new MathAppBean();
		bean.setLevel(mLevel);
		bean.setOperation(mOperationStr);
		bean.setScore(mTotalCorrect);
		bean.setTotal(mTotalQuestion);
		bean.setPercentage(percentage);
		//load the scores
		Cursor cur = MyMathDbHelper.getInstance(this).getRank(bean);
		higestScore = percentage;
		while (cur.moveToNext()) {
			int score = cur.getInt(0);
			if (higestScore < score) {
				higestScore = score;
			}

			if (score <= percentage) {
				congrats = true;

			}
		}
		mTvNewHigScore = (TextView) findViewById(R.id.fs_TextView_New_HighScore);
		
		if (congrats && mTotalCorrect>0) {
			mTvNewHigScore.setVisibility(View.VISIBLE);
		} else {
			mTvNewHigScore.setVisibility(View.INVISIBLE);
			
		}
		mTvScore = (TextView) findViewById(R.id.fs_TextView_Scores);
		mTvScore.setText("Highest Score : " + higestScore +"%"
				+ " Your Score : " + percentage+"%");
		mScoreBoardButton = (Button) findViewById(R.id.fs_Button_scoreBoard);
		mScoreBoardButton.setOnClickListener(this);
		mNewGameButton = (Button) findViewById(R.id.fs_Button_newGame);
		mNewGameButton.setOnClickListener(this);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.final_score, menu);
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
	public void onClick(View v) {
		Intent newIntent = null;
		switch (v.getId()) {
		case R.id.fs_Button_newGame:
			newIntent = new Intent(this,MainActivity.class);
			newIntent.putExtra(MathConstants.OPERATIONS, mOperation);
			
			newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(newIntent);
			break;
		case R.id.fs_Button_scoreBoard:
			newIntent = new Intent(this,DisplayScoreActivity.class);
			startActivity(newIntent);
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onBackPressed() {
		Intent newIntent = new Intent(this,SelectionActivity.class);
		newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(newIntent);
		
	}
}
