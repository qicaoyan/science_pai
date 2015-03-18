package com.science.activity;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;

import com.example.science.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.science.activity.Android_DialogActivity.MyThread;
import com.science.http.HttpUtil;
import com.science.json.JsonDownLoadsKeywords;
import com.science.json.JsonHotPageMainHandler;
import com.science.services.FunctionManage;
import com.science.services.MyApplication;
import com.science.util.AppUtil;
import com.science.util.AsyncImageLoader;
import com.science.util.NetWorkState;
import com.science.util.Url;
import com.science.util.AsyncImageLoader.ImageCallback;
import com.science.view.MyHeader;

import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class MainActivity extends Activity {
	
	 private MyApplication myApplication=null;
	 private FunctionManage functionManage=null;
	
	 private ViewPager viewPager;  
	 private ArrayList<View> pageViews;  
	 private ImageView imageView;  
	 private ImageView[] imageViews; 
	 private Button hotButton=null;
	 private Button mainButton=null;
	 
	 private MyHeader myHeader=null;
	 
	 // ��������ͼƬLinearLayout
	 private ViewGroup main;
	 // ����СԲ���LinearLayout
	 private ViewGroup group;
	 private TextView hotView;
	 private TextView mainView;
	 
	 
	 
	 public MyHandler handler;
	 public JsonHotPageMainHandler jsonHotPage=null;
	 public JsonHotPageMainHandler jsonHotPage1=null;
	 public JsonHotPageMainHandler jsonHotPage2=null;
	 public JsonHotPageMainHandler jsonHotPage3=null;
	 public JsonDownLoadsKeywords jsonDownLoadsKeywords=null;
	 public List<Map<String, String>> list0=null;
	 public List<Map<String, String>> list1=null;
	 public List<Map<String, String>> list2=null;
	 public List<Map<String, String>> list3=null;
	 public Map<String,String> keywordsMap=null;
	 
	 private ImageView hotpageImage1;
	 private ImageView hotpageImage2;
	 private ImageView hotpageImage3;
	 private ImageView hotpageImage4;
	 
	public ImageView mainPageImageView1;
	public ImageView mainPageImageView2;
	public ImageView mainPageImageView3;
	public ImageView mainPageImageView4;
	 
	 private ListView hotpageListView1;
	 private ListView hotpageListView2;
	 private ListView hotpageListView3;
	 private ListView hotpageListView4;
	 
	 private LinearLayout hotpageblock1;
	 private LinearLayout hotpageblock2;
	 private LinearLayout hotpageblock3;
	 private LinearLayout hotpageblock4;
	 
	 private ProgressBar hotpageProgressBar1;
	 
	 private View moreView1;
	 private View moreView2;
	 private View moreView3;
	 private View moreView4;
	 
	 private Button hotpageButton1;
	 private Button hotpageButton2;
	 private Button hotpageButton3;
	 private Button hotpageButton4;
	 
	 private TextView[] mainpageKeywords;
	 private ImageView[] mainpageKeywordsChar;
	 private boolean mainpageKeywordsEditeState=false;
	 private ImageButton keywordsEdite=null;
	 
	 private View v1;
	 private View v2;
	 private LinearLayout hotpagecontent;
	 private View hotmain;
	 private ViewSwitcher viewSwitcher;

	 
	 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        myApplication=(MyApplication)this.getApplication();
        functionManage=new FunctionManage(MainActivity.this);
     // �����ޱ��ⴰ��
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        initVariable();
        initViews();
        setClickListener();    
        
        requestNews();
        downloadKeywords();
        //hotButton.setBackground(getResources().getDrawable(R.drawable.hotpageselect));
        
        setContentView(main);

        if (myHeader!=null) {
        	myHeader.SetHeaderText("科学 · 派");
            String[] str=new String[2];
            str[0]="热点";
            str[1]="主页";
            myHeader.SetHeaderButtons(str);
		}
        else {
			Log.v("MainActivity", "myHeader is null");
		}
        
        myHeader.SetOnHeadButtonClickListener(onClickListener,0);
        myHeader.SetOnHeadButtonClickListener(onClickListener,1);
        
        myHeader.SetSelected(0);
        
        UpdateKeyWordsContent();
    }

		@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		downloadKeywords();
        UpdateKeyWordsContent();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		downloadKeywords();
        UpdateKeyWordsContent();
	}

		@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
			
        //可以根据多个请求代码来作相应的操作  
        if(20==resultCode)  
        {  
        	downloadKeywords();
        	UpdateKeyWordsContent();
        	Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG);
        }  
        else if(21==requestCode)
        {
        	Toast.makeText(MainActivity.this, "Login File", Toast.LENGTH_LONG);
        }
		super.onActivityResult(requestCode, resultCode, data);
	}

		/**
		 * 按下键盘上返回按钮
		 */
		@Override
		public boolean dispatchKeyEvent(KeyEvent event) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				AppUtil.QuitHintDialog(MainActivity.this);
				return false;
			} else {
				return super.dispatchKeyEvent(event);
			}
		}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void initVariable()
    {
        handler=new MyHandler();
        jsonHotPage=new JsonHotPageMainHandler();
        jsonHotPage1=new JsonHotPageMainHandler();
        jsonHotPage2=new JsonHotPageMainHandler();
        jsonHotPage3=new JsonHotPageMainHandler();
        
        jsonDownLoadsKeywords=new JsonDownLoadsKeywords();
        
        pageViews = new ArrayList<View>();  
        
        mainpageKeywords=new TextView[8];
        mainpageKeywordsChar=new ImageView[8];
        
        mainpageKeywordsEditeState=false;
    }
    
	private void initViews()
	{
		LayoutInflater inflater = getLayoutInflater(); 
		main = (ViewGroup)inflater.inflate(R.layout.main, null); 

        
        v1 = (ViewGroup) inflater.inflate(R.layout.hotpage, null);
        v2 = inflater.inflate(R.layout.mainpage, null); 
        hotmain=inflater.inflate(R.layout.hotpagemain, null);
        
        hotpagecontent=(LinearLayout)v1.findViewById(R.id.hotpagecontent);
        
        pageViews.add(hotmain);  
        pageViews.add(v2); 
        
        viewSwitcher=(ViewSwitcher)hotmain.findViewById(R.id.viewswitcher_news);
        viewSwitcher.addView(v1);
        viewSwitcher.addView(getLayoutInflater().inflate(
				R.layout.layout_progress_page, null));
		viewSwitcher.showNext();
		viewSwitcher.setDisplayedChild(0);
				
        viewPager = (ViewPager)main.findViewById(R.id.guidePages);
        viewPager.setAdapter(new GuidePageAdapter());
        
        hotpageListView1=(ListView)v1.findViewById(R.id.hotpageListView1);
//        hotpageListView1.setCacheColorHint(Color.argb(0, 0, 0, 0));
//        hotpageListView1.setDivider(getResources().getDrawable(
//				R.drawable.list_divider_line));
//        hotpageListView1.setDividerHeight(1);
//        hotpageListView1.setSelector(R.drawable.list_item_selector);
        hotpageListView2=(ListView)v1.findViewById(R.id.hotpageListView2);
        hotpageListView3=(ListView)v1.findViewById(R.id.hotpageListView3);
        hotpageListView4=(ListView)v1.findViewById(R.id.hotpageListView4);
        
        hotpageblock1=(LinearLayout)v1.findViewById(R.id.hotpageblock1);
        
        hotpageProgressBar1=(ProgressBar)v1.findViewById(R.id.hotpageProgressBar);
        
         moreView1 = getLayoutInflater().inflate(R.layout.moredata, null);
         moreView2 = getLayoutInflater().inflate(R.layout.moredata, null);
         moreView3 = getLayoutInflater().inflate(R.layout.moredata, null);
         moreView4 = getLayoutInflater().inflate(R.layout.moredata, null);
         
        
		if(v2!=null)
		{			
			mainPageImageView1=(ImageView)v2.findViewById(R.id.mainPageImageView1);
			mainPageImageView2=(ImageView)v2.findViewById(R.id.mainPageImageView2);
			mainPageImageView3=(ImageView)v2.findViewById(R.id.mainPageImageView3);
			mainPageImageView4=(ImageView)v2.findViewById(R.id.mainPageImageView4);
			keywordsEdite=(ImageButton)v2.findViewById(R.id.keywordsedite);
			
			mainpageKeywords[0]=(TextView)v2.findViewById(R.id.mainpageKeywords1);
			mainpageKeywords[1]=(TextView)v2.findViewById(R.id.mainpageKeywords2);
			mainpageKeywords[2]=(TextView)v2.findViewById(R.id.mainpageKeywords3);
			mainpageKeywords[3]=(TextView)v2.findViewById(R.id.mainpageKeywords4);
			mainpageKeywords[4]=(TextView)v2.findViewById(R.id.mainpageKeywords5);
			mainpageKeywords[5]=(TextView)v2.findViewById(R.id.mainpageKeywords6);
			mainpageKeywords[6]=(TextView)v2.findViewById(R.id.mainpageKeywords7);
			mainpageKeywords[7]=(TextView)v2.findViewById(R.id.mainpageKeywords8);
			
			mainpageKeywordsChar[0]=(ImageView)v2.findViewById(R.id.cha1);
			mainpageKeywordsChar[1]=(ImageView)v2.findViewById(R.id.cha2);
			mainpageKeywordsChar[2]=(ImageView)v2.findViewById(R.id.cha3);
			mainpageKeywordsChar[3]=(ImageView)v2.findViewById(R.id.cha4);
			mainpageKeywordsChar[4]=(ImageView)v2.findViewById(R.id.cha5);
			mainpageKeywordsChar[5]=(ImageView)v2.findViewById(R.id.cha6);
			mainpageKeywordsChar[6]=(ImageView)v2.findViewById(R.id.cha7);
			mainpageKeywordsChar[7]=(ImageView)v2.findViewById(R.id.cha8);
			
		}
		if(main!=null)
		{
//			hotButton=(Button)main.findViewById(R.id.mainhot);
//			mainButton=(Button)main.findViewById(R.id.mainmainpage);
			myHeader=(MyHeader)main.findViewById(R.id.mainheader);
		}
	}
	
	private void setClickListener()
	{
		viewPager.setOnPageChangeListener(onPageChangeListener);
		
		mainPageImageView1.setOnClickListener(onClickListenerMain);
		mainPageImageView2.setOnClickListener(onClickListenerMain);
		mainPageImageView3.setOnClickListener(onClickListenerMain);
		mainPageImageView4.setOnClickListener(onClickListenerMain);
		
//		hotButton.setOnClickListener(onClickListener);
//		mainButton.setOnClickListener(onClickListener);
		
		hotpageListView1.setOnItemClickListener(onItemClickListenerHotPageListView1);
		hotpageListView2.setOnItemClickListener(onItemClickListenerHotPageListView2);
		hotpageListView3.setOnItemClickListener(onItemClickListenerHotPageListView3);
		hotpageListView4.setOnItemClickListener(onItemClickListenerHotPageListView4);
		
		mainpageKeywords[0].setOnClickListener(onClickListenerKeywords);
		mainpageKeywords[1].setOnClickListener(onClickListenerKeywords);
		mainpageKeywords[2].setOnClickListener(onClickListenerKeywords);
		mainpageKeywords[3].setOnClickListener(onClickListenerKeywords);
		mainpageKeywords[4].setOnClickListener(onClickListenerKeywords);
		mainpageKeywords[5].setOnClickListener(onClickListenerKeywords);
		mainpageKeywords[6].setOnClickListener(onClickListenerKeywords);
		mainpageKeywords[7].setOnClickListener(onClickListenerKeywords);
		keywordsEdite.setOnClickListener(onClickListenerKeywordsEdit);
	}
    
	private void GetData(String url,int what)
    {
    	 String urlString = "http://client.azrj.cn/json/cook/cook_list.jsp?type=1&p=2&size=10"; // һ���@ȡ���׵�url��ַ
         HttpUtil.get(urlString, new AsyncHttpResponseHandler() {
             public void onSuccess(String arg0) {

             };
             public void onFailure(Throwable arg0) { 

             };
             public void onFinish() { 
             };
         });
    }
    
	private OnClickListener onClickListenerKeywordsEdit=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (!myApplication.IsLogin()) {
				functionManage.Login();
				return;
			}
			mainpageKeywordsEditeState=!mainpageKeywordsEditeState;
			if (mainpageKeywordsEditeState) {
				//keywordsEdite.setBackgroundDrawable(getResources().getDrawable(R.drawable.bianjiwancheng));
				//keywordsEdite.setBackgroundResource(R.drawable.bianjiwancheng);
				keywordsEdite.setBackground(getResources().getDrawable(R.drawable.bianjiwancheng));
				for(int i=0;i<8;i++)
				{
					if (mainpageKeywords[i].getText().length()>0) {
						mainpageKeywords[i].setBackgroundColor(getResources().getColor(R.color.keywordsbackground));
						mainpageKeywordsChar[i].setImageDrawable(getResources().getDrawable(R.drawable.cha));
						mainpageKeywordsChar[i].setBackgroundColor(Color.GREEN);
						//mainpageKeywordsChar[i].setBackground(getResources().getDrawable(R.drawable.cha));
						mainpageKeywordsChar[i].setVisibility(View.VISIBLE);
					}
					
				}
			}
			else {
				//keywordsEdite.setBackgroundDrawable(getResources().getDrawable(R.drawable.bianji));
				keywordsEdite.setBackgroundResource(R.drawable.bianji);
				for(int i=0;i<8;i++)
				{
					
					if (mainpageKeywords[i].getText().length()==0) {
						mainpageKeywords[i].setBackground(getResources().getDrawable(R.drawable.tj));
					}
					mainpageKeywordsChar[i].setVisibility(View.INVISIBLE);
				}
				
				
			}

		}
	};
	
	private OnClickListener onClickListenerKeywords=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			TextView bt=(TextView)v;
			String btnTextString=(String) bt.getText();		
			final View temp=v;
			if(bt.getText().length()==0) {
				
				if (!myApplication.IsLogin()) {
					functionManage.Login();
					return;
				}
				 LayoutInflater factory = LayoutInflater  
		                    .from(MainActivity.this);  
		            final View DialogView = factory.inflate(R.layout.keywordsinput, null);
		            
		           final EditText edt=(EditText)DialogView.findViewById(R.id.KeywordsEditText);
		           edt.setText(btnTextString);
				 AlertDialog dlg = new AlertDialog.Builder(  
		                    MainActivity.this)  
		                    .setTitle("输入关键词")  
		                    .setView(DialogView)  
		                    .setPositiveButton("确定",  
		                            new DialogInterface.OnClickListener() {  

		                                @Override  
		                                public void onClick(  
		                                        DialogInterface dialog,  
		                                        int which) {  
		                                    // TODO Auto-generated method  
		                                    // stub  
		                        			switch (temp.getId()) {
		                        			case R.id.mainpageKeywords1:
		                        				mainpageKeywords[0].setText(edt.getText().toString());
		                        				break;
		                        			case R.id.mainpageKeywords2:
		                        				mainpageKeywords[1].setText(edt.getText().toString());
		                        				break;
		                        			case R.id.mainpageKeywords3:
		                        				mainpageKeywords[2].setText(edt.getText().toString());
		                        				break;
		                        			case R.id.mainpageKeywords4:
		                        				mainpageKeywords[3].setText(edt.getText().toString());
		                        				break;
		                        			case R.id.mainpageKeywords5:
		                        				mainpageKeywords[4].setText(edt.getText().toString());
		                        				break;
		                        			case R.id.mainpageKeywords6:
		                        				mainpageKeywords[5].setText(edt.getText().toString());
		                        				break;
		                        			case R.id.mainpageKeywords7:
		                        				mainpageKeywords[6].setText(edt.getText().toString());
		                        				break;
		                        			case R.id.mainpageKeywords8:
		                        				mainpageKeywords[7].setText(edt.getText().toString());
		                        				break;
		                        			default:
		                        				break;
		                        			}
		                        			updateKeywords();
		                                }  
		                            })  
		                    .setNegativeButton("取消",  
		                            new DialogInterface.OnClickListener() {  

		                                @Override  
		                                public void onClick(  
		                                        DialogInterface dialog,  
		                                        int which) {  
		                                }  
		                            }).create();  
		            dlg.show();  
			}
			else if(mainpageKeywordsEditeState&&bt.getText().length()>0)
			{
				switch (temp.getId()) {
    			case R.id.mainpageKeywords1:
    				mainpageKeywords[0].setText("");
    				mainpageKeywordsChar[0].setVisibility(View.INVISIBLE);
    				break;
    			case R.id.mainpageKeywords2:
    				mainpageKeywords[1].setText("");
    				mainpageKeywordsChar[1].setVisibility(View.INVISIBLE);
    				break;
    			case R.id.mainpageKeywords3:
    				mainpageKeywords[2].setText("");
    				mainpageKeywordsChar[2].setVisibility(View.INVISIBLE);
    				break;
    			case R.id.mainpageKeywords4:
    				mainpageKeywords[3].setText("");
    				mainpageKeywordsChar[3].setVisibility(View.INVISIBLE);
    				break;
    			case R.id.mainpageKeywords5:
    				mainpageKeywords[4].setText("");
    				mainpageKeywordsChar[4].setVisibility(View.INVISIBLE);
    				break;
    			case R.id.mainpageKeywords6:
    				mainpageKeywords[5].setText("");
    				mainpageKeywordsChar[5].setVisibility(View.INVISIBLE);
    				break;
    			case R.id.mainpageKeywords7:
    				mainpageKeywords[6].setText("");
    				mainpageKeywordsChar[6].setVisibility(View.INVISIBLE);
    				break;
    			case R.id.mainpageKeywords8:
    				mainpageKeywords[7].setText("");
    				mainpageKeywordsChar[7].setVisibility(View.INVISIBLE);
    				break;
    			default:
    				break;
    			}
				updateKeywords();
			}
		}
	};
	
	private OnClickListener onClickListenerMain = new OnClickListener() {

		@Override
		public void onClick(View v) {
			//avg_width = findViewById(R.id.layout).getWidth();
			
//			if (LoginManage.sidString==null) {
//				//定义一个Intent
//				   Intent intent = new Intent(MainActivity.this, Android_DialogActivity.class);
//				   startActivity(intent);
//				   return ;
//			}

			switch (v.getId()) {
			case R.id.mainPageImageView1://进入项目申报页面
				//定义一个Intent
				   Intent intent = new Intent(MainActivity.this, ProjectApplyActivity.class);
				   startActivity(intent);
				break;
			case R.id.mainPageImageView2:
				//定义一个Intent
				   Intent intent1 = new Intent(MainActivity.this, DocumentExpressActivity.class);
				   startActivity(intent1);
				break;
			case R.id.mainPageImageView3:
				
				break;
			case R.id.mainPageImageView4:
				
				break;
			default:
				break;
			}
		}
	};
    
	private OnClickListener onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case 0:
