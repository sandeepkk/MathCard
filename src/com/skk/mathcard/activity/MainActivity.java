package com.skk.mathcard.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.skk.mathcard.R;
import com.skk.mathcard.database.MyMathDbHelper;
import com.skk.mathcard.misc.MathAppBean;
import com.skk.mathcard.misc.MathConstants;

public class MainActivity extends Activity implements OnClickListener {
	int mRedButton = 0;
	int mGreenButton=0;
	int mTotalCorrect = 0;
	int mTotalQuestion = 0;
	MediaPlayer mMplayer;
	boolean mSoundOn=false;
	int mPosition = 0;
	//int mGuess = 0;
	int mlevel = 0;
	String mStringLevel ;
	int mMultiplier = 1;
	int mNumberOfGames = 10;
	String mGameState ;
	
	boolean mGameOver = true;
	Integer mResult = 0;
	String mStrResult = "";
	
	String[] mButtonTexts;
	
	Integer mRand2 = 0;
	Integer mRand3 = 0;
	
	//RatingBar rb;
	Button mButton1;
	Button mButton2;
	Button mButton3;
	Button mButtonContinue;
	Button mButtonClicked;

	TextView mTextViewFirstNum;
	TextView mTextViewSecondNum;
	TextView mTextViewCounter;
	TextView mTextViewOeration;
	TextView mTextViewLevel;
	TextView mTextViewScore;
	
	int mOperation = 0;
	String mStringOperation;
	final int SUM = 0;
	final int SUBTRACTION = 1;
	final int MULTIPLY = 2;
	final int DIVISION = 3;
	SharedPreferences sharedPreferences;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setTitle("Let's Do Math!");
		mOperation = getIntent().getIntExtra(MathConstants.OPERATIONS, 0);
		
		setContentView(R.layout.activity_main);
		mButton1 = (Button) findViewById(R.id.button1);
		mButton1.setOnClickListener(this);
		mButton2 = (Button) findViewById(R.id.button2);
		mButton2.setOnClickListener(this);
		mButton3 = (Button) findViewById(R.id.button3);
		mButton3.setOnClickListener(this);
		mButtonContinue = (Button) findViewById(R.id.buttonContinue);
		mButtonContinue.setOnClickListener(this);
		mButtonContinue.setEnabled(false);
		mButtonContinue.setVisibility(View.INVISIBLE);
		mButton1.setTextColor(Color.WHITE);
		mButton2.setTextColor(Color.WHITE);
		mButton3.setTextColor(Color.WHITE);
		
		

		mTextViewLevel = (TextView) findViewById(R.id.TextView_Level);
		mTextViewScore = (TextView) findViewById(R.id.TextView_Score);
		mTextViewScore.setText("Score :" + mTotalCorrect);
		//shared preferences
		sharedPreferences = getSharedPreferences(MathConstants.MATH_APP_SHARED_PREFERENCE, 0);
		
		mNumberOfGames = Integer.parseInt(sharedPreferences.getString(MathConstants.PREF_NO_OF_GAMES, "10"));
		mSoundOn = sharedPreferences.getBoolean(MathConstants.PREF_SOUNDS, false);
		mlevel = sharedPreferences.getInt(MathConstants.PREF_LEVEL, 0);
	
		mTextViewOeration = (TextView) findViewById(R.id.sum_char);
		mTextViewFirstNum = (TextView) findViewById(R.id.first_num);
		mTextViewSecondNum = (TextView) findViewById(R.id.sec_num);
		mTextViewCounter = (TextView) findViewById(R.id.TextView_Counter);
		
		if (mlevel == 0) {
			mTextViewLevel.setText("Level : " + MathConstants.BEGINNER);
			mStringLevel =  MathConstants.BEGINNER;
			mMultiplier = 10;
		} else if (mlevel == 1) {
			mTextViewLevel.setText("Level : " + MathConstants.INTERMMEDIATE);
			mStringLevel =  MathConstants.INTERMMEDIATE;
			mMultiplier = 100;
		} else if (mlevel == 2) {
			mTextViewLevel.setText("Level : " + MathConstants.Expert);
			mStringLevel = MathConstants.Expert;
			mMultiplier = 1000;
		}
		
