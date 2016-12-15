package com.education.officertopline.db;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.education.officertopline.log.LogUtil;


/**
 * Database SymboltechDBOpenHelper simple introduction
 *
 * <p>
 * detailed comment
 * 
 * @author CWI-APST Email:lei.zhang@symboltech.com 2015年10月28日
 * @see
 * @since 1.0
 */
public class EducationDBOpenHelper {

	private static final String TAG = "EducationDBOpenHelper";

	/** context */
	private final Context mCtx;
	/** DatabaseHelper */
	private DatabaseHelper mDbHelper;
	/** SQLiteDatabase */
	private SQLiteDatabase mDb;

	/** Database Name */
	private static final String DATABASE_NAME = "education_database";

	/** Database Version */
	private static final int DATABASE_VERSION = 1;



	private static final String DATABASE_TABLE_NEWSLIST = "channel_news_list";//频道下的新闻列表

	private static final String ID = "id";//主键id
	private static final String CHANNEL_NAME = "channelName";//频道名称
	private static final String CHANNEL_CODE = "channelCode"; // 频道编码
	private static final String NEWS_TYPE = "newsType";//新闻类型（1： 无图片类型，2：单图片类型,3： 多图片类型）
	private static final String TITLE = "title";//标题
	private static final String PIC = "pic";//名图片（以|分割多个地址）
	private static final String SRC = "src";//来源
	private static final String COMMENT_NUM = "commentNum";//评论数
	private static final String FIRST_TIME = "firstTime";//文章第一次发表时间
	private static final String FIRST_PERSON = "firstPerson";//文章撰写人
	private static final String KEYWORD = "keyword";//关键词
	/** table creat for paytype */
	private static final String CREAT_NEWSLIST_TABLE = "create table " + DATABASE_TABLE_NEWSLIST
			+ "(_id integer primary key autoincrement, " + ID + " double not null, " + CHANNEL_NAME + " varchar(20) not null, "
			+ CHANNEL_CODE + " varchar(10) not null, " + NEWS_TYPE+ " varchar(5) not null, "+ TITLE +" varchar(50), "
			+ PIC +" varchar(300), "+ SRC +" varchar(50), "+ FIRST_TIME + " timespace, " + FIRST_PERSON +" varchar(30), "
			+ KEYWORD +" varchar(50), "+ COMMENT_NUM +" double )";

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 *
	 * @param ctx
	 *            the Context within which to work
	 */
	public EducationDBOpenHelper(Context ctx) {
		this.mCtx = ctx;
	}

	/**
	 * 
	 * @author CWI-APST emial:26873204@qq.com
	 * @Description: (getDatabaseHelper)
	 * @return
	 * @throws SQLException
	 */
	public DatabaseHelper getDatabaseHelper() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		return mDbHelper;
	}

	/**
	 * Inner private class. Database Helper class for creating and updating
	 * database.
	 */
	public static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		/**
		 * onCreate method is called for the 1st time when database doesn't
		 * exists.
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("pragma foreign_keys=on");
			db.execSQL(CREAT_NEWSLIST_TABLE);
		}

		/**
		 * onUpgrade method is called when database version changes.
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			LogUtil.i(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
			if(newVersion > oldVersion){
				onCreate(db); //创建新的表
			}
		}
	}

}