//				hotButton.setBackground(getResources().getDrawable(R.drawable.hotpageselect));
//				mainButton.setBackground(getResources().getDrawable(R.drawable.mainpage));
				viewPager.setCurrentItem(0);
				break;
			case 1:
//				hotButton.setBackground(getResources().getDrawable(R.drawable.hotpage));
//				mainButton.setBackground(getResources().getDrawable(R.drawable.mainpageselect));
				viewPager.setCurrentItem(1);
				break;
			default:
				break;
			}
		}
	};

	private OnItemClickListener onItemClickListenerHotPageListView1=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			if (arg2>=list0.size()) {
				Log.v("MainActivity", "more data");
				Intent intent=new Intent();//Intent可以在不同的应用程序的Activity发送数据
				intent.setClass(MainActivity.this, HotPageListActivity.class);//从哪里跳到哪里
				intent.putExtra("block", "0");//传递数据
				intent.putExtra("title",getResources().getString(R.string.hotpage_title1));
				startActivity(intent);
				return;
			}

			
//			if (myApplication.IsLogin()) {
//				String url=list0.get(arg2).get("filename");
//				Log.v("test", url);
//				Intent intent=new Intent();//Intent可以在不同的应用程序的Activity发送数据
//				intent.setClass(MainActivity.this, HotPage1ContentActivity.class);//从哪里跳到哪里
//				intent.putExtra("url", url);//传递数据
//				startActivity(intent);
//			}
//			else {
//				
//				//定义一个Intent
//				   Intent intent = new Intent(MainActivity.this, Android_DialogActivity.class);
//				   intent.putExtra("title",getResources().getString(R.string.hotpage_title1));
//				   startActivity(intent);
//			}
			String url=list0.get(arg2).get("filename");
			Log.v("test", url);
			Intent intent=new Intent();//Intent可以在不同的应用程序的Activity发送数据
			intent.setClass(MainActivity.this, CommonContentActivity.class);//从哪里跳到哪里
			intent.putExtra("url", url);//传递数据
			intent.putExtra("title",getResources().getString(R.string.hotpage_title1));
			intent.putExtra("act_class", "热点新闻");
			startActivity(intent);
			
		}
	};
	
	private OnItemClickListener onItemClickListenerHotPageListView2=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			if (arg2>=list1.size()) {
				Log.v("MainActivity", "more data");
				Intent intent=new Intent();//Intent可以在不同的应用程序的Activity发送数据
				intent.setClass(MainActivity.this, HotPageListActivity.class);//从哪里跳到哪里
				intent.putExtra("block", "1");//传递数据
				intent.putExtra("title",getResources().getString(R.string.hotpage_title2));
				startActivity(intent);
				return;
			}

			
