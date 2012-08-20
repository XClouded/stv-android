/**
 * 
 */
package com.stv.supervod.doc;

import com.stv.supervod.service.HttpDownloadImpl.KeyEnum;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Description: 先仅保存myfav中的信息
 * 
 * 数据库中表的主键 为 KeyEnum.id, 其余列名为KeyEnum
 * Copyright (c) 永新视博
 * All Rights Reserved.
 * @version 1.0  2012-1-16 下午4:29:34 zhaojunfeng created
 */
public class DB {
	private static final String TAG = "DB";
	private static final String DATABASE_NAME = "quickvodDataBase.db";
	private static final String DATABASE_TABLE_MYFAV = "myfavTable";
	private static final int DATABASE_VERSION = 2;
		
	// 内部变量
	private Context mCon;
	private dbHelper mDbHelp = null;
	private SQLiteDatabase mDb = null;
	
	/**
	 * Description: 获取数据库接口
	 * @Version1.0 2012-1-16 下午3:38:51 zhaojunfeng created
	 * @param c
	 * @return
	 */
	public static DB getDBInstance( Context c ) {
		if (mDbInstance == null) {
			mDbInstance = new DB( c );
		}
		return mDbInstance;
	}
	
	/**
	 * Description: 增加新的纪录
	 * @Version1.0 2012-1-16 下午3:54:45 zhaojunfeng created
	 * @param values
	 * @return
	 */
	public boolean addMyFav( ContentValues values ) {
		if( values == null ){
			return false;
		}	

		try {
			// 查询注意，值一定要加 ''
			StringBuilder where = new StringBuilder();
			where.append(KeyEnum.assetId.toString()).append("='")
				.append(values.getAsString(KeyEnum.assetId.toString())).append("'");
			
			Cursor cursor = mDb.query(DATABASE_TABLE_MYFAV, null, where.toString(), null, null, null, null);
			boolean isHaveAdd = (cursor.getCount() != 0);
			cursor.close();
			if( !isHaveAdd ){
				return (mDb.insert(DATABASE_TABLE_MYFAV, null, values) > 0 );
			}
			Log.d(TAG, "addMyFav have value");
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Description: 删除指定纪录
	 * @Version1.0 2012-1-16 下午3:55:00 zhaojunfeng created
	 * @param row_index
	 * @return
	 */
	public boolean delMyfav( int row_index ) {
		return (mDb.delete(DATABASE_TABLE_MYFAV, KeyEnum.id.toString() + "=" + row_index, null) > 0 );
	}
	
	/**
	 * Description: 获取myfav的所有纪录
	 * @Version1.0 2012-1-16 下午4:04:32 zhaojunfeng created
	 * @return
	 */
	public Cursor getAllMyfavCursor(){
		String[] columns = { KeyEnum.id.toString(), 
				KeyEnum.title.toString(),
				KeyEnum.type.toString(),
				KeyEnum.categories.toString(),
				KeyEnum.poster.toString(),
				KeyEnum.assetId.toString()};
		return mDb.query(DATABASE_TABLE_MYFAV, columns, null, null, null, null, null);
	}
	
	/**
	 * Description: 关闭数据库，必须在退出前调用
	 * @Version1.0 2012-1-16 下午4:11:03 zhaojunfeng created
	 */
	public void close() {
		mDb.close();
		mDbInstance = null;
	}
	
	/**
	 * 采用单实例进行数据库操作
	 */
	private static DB mDbInstance;
	private DB( Context c ) throws SQLException {
		mCon = c;
		mDbHelp = new dbHelper(mCon, DATABASE_NAME, null, DATABASE_VERSION);
		try {
			mDb = mDbHelp.getWritableDatabase();
		} catch (Exception e) {
			// TODO: handle exception
			mDb = mDbHelp.getReadableDatabase();
		}
	}
	
	/*
	 * db辅助类
	 */
	private static class dbHelper extends SQLiteOpenHelper{

		public dbHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			StringBuffer sql = new StringBuffer();
			sql.append("CREATE TABLE ").append(DATABASE_TABLE_MYFAV).append(" (");
			sql.append(KeyEnum.id.toString()).append(" INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT,");
			sql.append(KeyEnum.title.toString()).append(" TEXT NOT NULL,");
			sql.append(KeyEnum.type.toString()).append(" TEXT NOT NULL,");
			sql.append(KeyEnum.categories.toString()).append(" TEXT NOT NULL,");
			sql.append(KeyEnum.poster.toString()).append(" TEXT NOT NULL,");
			sql.append(KeyEnum.assetId.toString()).append(" TEXT NOT NULL");
			sql.append(")");
			db.execSQL(sql.toString());
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			Log.w(TAG, "Upgrade db from " + oldVersion + " to " + newVersion);
	        StringBuffer sql = new StringBuffer();  
	        sql.append("DROP TABLE IF EXISTS ").append(DATABASE_TABLE_MYFAV);  
	        db.execSQL(sql.toString());  
	        onCreate(db); 
		}
		
	}
}
