package com.example.util;

import android.app.Activity;
import android.content.SharedPreferences;

public class MySharedPreferences {
	private Activity acivity;
	
	public MySharedPreferences(Activity activity){
		this.acivity = activity;
	}
	/**
	 * ��ȡ��Ӧ�ļ�ֵ
	 * @param key
	 * @return String
	 */
	public int readMessage(String key, int value) {
		//��õ�ǰ��SharedPreferences����
		SharedPreferences message = acivity.getPreferences(Activity.MODE_PRIVATE);
		//��ȡ��Ϣ
		int tmp = message.getInt(key, value);
		return tmp;
	}
	/**
	 * ����ֵ��д�������ļ�
	 * @param key
	 * @param value
	 */
	public void writeMessage(String key, int value) {
		//����һ��SharedPreferences����
		SharedPreferences message = acivity.getPreferences(0);
		//�༭SharedPreferences����
		SharedPreferences.Editor editor = message.edit();
		//����һ������
		editor.putInt(key, value);
		//�ύ����
		editor.commit();
	}
	public String readMessage(String key, String value) {
		SharedPreferences message = acivity.getPreferences(Activity.MODE_PRIVATE);
		String text = message.getString(key, value);
		return text;
	}
	public void writeMessage(String key, String value) {
		SharedPreferences message = acivity.getPreferences(0);
		SharedPreferences.Editor editor = message.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
}