//			if (myApplication.IsLogin()) {
//				String url=list1.get(arg2).get("filename");
//				Log.v("test", url);
//				Intent intent=new Intent();//Intent可以在不同的应用程序的Activity发送数据
//				intent.setClass(MainActivity.this, HotPage1ContentActivity.class);//从哪里跳到哪里
//				intent.putExtra("url", url);//传递数据
//				startActivity(intent);
//			}
//			else {
//				
//				//定义一个Intent
//				   Intent intent = new Intent(MainActivity.this, Android_DialogActivity.class);
//				   startActivity(intent);
//			}
			String url=list1.get(arg2).get("filename");
			Log.v("test", url);
			Intent intent=new Intent();//Intent可以在不同的应用程序的Activity发送数据
			intent.setClass(MainActivity.this, CommonContentActivity.class);//从哪里跳到哪里
			intent.putExtra("url", url);//传递数据
			intent.putExtra("title",getResources().getString(R.string.hotpage_title2));
			intent.putExtra("act_class", "热点新闻");
			startActivity(intent);
			
		}
	};
	
	private OnItemClickListener onItemClickListenerHotPageListView3=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			if (arg2>=list2.size()) {
				Log.v("MainActivity", "more data");
				Intent intent=new Intent();//Intent可以在不同的应用程序的Activity发送数据
				intent.setClass(MainActivity.this, HotPageListActivity.class);//从哪里跳到哪里
				intent.putExtra("block", "2");//传递数据
				intent.putExtra("title",getResources().getString(R.string.hotpage_title3));
				startActivity(intent);
				return;
			}

