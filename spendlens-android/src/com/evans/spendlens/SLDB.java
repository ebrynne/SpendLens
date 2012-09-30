package com.evans.spendlens;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SLDB extends SQLiteOpenHelper {

	
	final static int DB_VERSION = 1;
	final static String DB_NAME = "sl.s3db";
	Context context;
	    
	public SLDB(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte buf[] = new byte[1024];
	    int len;
	    AssetManager assetManager = context.getAssets();
	    InputStream inputStream = null;
	    try{
	    	inputStream = assetManager.open(DB_NAME);
	    	while ((len = inputStream.read(buf)) != -1) {
	    		outputStream.write(buf, 0, len);
	    	}
	    	outputStream.close();
	    	inputStream.close();
	        
	        String[] createScript = outputStream.toString().split(";");
	        for (int i = 0; i < createScript.length; i++) {
	        	String sqlStatement = createScript[i].trim();
	            if (sqlStatement.length() > 0) {
	            	db.execSQL(sqlStatement + ";");
	            }
	        }
	    } catch (IOException e){
	    	// TODO Handle Script Failed to Load
	    } catch (SQLException e) {
	    	// TODO Handle Script Failed to Execute
	    }
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS purchases");
		db.execSQL("DROP TABLE IF EXISTS summaries");
	    onCreate(db);
	}

}
