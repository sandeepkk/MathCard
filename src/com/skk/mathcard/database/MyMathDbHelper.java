package com.skk.mathcard.database;

import com.skk.mathcard.misc.MathAppBean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyMathDbHelper extends SQLiteOpenHelper {
	private static MyMathDbHelper mSingleInstance = null;
	public static final String DATABASE_NAME = "MyMathDB.db";

	public static final String SCORE_TABLE = "score";
	public static final String SCORE_COLUMN_ID = "_id";
	public static final String SCORE_COLUMN_OPERATION = "operation";
	public static final String SCORE_COLUMN_SCORE = "score";
	public static final String SCORE_COLUMN_TOTAL = "total";
	public static final String SCORE_COLUMN_LEVEL = "level";
	public static final String SCORE_COLUMN_PERCENTAGE = "percentage";
	

	public static MyMathDbHelper getInstance(Context ctx) {
		if (mSingleInstance == null) {
			mSingleInstance = new MyMathDbHelper(ctx.getApplicationContext());
		}
		return mSingleInstance;
	}

	private MyMathDbHelper(Context context) {
		super(context, DATABASE_NAME, null, 2);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table  " + SCORE_TABLE + "(" + SCORE_COLUMN_ID
				+ " integer primary key, " + SCORE_COLUMN_OPERATION + " text,"
				+ SCORE_COLUMN_SCORE + " INTEGER, " + SCORE_COLUMN_TOTAL
				+ " INTEGER, " + SCORE_COLUMN_LEVEL + " text ," +SCORE_COLUMN_PERCENTAGE+" INTEGER )");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public boolean insertScore(MathAppBean bean) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();

		contentValues.put(SCORE_COLUMN_OPERATION, bean.getOperation());
		contentValues.put(SCORE_COLUMN_SCORE, bean.getScore());
		contentValues.put(SCORE_COLUMN_TOTAL, bean.getTotal());
		contentValues.put(SCORE_COLUMN_LEVEL, bean.getLevel());
		contentValues.put(SCORE_COLUMN_PERCENTAGE, bean.getPercentage());
		db.insert(SCORE_TABLE, null, contentValues);
		return true;
	}

	public Cursor getScore(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select * from " + SCORE_TABLE + " where id="
				+ id + "", null);
		return res;
	}

	public int numberOfRows() {
		SQLiteDatabase db = this.getReadableDatabase();
		int numRows = (int) DatabaseUtils.queryNumEntries(db, SCORE_TABLE);
		return numRows;
	}

	public void deleteScore(Integer id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(SCORE_TABLE, 
			      "_id = ? ", 
			      new String[] { Integer.toString(id) });
	}

	public Cursor getAllScores() {
		Cursor constantsCursor = (SQLiteCursor) this.getReadableDatabase()
				.rawQuery(
						"SELECT " + SCORE_COLUMN_ID + " , "
								+ SCORE_COLUMN_OPERATION + " , "
								+ SCORE_COLUMN_SCORE + " , "
								+ SCORE_COLUMN_TOTAL + " , "
								+ SCORE_COLUMN_LEVEL + " , " + SCORE_COLUMN_PERCENTAGE + " FROM " + SCORE_TABLE
								+ " ORDER BY " + SCORE_COLUMN_PERCENTAGE + " desc",
						null);

		return constantsCursor;
	}

	public Cursor getAllScoresForLevel(String level) {
		Cursor constantsCursor = (SQLiteCursor) this.getReadableDatabase()
				.rawQuery(
						"SELECT " + SCORE_COLUMN_ID + " , "
								+ SCORE_COLUMN_OPERATION + " , "
								+ SCORE_COLUMN_SCORE + " , "
								+ SCORE_COLUMN_TOTAL + " , "
								+ SCORE_COLUMN_LEVEL + " , "
								+ SCORE_COLUMN_PERCENTAGE + " FROM " + SCORE_TABLE
								+ " WHERE " + SCORE_COLUMN_LEVEL + "= " + "'"+ level + "'"
								+ " GROUP BY " + SCORE_COLUMN_OPERATION +"," +SCORE_COLUMN_PERCENTAGE +","  +SCORE_COLUMN_TOTAL
								+ " ORDER BY " + SCORE_COLUMN_PERCENTAGE + " desc",
						null);

		return constantsCursor;
	}

	public Cursor getRank(MathAppBean bean) {
		StringBuffer query = new StringBuffer();
		SQLiteDatabase db = this.getReadableDatabase();
		query.append("select distinct " + SCORE_COLUMN_PERCENTAGE + " from ")
				.append(SCORE_TABLE)
				.append(" Where ")
				.append(SCORE_COLUMN_OPERATION)
				.append("=")
				.append("'")
				.append(bean.getOperation())
				.append("'")
				.append(" and ")
				.append(SCORE_COLUMN_LEVEL)
				.append("=")
				.append("'")
				.append(bean.getLevel())
				.append("'").append(" and ")
				.append(SCORE_COLUMN_PERCENTAGE)
				.append(">=")
				.append(bean.getPercentage())
				.append(" order by " + SCORE_COLUMN_SCORE + " desc limit 3 ");

		Cursor res = db.rawQuery(query.toString(), null);
		return res;
	}
}