		generateNumber();
		

	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	
	private void setQuestionDisplays() {
		Integer first_num = 0;
		Integer second_num = 0;
		Integer temp_num = 0;
		// Generate two random numbers
				first_num = generateRandomNumbers();
				do {
					second_num = generateRandomNumbers();
				} while (second_num == first_num);
	
		switch (mOperation) {
		case SUM:
			mStringOperation = "+";
			mTextViewOeration.setText(mStringOperation);
			mResult = first_num + second_num;
			mTextViewFirstNum.setText(first_num.toString());
			mTextViewSecondNum.setText(second_num.toString());
			this.setTitle("Addition");
			break;
		case SUBTRACTION:
			mStringOperation= "-";
			this.setTitle("Subtraction");
			mTextViewOeration.setText(mStringOperation);
			if (first_num > second_num) {
				mResult = first_num - second_num;
				mTextViewFirstNum.setText(first_num.toString());
				mTextViewSecondNum.setText(second_num.toString());

			} else {
				mResult = second_num - first_num;
				mTextViewFirstNum.setText(second_num.toString());
				mTextViewSecondNum.setText(first_num.toString());
			}
			break;
		case MULTIPLY:
			mStringOperation="*";
			this.setTitle("Multiplication");
			mTextViewOeration.setText(mStringOperation);
			mResult = first_num * second_num;
			mTextViewFirstNum.setText(first_num.toString());
			mTextViewSecondNum.setText(second_num.toString());
			break;
		case DIVISION:
			mStringOperation="/";
			this.setTitle("Division");
			mTextViewOeration.setText(mStringOperation);
			temp_num = first_num;
			mResult = first_num * second_num;
			first_num = mResult;
			mResult = temp_num;

			mTextViewFirstNum.setText(first_num.toString());
			mTextViewSecondNum.setText(second_num.toString());

			break;
		}
	}
	private void generateNumber() {

		mGameState = MathConstants.CARD_ON;
		mPosition = 0;
		mResult = 0;
		
		// Display part starts here
		
		mTextViewCounter.setText("Completed :" + mTotalQuestion + " / "
				+ mNumberOfGames);

		
		setQuestionDisplays();
		mButton1.setEnabled(true);
		mButton2.setEnabled(true);
		mButton3.setEnabled(true);
		
		mButton1.setBackgroundColor(Color.GRAY);
		mButton2.setBackgroundColor(Color.GRAY);
		mButton3.setBackgroundColor(Color.GRAY);
		/*
		 * 1: Done with the text view setting 2: setting the button part start
		 * here 3: generate a position between 1 and 3 to select which button to
		 * set the sum
		 */
		while (mPosition < 1 || mPosition > 3) {
			mPosition = (int) (Math.random() * 10);
		}
		switch (mPosition) {
		case 1:
			setButtonData(mButton1, mButton2, mButton3);
			break;
		case 2:
			setButtonData(mButton2, mButton1, mButton3);
			break;
		case 3:
			setButtonData(mButton3, mButton2, mButton1);
			break;
		}
	}

	private void setButtonData(Button sumButton, Button randomButton_1,
			Button randomButton_2) {
		mRand2 = 0;
		mRand3 = 0;

		int marginNum = (mResult / 10) * 10;

		mStrResult = mResult.toString();
		sumButton.setText(mStrResult);

		while (mResult == mRand2 || mRand2 == 0) {
			mRand2 = (int) (Math.random() * mMultiplier) + marginNum;
			randomButton_1.setText(mRand2.toString());
		}
		while (mResult == mRand3 || mRand3 == mRand2 || mRand3 == 0) {
			mRand3 = (int) (Math.random() * mMultiplier) + marginNum;
			randomButton_2.setText(mRand3.toString());
		}
	}

	private int generateRandomNumbers() {
		int randomNum = 0;
		while (randomNum < (mMultiplier / 10) || randomNum >= mMultiplier) {
			randomNum = (int) (Math.random() * mMultiplier);
		}
		return randomNum;

	}