//			if (myApplication.IsLogin()) {
//				String url=list2.get(arg2).get("filename");
//				Log.v("test", url);
//				Intent intent=new Intent();//Intent可以在不同的应用程序的Activity发送数据
//				intent.setClass(MainActivity.this, HotPage1ContentActivity.class);//从哪里跳到哪里
//				intent.putExtra("url", url);//传递数据
//				startActivity(intent);
//			}
//			else {
//				
//				//定义一个Intent
//				   Intent intent = new Intent(MainActivity.this, Android_DialogActivity.class);
//				   startActivity(intent);
//			}
			String url=list2.get(arg2).get("filename");
			Log.v("test", url);
			Intent intent=new Intent();//Intent可以在不同的应用程序的Activity发送数据
			intent.setClass(MainActivity.this, CommonContentActivity.class);//从哪里跳到哪里
			intent.putExtra("url", url);//传递数据
			intent.putExtra("title",getResources().getString(R.string.hotpage_title3));
			intent.putExtra("act_class", "热点新闻");
			startActivity(intent);
		}
	};
	
	private OnItemClickListener onItemClickListenerHotPageListView4=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			if (arg2>=list3.size()) {
				Log.v("MainActivity", "more data");
				Intent intent=new Intent();//Intent可以在不同的应用程序的Activity发送数据
				intent.setClass(MainActivity.this, HotPageListActivity.class);//从哪里跳到哪里
				intent.putExtra("block", "3");//传递数据
				intent.putExtra("title",getResources().getString(R.string.hotpage_title4));
				intent.putExtra("act_class", "热点新闻");
				startActivity(intent);
				return;
			}

			
