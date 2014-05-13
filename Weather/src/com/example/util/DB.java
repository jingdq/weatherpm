package com.example.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DB {
	private static final String filename = android.os.Environment.getExternalStorageDirectory()+"/weather/database.db";
	//��ѯʡ
	public static List<Map<String, String>> getProvince(){
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(filename, null);
		String SQL = "select DISTINCT province from addressIdTbl";
		Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst();
        do{
        	Map<String, String> map = new HashMap<String, String>();
        	map.put("address", cursor.getString(0));
        	list.add(map);
        }while(cursor.moveToNext());
        cursor.close();
        database.close();
        return list;
	}
	//��ѯ��
	public static List<Map<String, String>> getCity(String province){
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(filename, null);
		String SQL = "select DISTINCT city from addressIdTbl where province = "+"'"+province+"'";
		Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst();
		do {
			Map<String, String> map = new HashMap<String, String>();
        	map.put("address", cursor.getString(0));
        	list.add(map);
		} while (cursor.moveToNext());
        cursor.close();
        database.close();
        return list;
	}
	//��ѯ�أ�����
	public static List<Map<String, String>> getCountry(String city){
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(filename, null);
		String SQL = "select country from addressIdTbl where city = "+"'"+city+"'";
		Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst();
        do{
        	Map<String, String> map = new HashMap<String, String>();
        	map.put("address", cursor.getString(0));
        	list.add(map);
        }while(cursor.moveToNext());
        cursor.close();
        database.close();
        return list;
	}
	//��ѯ��ַid��
	public static String getAddressId(String country){
		String result = "";
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(filename, null);
		String SQL = "select addressId from addressIdTbl where country = "+"'"+country+"'";
		Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst();
        do{
        	result = cursor.getString(0);
        }while(cursor.moveToNext());
        cursor.close();
        database.close();
        return result;
	}
	//������ӵĵ�����id��
	public static void saveCityAndId(String city, String id){
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(filename, null);
		ContentValues values = new ContentValues();
		values.put("city", city);
		values.put("addressId", id);
		database.insert("cityTbl", null, values);
		database.close();
	}
	//��ѯ��ӵĵ�����id��
	public static List<Map<String, String>> getCityAndId(){
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(filename, null);
		String SQL = "select city,addressId from cityTbl";
		Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
	        do{
	        	String city = cursor.getString(0);
	        	String id = cursor.getString(1);
	        	Map<String, String> map = new HashMap<String, String>();
	        	map.put("address", city);
	        	map.put("id", id);
	        	list.add(map);
	        }while(cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return list;
	}
	//ɾ��ָ��id�ĵ�����¼
	public static void deleteCityAndId(String id){
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(filename, null);
		String SQL = "delete from cityTbl where addressId = '" + id + "'";
		database.execSQL(SQL);
        database.close();
	}
}