	private void showToast(String message) {
		Toast toast = Toast.makeText(getApplicationContext(), message,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
		toast.show();
	}

	@Override
	public void onClick(View arg0) {
		mRedButton=0;
		mGreenButton=0;
		mGameState = MathConstants.CARD_OFF;
		mGameOver = false;
		mButton1.setEnabled(false);
		mButton2.setEnabled(false);
		mButton3.setEnabled(false);
		mButtonContinue.setEnabled(true);
		mButtonContinue.setVisibility(View.VISIBLE);
		switch (arg0.getId()) {
		case R.id.button1:
		case R.id.button3:
		case R.id.button2:
			mButtonClicked = ((Button) findViewById(arg0.getId()));
			mButtonClicked.setEnabled(false);
			
			if (mButtonClicked.getText().equals(mStrResult)) {
				mTotalCorrect++;
				mButtonClicked.setBackgroundColor(Color.GREEN);
				if (mSoundOn) {
					mMplayer = MediaPlayer.create(this, R.raw.soundcorrect);
				    mMplayer.start();
				}
				mGreenButton = arg0.getId();
				showToast("Correct Answer");
			} else {
				mButtonClicked.setBackgroundColor(Color.RED);
				{
					mRedButton=arg0.getId();
					if (mSoundOn) {
						mMplayer = MediaPlayer.create(this, R.raw.soundwrong);
					    mMplayer.start();
						
					}
				}
				showToast("Wrong Answer");
				showCorrectAnswer();
			}
			mTotalQuestion++;
			break;

		case R.id.buttonContinue:
			mButtonContinue.setEnabled(false);
			mButtonContinue.setVisibility(View.INVISIBLE);
			generateNumber();
			break;
		}
		mTextViewScore.setText("Correct : " + mTotalCorrect);
		mTextViewCounter.setText("Completed :" + mTotalQuestion + " / "
				+ mNumberOfGames);
		if (mTotalQuestion >= mNumberOfGames) {
			MathAppBean bean = new MathAppBean();
			bean.setOperation(mTextViewOeration.getText().toString());
			bean.setLevel(mStringLevel);
			bean.setScore(mTotalCorrect);
			bean.setTotal(mTotalQuestion);
			bean.setPercentage((mTotalCorrect*100)/mTotalQuestion);
			mGameOver = true;
			mButtonContinue.setEnabled(false);
			mButtonContinue.setVisibility(View.INVISIBLE);
			MyMathDbHelper db = MyMathDbHelper.getInstance(this);
			
			
			db.insertScore(bean);
			
			gameOver();
			
		}
	}
	
	private void showCorrectAnswer() {
		if (mButton1.getText().equals(mStrResult)){
			mButton1.setBackgroundColor(Color.GREEN);
			mGreenButton =mButton1.getId();
		} else 		if (mButton2.getText().equals(mStrResult)){
			mButton2.setBackgroundColor(Color.GREEN);
			mGreenButton =mButton2.getId();
		} else 		if (mButton3.getText().equals(mStrResult)){
			mButton3.setBackgroundColor(Color.GREEN);
			mGreenButton =mButton3.getId();
		}
	}
	private void gameOver() {
		Intent finalScoreActivity = new Intent(this,FinalScoreActivity.class);
		finalScoreActivity.putExtra(MathConstants.OPERATIONS_STRING, mStringOperation);
		finalScoreActivity.putExtra(MathConstants.SCORE, mTotalCorrect);
		finalScoreActivity.putExtra(MathConstants.LEVEL, mStringLevel);
		finalScoreActivity.putExtra(MathConstants.TOTAL_QUESTION, mTotalQuestion);
		finalScoreActivity.putExtra(MathConstants.OPERATIONS, mOperation);
		
		
		startActivity(finalScoreActivity);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		
		mGreenButton = savedInstanceState.getInt(MathConstants.GREEN_BUTTON_ID);
		mRedButton = savedInstanceState.getInt(MathConstants.RED_BUTTON_ID);
		if (mGreenButton!=0 ) {
			((Button)findViewById(mGreenButton)).setBackgroundColor(Color.GREEN) ;
		} 
		if (mRedButton!=0) {
			((Button)findViewById(mRedButton)).setBackgroundColor(Color.RED) ;
		}
		mTotalCorrect = savedInstanceState.getInt(MathConstants.SCORE);
		mTotalQuestion = savedInstanceState.getInt(MathConstants.QUESTIONS);
		mOperation = savedInstanceState.getInt(MathConstants.OPERATIONS);
		mGameOver = savedInstanceState.getBoolean(MathConstants.GAME_OVER);
		mGameState = savedInstanceState.getString(MathConstants.GAME_STATE);
		mButtonTexts = savedInstanceState.getStringArray(MathConstants.NUMBER_VALUES);
		mButtonContinue.setOnClickListener(this);
		if (mGameState.equals(MathConstants.CARD_OFF)) {
			mButton1.setEnabled(false);
			mButton2.setEnabled(false);
			mButton3.setEnabled(false);
			mButtonContinue.setEnabled(true);
			mButtonContinue.setVisibility(View.VISIBLE);
		} 
		else if (mGameState.equals(MathConstants.CARD_ON)) {
			mButton1.setEnabled(true);
			mButton2.setEnabled(true);
			mButton3.setEnabled(true);
			mButtonContinue.setEnabled(false);
			mButtonContinue.setVisibility(View.INVISIBLE);
		}
		
		mTextViewCounter = (TextView) findViewById(R.id.TextView_Counter);
		mTextViewCounter.setText("Completed :" + mTotalQuestion + " / "
				+ mNumberOfGames);
		
		mTextViewScore = (TextView) findViewById(R.id.TextView_Score);
		mTextViewScore.setText("Correct : " + mTotalCorrect);
		
		Log.i("MainActivity", "" + mTotalCorrect + mTotalQuestion);
		mTextViewOeration = (TextView) findViewById(R.id.sum_char);
		if (mOperation ==0 ) {
			mTextViewOeration.setText("+");
		} else if (mOperation ==1 ) {
			mTextViewOeration.setText("-");
		}else if (mOperation ==2 ) {
			mTextViewOeration.setText("*");
		}else if (mOperation ==3 ) {
			mTextViewOeration.setText("/");
		}
		mTextViewFirstNum = (TextView) findViewById(R.id.first_num);
		mTextViewFirstNum.setText(mButtonTexts[0]);
		mTextViewSecondNum = (TextView) findViewById(R.id.sec_num);
		mTextViewSecondNum.setText(mButtonTexts[1]);
		mButton1 = (Button) findViewById(R.id.button1);
		mButton1.setText(mButtonTexts[2]);
		mButton2 = (Button) findViewById(R.id.button2);
		mButton2.setText(mButtonTexts[3]);
		mButton3 = (Button) findViewById(R.id.button3);
		mButton3.setText(mButtonTexts[4]);
		mStrResult = mButtonTexts[5];
		
		mButton1.setTextColor(Color.WHITE);
		mButton2.setTextColor(Color.WHITE);
		mButton3.setTextColor(Color.WHITE);
		super.onRestoreInstanceState(savedInstanceState);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		outState.putInt(MathConstants.SCORE, mTotalCorrect);
		outState.putInt(MathConstants.QUESTIONS, mTotalQuestion);
		outState.putInt(MathConstants.OPERATIONS, mOperation);
		outState.putBoolean(MathConstants.GAME_OVER, mGameOver);
		outState.putString(MathConstants.GAME_STATE, mGameState);
		outState.putInt(MathConstants.GREEN_BUTTON_ID, mGreenButton);
		outState.putInt(MathConstants.RED_BUTTON_ID, mRedButton);
	    mButtonTexts  = new String[6];
		mButtonTexts[0] = mTextViewFirstNum.getText().toString();
		mButtonTexts[1] = mTextViewSecondNum.getText().toString();
		mButtonTexts[2] = mButton1.getText().toString();
		mButtonTexts[3] = mButton2.getText().toString();
		mButtonTexts[4] = mButton3.getText().toString();
		mButtonTexts[5] = mStrResult;
		outState.putStringArray(MathConstants.NUMBER_VALUES, mButtonTexts);
		
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onBackPressed() {
		if (!mGameOver) {
			new AlertDialog.Builder(this)
					.setMessage(
							"Are you sure you want to Exit? All scores will be lost ?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									MainActivity.this.finish();
								}
							})
					.setNegativeButton("No", null).show().getWindow();
					//.setLayout(600, 400);
		} else{
			MainActivity.this.finish();
		}
}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent secondActivity = new Intent(this, DisplayScoreActivity.class);
		int id = item.getItemId();
		switch (id) {
		case R.id.history:
			startActivity(secondActivity);
			break;
		}
			return super.onOptionsItemSelected(item);
	}

}