//			if (myApplication.IsLogin()) {
//				String url=list3.get(arg2).get("filename");
//				Log.v("test", url);
//				Intent intent=new Intent();//Intent可以在不同的应用程序的Activity发送数据
//				intent.setClass(MainActivity.this, HotPage1ContentActivity.class);//从哪里跳到哪里
//				intent.putExtra("url", url);//传递数据
//				startActivity(intent);
//			}
//			else {
//				
//				//定义一个Intent
//				   Intent intent = new Intent(MainActivity.this, Android_DialogActivity.class);
//				   startActivity(intent);
//			}
			String url=list3.get(arg2).get("filename");
			Log.v("test", url);
			Intent intent=new Intent();//Intent可以在不同的应用程序的Activity发送数据
			intent.setClass(MainActivity.this, CommonContentActivity.class);//从哪里跳到哪里
			intent.putExtra("url", url);//传递数据
			intent.putExtra("title",getResources().getString(R.string.hotpage_title4));
			intent.putExtra("act_class", "热点新闻");
			startActivity(intent);
		}
	};
	
	private OnPageChangeListener onPageChangeListener=new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			if(arg0==0)
			{
				myHeader.SetSelected(0);
			}
			else if(arg0==1)
			{
				myHeader.SetSelected(1);
			}
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			Log.v("1","onPageScrolled");
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			Log.v("1","onPageScrollStateChanged");
		}
	};
	   
    private class GuidePageAdapter extends PagerAdapter {  
    	  
        @Override  
        public int getCount() {  
            return pageViews.size();  
        }  
  
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {  
            return arg0 == arg1;  
        }  
  
        @Override  
        public int getItemPosition(Object object) {  
            // TODO Auto-generated method stub  
            return super.getItemPosition(object);  
        }  
  
        @Override  
        public void destroyItem(View arg0, int arg1, Object arg2) {  
            // TODO Auto-generated method stub  
            ((ViewPager) arg0).removeView(pageViews.get(arg1));  
        }  
  
        @Override  
        public Object instantiateItem(View arg0, int arg1) {  
            // TODO Auto-generated method stub  
            ((ViewPager) arg0).addView(pageViews.get(arg1));  
            return pageViews.get(arg1);  
        }  
  
        @Override  
        public void restoreState(Parcelable arg0, ClassLoader arg1) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public Parcelable saveState() {  
            // TODO Auto-generated method stub  
            return null;  
        }  
  
        @Override  
        public void startUpdate(View arg0) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void finishUpdate(View arg0) {  
            // TODO Auto-generated method stub  
  
        }  
    } 
    
	private void requestNews() {
		MyThread mThread=new MyThread();
		new Thread(mThread).start();
		
	}
	
	private void downloadKeywords()
	{
		if (myApplication.IsLogin()) {
			MyThreadDownLoadKeyWords myThreadDownLoadKeyWords=new MyThreadDownLoadKeyWords();
			new Thread(myThreadDownLoadKeyWords).start();
		}
	}
	
	private void updateKeywords()
	{
		for (int i = 0; i < mainpageKeywords.length; i++) {
			myApplication.keywords[i]=mainpageKeywords[i].getText().toString();
		}
		//先更新一下application中的non_null_keywords_list
		myApplication.updateNonNullKeywordsList();
		UpdateKeyWordsContent();

	}
    private void getNewsList(String address,int block) {
    	List<Map<String, String>> list=null;
		if (!NetWorkState.isNetworkAvailable(this)) { // 判断网络连接情况
			handler.sendEmptyMessage(0);
			Log.v("test", "network wrong");
			return;
		}
		try {
//			// 获取新闻列表，存到list里边
			URL url = new URL(address+Integer.toString(block));
			URLConnection con = url.openConnection();
			con.connect();
			InputStream input = con.getInputStream();
			Log.v("test", "get data success");
			switch (block) {
			case 0:	
				list0=jsonHotPage.getListItems(input);
				list=list0;
				break;
			case 1:
				list1=jsonHotPage1.getListItems(input);
				list=list1;
				break;
			case 2:
				list2=jsonHotPage2.getListItems(input);
				list=list2;
				break;
			case 3:
				list3=jsonHotPage3.getListItems(input);
				list=list3;
				break;
			default:
				break;
			}

			if (list.size() == 0) {
				handler.sendEmptyMessage(-1);
			} else {
				Message msg=new Message();
				msg.what=1;
	            Bundle bundle = new Bundle();    
                bundle.putString("block",Integer.toString(block));
                msg.setData(bundle);
				handler.sendMessage(msg);
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    private class MyHandler extends Handler {

    	@Override
    	public void dispatchMessage(Message msg) {
    		// TODO Auto-generated method stub
    		super.dispatchMessage(msg);
    	}

    	@Override
    	public void handleMessage(Message msg) {
    		// TODO Auto-generated method stub
    		if (msg.what == 1) {
    			Bundle bd=msg.getData();
    			String blockString=bd.getString("block");
				if (blockString.equals("0")) {
			        //hotpageListView1.addFooterView(bt1);
					hotpageProgressBar1.setVisibility(View.GONE);
			        hotpageListView1.addFooterView(moreView1);
					hotpageListView1.setAdapter(new MyListAdapter(list0, "0"));
					setListViewHeightBasedOnChildren(hotpageListView1);
//					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//					hotpageListView1.setLayoutParams(lp);
//					hotpageblock1.setLayoutParams(lp);
				}
				else if (blockString.equals("1")) {
					hotpageListView2.addFooterView(moreView2); 
					hotpageListView2.setAdapter(new MyListAdapter(list1, "1"));
					setListViewHeightBasedOnChildren(hotpageListView2);
				}
				else if (blockString.equals("2")) {
					hotpageListView3.addFooterView(moreView3);
					hotpageListView3.setAdapter(new MyListAdapter(list2, "2"));
					setListViewHeightBasedOnChildren(hotpageListView3);
				}
				else if (blockString.equals("3")) {
					hotpageListView4.addFooterView(moreView4);
					hotpageListView4.setAdapter(new MyListAdapter(list3, "3"));
					setListViewHeightBasedOnChildren(hotpageListView4);
				}

			} else if (msg.what == -1) {

			} else if (msg.what == 0) {

			} else if (msg.what == 2) {
				//更新关键字
				for (int i = 0; i < keywordsMap.size(); i++) {
					String temp=keywordsMap.get(Integer.toString(i));
					if (temp!=null) {
						mainpageKeywords[i].setText(temp);
						//mainpageKeywords[i].setBackgroundColor(getResources().getColor(R.color.background));
					}
				}
				updateKeywords();

			} else if (msg.what == 3) { // 进行搜索，将显示列表的区域显示为旋转的进度条
//				viewSwitcher.setDisplayedChild(1);
			} else if (msg.what == 4) {
//				Toast.makeText(ScienceNetNewsActivity.this, "无搜索结果",
//						Toast.LENGTH_LONG).show();
//				refreshPage();
			} else if (msg.what == 5) { // 广告请求完毕，可以刷新
//				if (adapter != null) {
//					adapter.notifyDataSetChanged();
//					listView.onRefreshComplete();
//				}
			} else if (msg.what == 6) { // 搜索结束，有搜索结果

			}
 
    		super.handleMessage(msg);
    	}

    	@Override
    	public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
    		// TODO Auto-generated method stub
    		return super.sendMessageAtTime(msg, uptimeMillis);
    	}

    	@Override
    	public String toString() {
    		// TODO Auto-generated method stub
    		return super.toString();
    	}
    	
    }

    private class MyListAdapter extends BaseAdapter
    {
    	private List<Map<String, String>> list=null;
    	private String name=null;
		AsyncImageLoader asyncImageLoader = new AsyncImageLoader(
				AppUtil.ITEM_IMG_WIDTH, AppUtil.ITEM_IMG_HEIGHT);
    	
    	public MyListAdapter(List<Map<String, String>> L,String N)
    	{
    		list=L;
    		name=N;
    	}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (list!=null) {
				return list.size();
			}
			else {
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (list!=null) {
				return list.get(position);
			}
			else {
				return null;
			}
			
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (position==0) {
				convertView = getLayoutInflater().inflate(R.layout.hotpageitemsmain,
						null);
				TextView textView=(TextView)convertView.findViewById(R.id.hotpageitemsMainTextView);
				TextView textTitleTextView=(TextView)convertView.findViewById(R.id.houpageitemsMainTitle);
				final ImageView imageView=(ImageView)convertView.findViewById(R.id.hotpageItemsMainImageView);
				
				String temp=list.get(position).get("title");
				textView.setText(temp);
				if("0"==name){
					textTitleTextView.setText(getResources().getText(R.string.hotpage_title1));
					textTitleTextView.setBackgroundColor(getResources().getColor(R.color.hotpagetitle1));
				}
				else if("1"==name) {
					textTitleTextView.setText(getResources().getText(R.string.hotpage_title2));
					textTitleTextView.setBackgroundColor(getResources().getColor(R.color.hotpagetitle2));
				}
				else if("2"==name){
					textTitleTextView.setText(getResources().getText(R.string.hotpage_title3));
					textTitleTextView.setBackgroundColor(getResources().getColor(R.color.hotpagetitle3));
				}
				else if("3"==name){
					textTitleTextView.setText(getResources().getText(R.string.hotpage_title4));
					textTitleTextView.setBackgroundColor(getResources().getColor(R.color.hotpagetitle4));
				}
				
				String urlString=list.get(position).get("imgurl");
				Drawable cachedImage = asyncImageLoader.loadDrawable(
						urlString, new ImageCallback() {
							
							@Override
							public void imageLoaded(Drawable imageDrawable, String imageUrl) {
								// TODO Auto-generated method stub
								if (imageView != null && imageDrawable != null) {
									//imageView.setImageDrawable(imageDrawable);
									imageView.setBackground(imageDrawable);
									// Log.e("在回调里面设置好图片", "liushuai");
								} else {
									try {
										imageView.setBackground(getResources().getDrawable(R.drawable.sync));
										//imageView.setImageResource(R.drawable.sync);
										// Log.e("在回调里面设置了默认的图片", "liushuai");
									} catch (Exception e) {
										
									}
								}
							}
						});
			}
			else if (position>0)
			{
				convertView = getLayoutInflater().inflate(R.layout.hotpageitems,
						null);
				TextView tView=(TextView)convertView.findViewById(R.id.hotpageItemsTextView);
				final ImageView imageView=(ImageView)convertView.findViewById(R.id.hotpageItemsImageView);
				
				String temp=list.get(position).get("title");
				String urlString=list.get(position).get("imgurl");
				
				tView.setText(temp);
				
				Drawable cachedImage = asyncImageLoader.loadDrawable(
						urlString, new ImageCallback() {
							
							@Override
							public void imageLoaded(Drawable imageDrawable, String imageUrl) {
								// TODO Auto-generated method stub
								if (imageView != null && imageDrawable != null) {
									imageView.setImageDrawable(imageDrawable);
									// Log.e("在回调里面设置好图片", "liushuai");
								} else {
									try {
										imageView.setImageResource(R.drawable.sync);
										// Log.e("在回调里面设置了默认的图片", "liushuai");
									} catch (Exception e) {
										
									}
								}
							}
						});
			}
			return convertView;
		}
    	
    }
    
    private class MyThread implements Runnable
    {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.v("test","thread start");
			getNewsList(Url.HOTPAGEURL0,0);
			getNewsList(Url.HOTPAGEURL1,1);
			getNewsList(Url.HOTPAGEURL2,2);
			getNewsList(Url.HOTPAGEURL3,3);
    	
		}
    }

    private class MyThreadDownLoadKeyWords implements Runnable
    {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			URL url;
			try {
				url = new URL(myApplication.ComposeToken(Url.DOWNLOADKEYWORDS));
				URLConnection con = url.openConnection();
				con.connect();
				InputStream input = con.getInputStream();
				keywordsMap=jsonDownLoadsKeywords.getListItems(input);
				if (keywordsMap!=null) {
					handler.sendEmptyMessage(2);
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

    }

    private class MyThreadUpdateKeywords implements Runnable
    {
		@Override
		public void run() {
			// TODO Auto-generated method stub
						URL url;
						try {
				    		String temp="";
				    		for(int i=0;i<myApplication.keywords.length;i++)
				    		{
				    			temp+=myApplication.keywords[i];
				    			temp+=" ";
				    		}
				    		temp=URLEncoder.encode(temp, "utf8");
							Log.v("MainActivity", myApplication.ComposeToken(Url.UpdateKeywords+temp));
							url = new URL(myApplication.ComposeToken(Url.UpdateKeywords+temp));
							URLConnection con = url.openConnection();
							con.connect();
							InputStream input = con.getInputStream();
//							keywordsMap=jsonDownLoadsKeywords.getListItems(input);
//							if (keywordsMap!=null) {
//								handler.sendEmptyMessage(2);
//							}
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		}
    	
    }
    
    public void setListViewHeightBasedOnChildren(ListView listView) {  
        ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) {  
            // pre-condition  
            return;  
        }  
  
        int totalHeight = 0;  
        for (int i = 0; i < listAdapter.getCount(); i++) {  
            View listItem = listAdapter.getView(i, null, listView);  
            listItem.measure(0, 0);  
            totalHeight += listItem.getMeasuredHeight();  
        }  
  
        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
        listView.setLayoutParams(params);  
    } 
    
    public void UpdateKeyWordsContent()
    {
    	for(int i=0;i<myApplication.keywords.length;i++)
    	{
    		if (myApplication.keywords[i].length()>0) {
				mainpageKeywords[i].setText(myApplication.keywords[i]);
				mainpageKeywords[i].setBackgroundColor(getResources().getColor(R.color.keywordsbackground));
				if (mainpageKeywordsEditeState) {
					mainpageKeywordsChar[i].setVisibility(View.VISIBLE);
				}
			}
    		else {
    			mainpageKeywords[i].setBackground(getResources().getDrawable(R.drawable.tj));
			}
    	}
    	
    	MyThreadUpdateKeywords myThreadUpdateKeywords=new MyThreadUpdateKeywords();
    	new Thread(myThreadUpdateKeywords).start();
    }
    
private PlatformActionListener paListener=new PlatformActionListener() {
		
		@Override
		public void onError(Platform arg0, int arg1, Throwable arg2) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
			// TODO Auto-generated method stub
			Toast.makeText(MainActivity.this, "complete", Toast.LENGTH_LONG);
		}
		
		@Override
		public void onCancel(Platform arg0, int arg1) {
			// TODO Auto-generated method stub
			Toast.makeText(MainActivity.this, "concel", Toast.LENGTH_LONG);
		}
	};
	
	
}
