package com.dingfch.dbutils.Admin;

import android.content.Context;

import com.dingfch.dbutils.DbUtils;
import com.dingfch.dbutils.User;
import com.dingfch.dbutils.DbUtils.DbUpgradeListener;
import com.dingfch.dbutils.exception.DbException;

public class DBAdmin {
	private static DbUtils mUtils;
	private static DBAdmin mInstance;
	private static String DB_Name = "test.db";
	private static int DB_Version = 1;
	
	public static DBAdmin getInstance(Context context){
		if(mInstance == null){
			mInstance = new DBAdmin();
		}
		if(mUtils == null){
			mUtils = DbUtils.create(context, DB_Name, DB_Version, new DbUpgradeListener() {
				
				@Override
				public void onUpgrade(DbUtils db, int oldVersion, int newVersion) {
					//TODO...
					
				}
			});
		}
		return mInstance;
	}
	
	public boolean saveUser(User user){
		if(user == null || mUtils == null) return false;
		try {
			mUtils.saveOrUpdate(user);
		} catch (DbException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	private DBAdmin(){
		/*DO NOT NEW ME*/
	}
}
