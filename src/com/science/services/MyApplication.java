package com.science.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.science.activity.Android_DialogActivity.MyThread;
import com.science.json.JsonLoginHandler;
import com.science.util.Url;

import android.R.string;
import android.app.Application;
import android.util.Log;

public class MyApplication extends Application{

	public static String loginurl=null;
	public static String loginState=null;
	public  String sidString=null;
	public static String result=null;
	public static JsonLoginHandler jsonLoginHandler=new JsonLoginHandler();
	public static Map<String,String> my_keywords = null;
	static MyApplication instance;
	public  String [] keywords=null;
	public  static List<StringBuffer> non_null_keywords_list = new ArrayList<StringBuffer>();
	static MyApplication getInstance() {

	return	instance;

	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		if (keywords==null) {
			keywords=new String[8];
			for (int i = 0; i < keywords.length; i++) {
				keywords[i]="";
			}
		}
		instance= this;
	}

	public  String Login(String name,String pass)
	{
		if (name==null||pass==null) {
			return null;
		}
		else {
			loginurl=Url.LOGINURL;
			loginurl+="name=";
			loginurl+=name;
			loginurl+="&";
			loginurl+="pass=";
			loginurl+=pass;
			
			URL url;
			try {
				url = new URL(loginurl);
				Log.v("MyApplicationLogin", loginurl);
				URLConnection con;
				con = url.openConnection();
				con.connect();
				InputStream input = con.getInputStream();
				sidString=jsonLoginHandler.getListItems(input);
				
				return sidString;
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
//			MyThread myThread=new MyThread();
//			new Thread(myThread).start();
		}
		
		return null;
	}
	
	public  String ComposeToken(String str)
	{
		if (str!=null) {
			//str+="&";
			str+="sid=";
			str+=this.sidString;
			return str;
		}
		else {
			return null;
		}
	}
	
	public  Boolean IsLogin()
	{
		if (sidString!=null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	public void  updateNonNullKeywordsList()
	{
		non_null_keywords_list.clear();
		for(int i = 0;i < keywords.length;i++)
		{
			if(!keywords[i].isEmpty())
			{
				StringBuffer sb = new StringBuffer(keywords[i]);
				non_null_keywords_list.add(sb);
			}
		}
	}
	
	
	
	public class MyThread implements Runnable
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				URL url = new URL(loginurl);
				Log.v("MyApplicationLogin", loginurl);
				URLConnection con;
				con = url.openConnection();
				con.connect();
				InputStream input = con.getInputStream();
				sidString=jsonLoginHandler.getListItems(input);
				//Log.v("MyApplication", sidString);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				
			}
		}
		
	}
}
