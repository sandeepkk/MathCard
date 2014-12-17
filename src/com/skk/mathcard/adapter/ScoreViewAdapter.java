package com.skk.mathcard.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.skk.mathcard.R;
import com.skk.mathcard.database.MyMathDbHelper;

public class ScoreViewAdapter extends CursorAdapter

// implements OnCreateContextMenuListener
{
	
	private LayoutInflater layoutInflater;
	private Cursor myCursor;
	

	public ScoreViewAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}

	@Override
	public void bindView(View convertView, Context context, Cursor c) {
		myCursor = c;
		this.addToView(convertView, context, myCursor);

	}

	private void addToView(View convertView, Context context, Cursor c) {
		Integer score = this.myCursor.getInt(this.myCursor
				.getColumnIndex(MyMathDbHelper.SCORE_COLUMN_SCORE));
		Integer total = this.myCursor.getInt(this.myCursor
				.getColumnIndex(MyMathDbHelper.SCORE_COLUMN_TOTAL));
		String level = this.myCursor.getString(this.myCursor
				.getColumnIndex(MyMathDbHelper.SCORE_COLUMN_LEVEL));
		final Integer id = this.myCursor.getInt(this.myCursor
				.getColumnIndex("_id"));
		String operation = this.myCursor.getString(this.myCursor
				.getColumnIndex(MyMathDbHelper.SCORE_COLUMN_OPERATION));
		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.sr_imageView);
		Integer percentage = this.myCursor.getInt(this.myCursor
				.getColumnIndex(MyMathDbHelper.SCORE_COLUMN_PERCENTAGE));
		if (operation.equals("+")) {
			imageView.setImageResource(R.drawable.image_add);
		} else if (operation.equals("-")) {
			imageView.setImageResource(R.drawable.image_sub);
		} else if (operation.equals("/")) {
			imageView.setImageResource(R.drawable.image_div);
		} else if (operation.equals("*")) {
			imageView.setImageResource(R.drawable.image_mul);
		}
		RatingBar rb = (RatingBar) convertView.findViewById(R.id.sr_ratingBar);
		rb.setStepSize(0.5F);
		rb.setEnabled(false);
		float rating = (float) percentage / 20;
		rb.setRating(rating);

		TextView textViewOper = (TextView) convertView
				.findViewById(R.id.sr_score_percentage);
		textViewOper.setText(percentage.toString() + "%");
		TextView textViewLevel = (TextView) convertView
				.findViewById(R.id.sr_score_totals);
		textViewLevel.setText(score + "/" + total);
		TextView textViewScore = (TextView) convertView
				.findViewById(R.id.sr_level_score_row);
		textViewScore.setText(level);

		// registerForContextMenu(convertView);
		// convertView.setOnCreateContextMenuListener(this);
		convertView
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						menu.add(0, id, 0, "Remove");

					}
				});
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		return layoutInflater.inflate(R.layout.score_row, arg2, false);
	}

}
