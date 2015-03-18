package com.science.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


import com.example.science.R;
import com.science.json.JsonDcumentListHandler;
import com.science.json.JsonProgramListHandler;
import com.science.json.JsonProjectProgramMainHandler;
import com.science.json.JsonProjectSelectListHandler;
import com.science.services.MyApplication;
import com.science.util.AppUtil;
import com.science.util.AsyncImageLoader;
import com.science.util.Url;
import com.science.util.AsyncImageLoader.ImageCallback;
import com.science.view.MyHeader;
import com.science.view.MyImageButton;
import com.science.model.FirstClassItem;
import com.science.model.SecondClassItem;



import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ProjectApplyActivity extends Activity {
	
	private MyApplication myApplication=null;

	
	
	private LinearLayout main=null;
	private String arrowState="";
	private ProgressBar projectprogramProgressBar=null;
	private View moreView=null;
	private String strUrl=null;
	private String str_url_proj_src;

	private MyImageButton fold_btn1;
	private MyImageButton fold_btn2;
	private MyImageButton[] proj_btns;
	private WindowManager wm;
	private Context context;

	
	private int screen_width;
	private int screen_height;
	
	private ListView  proj_list_view;
	private ListView  proj_src_list_view;
	private ListView  proj_src_next_list_view;
	private ListView  proj_type_list_view;
	private View      proj_src_pop_view;
	private View      proj_type_pop_view;
	private View      proj_src_btn;
	private View      proj_type_btn;
	private MyHeader  proj_apply_header;
	private PopupWindow  proj_src_pop_win;
	private PopupWindow   proj_type_pop_win;
	private ProjListAdapter proj_list_adapter;
	private ProjSourceAdapter first_adapter;
	private ProjSourceNextAdapter second_adapter;
    private ProjTypeAdapter proj_type_adapter;
	private View proj_more_view;
	private JsonProgramListHandler json = null;
	private JsonProjectSelectListHandler json_select = null;
	private MyApplication application;
	private static final int UPDATE_TAG_VIEW = 0x00; 
	private static final int UPDATE_TAG_VIEW2 = 0x01; 
    private static final int UPDATE_PROJ_LIST = 0x02;
    
	private List<Map<String,String>> proj_list;
	private List<FirstClassItem> proj_src_list;
	private List<SecondClassItem> proj_src_next_list;
    private List<FirstClassItem> proj_type_list;
    private List<SecondClassItem>proj_type_next_list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		LayoutInflater inflater = getLayoutInflater();  
		main=(LinearLayout)inflater.inflate(R.layout.projectapply, null); 
		setContentView(main);
		initVariable();
		initView();
	    requestData();//刚启动的时候刷新一次数据
		setOnClickListener();
	}
	
	private void initVariable()
	{
		
		
		moreView = getLayoutInflater().inflate(R.layout.moredata, null);
		strUrl=Url.ProjectListBase;
		str_url_proj_src = Url.ProjectUsual;
		context = this;
		json = new JsonProgramListHandler();
		json_select = new JsonProjectSelectListHandler();
		application = (MyApplication) this.getApplication();
		proj_list = null;
		screen_width = 0;
		screen_height = 0;

		
		    proj_src_list = new ArrayList<FirstClassItem>();
	        //1
	        ArrayList<SecondClassItem> proj_src_next_list1 = new ArrayList<SecondClassItem>();
	        proj_src_next_list1.add(new SecondClassItem(100, "全部"));
	        proj_src_next_list1.add(new SecondClassItem(101, "美国"));
	        proj_src_next_list1.add(new SecondClassItem(102, "加拿大"));
	        proj_src_next_list1.add(new SecondClassItem(103, "英国"));
	        proj_src_list.add(new FirstClassItem(1, "国际", proj_src_next_list1));
	        //2
	        ArrayList<SecondClassItem> proj_src_next_list2 = new ArrayList<SecondClassItem>();
	        proj_src_next_list2.add(new SecondClassItem(200, "全部"));
	        proj_src_next_list2.add(new SecondClassItem(201, "天津"));
	        proj_src_next_list2.add(new SecondClassItem(202, "北京"));
	        proj_src_next_list2.add(new SecondClassItem(203, "秦皇岛"));
	        proj_src_next_list2.add(new SecondClassItem(204, "沈阳"));
	        proj_src_next_list2.add(new SecondClassItem(205, "大连"));
	        proj_src_next_list2.add(new SecondClassItem(206, "哈尔滨"));
	        proj_src_next_list2.add(new SecondClassItem(207, "锦州"));
	        proj_src_next_list2.add(new SecondClassItem(208, "上海"));
	        proj_src_next_list2.add(new SecondClassItem(209, "杭州"));
	        proj_src_next_list2.add(new SecondClassItem(210, "南京"));
	        proj_src_next_list2.add(new SecondClassItem(211, "嘉兴"));
	        proj_src_next_list2.add(new SecondClassItem(212, "苏州"));
	        proj_src_list.add(new FirstClassItem(2, "国家", proj_src_next_list2));
	        //3
	        ArrayList<SecondClassItem> proj_src_next_list3 = new ArrayList<SecondClassItem>();
	        proj_src_next_list3.add(new SecondClassItem(300, "全部"));
	        proj_src_next_list3.add(new SecondClassItem(301, "南开区"));
	        proj_src_next_list3.add(new SecondClassItem(302, "和平区"));
	        proj_src_next_list3.add(new SecondClassItem(303, "河西区"));
	        proj_src_next_list3.add(new SecondClassItem(304, "河东区"));
	        proj_src_next_list3.add(new SecondClassItem(305, "滨海新区"));
	        proj_src_list.add(new FirstClassItem(3, "各部", proj_src_next_list3));
	        //4
	        ArrayList<SecondClassItem> proj_src_next_list4 = new ArrayList<SecondClassItem>();
	        proj_src_next_list4.add(new SecondClassItem(400, "全部"));
	        proj_src_next_list4.add(new SecondClassItem(401, "天津"));
	        proj_src_next_list4.add(new SecondClassItem(402, "北京"));
	        proj_src_next_list4.add(new SecondClassItem(403, "秦皇岛"));
	        proj_src_next_list4.add(new SecondClassItem(404, "沈阳"));
	        proj_src_next_list4.add(new SecondClassItem(405, "大连"));
	        proj_src_next_list4.add(new SecondClassItem(406, "哈尔滨"));
	        proj_src_next_list4.add(new SecondClassItem(407, "锦州"));
	        proj_src_next_list4.add(new SecondClassItem(408, "上海"));
	        proj_src_next_list4.add(new SecondClassItem(409, "杭州"));
	        proj_src_next_list4.add(new SecondClassItem(410, "南京"));
	        proj_src_next_list4.add(new SecondClassItem(411, "嘉兴"));
	        proj_src_next_list4.add(new SecondClassItem(412, "苏州"));
	        proj_src_list.add(new FirstClassItem(4, "地域", proj_src_next_list4));

	        proj_type_list = new ArrayList<FirstClassItem>();
	        proj_type_list.add(new FirstClassItem(1,"全部",null));
	        proj_type_list.add(new FirstClassItem(2,"社会科学",null));
	        proj_type_list.add(new FirstClassItem(3,"自然科学",null));
	        //copy
	        //area_source_list.addAll(area_source_list);
	}
	
	
	
private void requestData(){
	MyThread thread = new MyThread(0);
	new Thread(thread).start();
}
	private void initView()
	{
		wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		screen_width = wm.getDefaultDisplay().getWidth();
		screen_height = wm.getDefaultDisplay().getHeight();
		

		//findViewById
		proj_src_btn = findViewById(R.id.proj_src_layout);
		proj_type_btn = findViewById(R.id.proj_type_layout);
		proj_apply_header = (MyHeader) findViewById(R.id.proj_apply_header);
		
		//设置proj_apply的header
		proj_apply_header.SetHeaderText("项目申请");
		String[] header_btn_strs = {"热门推荐","即将到期","项目解读"};
		proj_apply_header.SetHeaderButtons(header_btn_strs);
		
		proj_list_view = (ListView)findViewById(R.id.proj_list_view);
		proj_list_view.setCacheColorHint(Color.argb(255, 0, 0, 0));
		proj_list_view.setSelector(R.drawable.list_item_selector);
		proj_list_view.setOnItemClickListener(proj_item_click_listener);
		
		/*加载更多按钮*/
		proj_more_view = (LinearLayout) getLayoutInflater().inflate(R.layout.listmoredata, null);
		proj_src_pop_view = getLayoutInflater().inflate(R.layout.pop_view_for_proj_src, null);
		proj_type_pop_view = getLayoutInflater().inflate(R.layout.pop_view_for_proj_type, null);
		proj_src_list_view = (ListView) proj_src_pop_view.findViewById(R.id.proj_src_list_view);
	    
		first_adapter = new ProjSourceAdapter(this,proj_src_list);
		proj_src_list_view.setAdapter(first_adapter);
		
	     proj_src_next_list_view = (ListView) proj_src_pop_view.findViewById(R.id.proj_src_next_list_view);
	     proj_src_next_list = new ArrayList<SecondClassItem>();
	     proj_src_next_list.addAll(proj_src_list.get(0).getSecondList());
	     
	     second_adapter = new ProjSourceNextAdapter(this, proj_src_next_list);
	     proj_src_next_list_view.setAdapter(second_adapter);
	     
	     
	     //proj_type处理
	     proj_type_list_view = (ListView)proj_type_pop_view.findViewById(R.id.proj_type_list_view);
	     proj_type_adapter = new ProjTypeAdapter(this,proj_type_list);
	     proj_type_list_view.setAdapter(proj_type_adapter);
	   //左侧ListView点击事件
	     proj_src_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                //二级数据
	                List<SecondClassItem> list2 = proj_src_list.get(position).getSecondList();
	                
                    String selectedName = proj_src_list.get(position).getName();
                    str_url_proj_src  = application.ComposeToken(str_url_proj_src);
                    try {
						str_url_proj_src += "&projSource1=" + URLEncoder.encode(selectedName,"utf-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                //如果没有二级类，则直接跳转
	                if (list2 == null || list2.size() == 0) {
	                    proj_src_pop_win.dismiss();

	                    int firstId = proj_src_list.get(position).getId();

	                    handleResult(firstId, -1, selectedName);
	                    return;
	                }

	                ProjSourceAdapter adapter = (ProjSourceAdapter) (parent.getAdapter());
	                //如果上次点击的就是这一个item，则不进行任何操作
	                if (adapter.getSelectedPosition() == position){
	                    return;
	                }

	                //根据左侧一级分类选中情况，更新背景色
	                adapter.setSelectedPosition(position);
	                adapter.notifyDataSetChanged();

	                //显示右侧二级分类
	                updateSecondListView(list2, second_adapter);
	            }
	        });

	     
	     
	   //右侧ListView点击事件
	     proj_src_next_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                //关闭popupWindow，显示用户选择的分类
	                proj_src_pop_win.dismiss();

	                int firstPosition = first_adapter.getSelectedPosition();
	                int firstId = proj_src_list.get(firstPosition).getId();
	                int secondId = proj_src_list.get(firstPosition).getSecondList().get(position).getId();
	                String selectedName = proj_src_list.get(firstPosition).getSecondList().get(position)
	                        .getName();
	                try {
						str_url_proj_src += "&projSource2=" + URLEncoder.encode(selectedName,"utf-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                
	                proj_list.clear();
	                proj_list_adapter.notifyDataSetChanged();
	                //proj_list_view.removeFooterView(proj_more_view);
	                second_adapter.setSelectedPosition(position);
	                second_adapter.notifyDataSetChanged();
	                
	                GetProjectThread get_proj_thread = new GetProjectThread(0);
	                new Thread(get_proj_thread).start();
	                handleResult(firstId, secondId, selectedName);
	            }
	        });
	     
	     
	     
	     
	     
	     
	     
	     
	     
	     
		   //项目类型ListView点击事件
	     proj_type_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                //关闭popupWindow，显示用户选择的分类
	                proj_type_pop_win.dismiss();             
	                String selectedName = proj_type_list.get(position).getName();
	                
	                try {
						str_url_proj_src += "&projType=" + URLEncoder.encode(selectedName,"utf-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                
	               
	                proj_list.clear();
	                proj_list_adapter.notifyDataSetChanged();
	                //proj_list_view.removeFooterView(proj_more_view);

	                
	                proj_type_adapter.notifyDataSetChanged();
	                proj_type_adapter.setSelectedPosition(position);
	                GetProjectThread get_proj_thread = new GetProjectThread(0);
	                new Thread(get_proj_thread).start();
	               // handleResult(firstId, secondId, selectedName);
	            }
	        });
	     
	     
	     
	     
	     
	    
	     
		//项目来源popupwindow
		proj_src_pop_win = new PopupWindow(proj_src_pop_view,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		proj_src_pop_win.setOutsideTouchable(true); 
		
		proj_src_pop_win.setBackgroundDrawable(new BitmapDrawable());
		proj_src_pop_win.setFocusable(true);
		proj_src_pop_win.setOnDismissListener(new PopupWindow.OnDismissListener(){

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				proj_src_list_view.setSelection(0);
				proj_src_next_list_view.setSelection(0);
				View vv = findViewById(R.id.proj_src_arrow);
				RotateAnimation rot_anim = new RotateAnimation(180,0,(float) (vv.getWidth()*0.5),(float) (vv.getHeight()*0.5));
				rot_anim.setFillAfter(true);
				rot_anim.setDuration(100);
				vv.startAnimation(rot_anim);
			}
			
		});
	
		
		
		//项目类型popupwindow
		proj_type_pop_win = new PopupWindow(proj_type_pop_view,screen_width/2,LayoutParams.WRAP_CONTENT);
		proj_type_pop_win.setOutsideTouchable(true); 
		
		proj_type_pop_win.setBackgroundDrawable(new BitmapDrawable());
		proj_type_pop_win.setFocusable(true);
		proj_type_pop_win.setOnDismissListener(new PopupWindow.OnDismissListener(){

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				proj_type_list_view.setSelection(0);
				View vv = findViewById(R.id.proj_type_arrow);
				RotateAnimation rot_anim = new RotateAnimation(180,0,(float) (vv.getWidth()*0.5),(float) (vv.getHeight()*0.5));
				rot_anim.setFillAfter(true);
				rot_anim.setDuration(100);
				vv.startAnimation(rot_anim);
			}
			
		});
		

	}
	
	  private class MyThread implements Runnable
		{
			private int index = 0;
			
			public MyThread(int temp)
			{
				index = temp;
			}

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				URL url;
				try{
					strUrl = application.ComposeToken(strUrl);
					strUrl +="&";
					//strUrl += "&date=2014-1-1" + "&class=" + URLEncoder.encode("企业","utf-8");
					url = new URL(strUrl);
					URLConnection con = url.openConnection();
					con.connect();
					InputStream input = con.getInputStream();
					List<Map<String,String>> temp = null;
					temp = json.getListItems(input);
					Log.i("strUrl", strUrl);
					if(temp != null)
					{
						proj_list = temp;
						Log.i("temp?==0","temp!=0");
						handler.sendEmptyMessage(ProjectApplyActivity.UPDATE_PROJ_LIST);
						
					}else
					{
						Log.i("temp?==0","temp==0");
						handler.sendEmptyMessage(3);
					}
				}catch(MalformedURLException e)
				{
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}

	  
	  
	  private class GetProjectThread implements Runnable
		{
			private int index = 0;
			
			public GetProjectThread(int temp)
			{
				index = temp;
			}

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				URL url;
				try{

					url = new URL(str_url_proj_src);
					URLConnection con = url.openConnection();
					con.connect();
					InputStream input = con.getInputStream();
					List<Map<String,String>> temp = null;
					temp = json_select.getListItems(input);
					
					if(temp != null)
					{
						proj_list = temp;
						handler.sendEmptyMessage(6);
						Log.i("temp?==0","temp!=0");
					}else
					{
						handler.sendEmptyMessage(3);
						
					}
				}catch(MalformedURLException e)
				{
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}

	 
	  
	  private Handler handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				switch(msg.what)
				{
				 
				//更新TAG_VIEW
				case ProjectApplyActivity.UPDATE_TAG_VIEW:
//					
					break;
				
				case ProjectApplyActivity.UPDATE_TAG_VIEW2:

//					
					break;    
					
				case ProjectApplyActivity.UPDATE_PROJ_LIST:
					Log.i("proj_apply","here!");
					proj_list_adapter = new ProjListAdapter();
					proj_list_view.setAdapter(proj_list_adapter);
					proj_list_view.addFooterView(proj_more_view);
					Log.i("add footer view", "added");
					break;
				case 3:
					
					break;
				case 6:
					
					proj_list_adapter.notifyDataSetChanged();
					proj_list_adapter = new ProjListAdapter();
					proj_list_view.setAdapter(proj_list_adapter);
					proj_list_view.addFooterView(proj_more_view);
					break;
					
				}
			}
		};

	  public void setOnClickListener()
		{

	
		

	        //设置proj_src_btn点击弹出事件
	        proj_src_btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					final View vv = findViewById(R.id.proj_src_arrow);
					
					if (proj_src_pop_win.isShowing()) {

						proj_src_pop_win.dismiss();		
			        }
					else{
					
						RotateAnimation rot_anim = new RotateAnimation(0,180,(float) (vv.getWidth()*0.5),(float) (vv.getHeight()*0.5));
						rot_anim.setFillAfter(true);
						rot_anim.setDuration(100);
						vv.startAnimation(rot_anim);
						proj_src_pop_win.showAsDropDown(findViewById(R.id.proj_src_layout));
					}
					

				}
	        	
	        });
	        
	        
	        
	        
	        
	        
	        //设置proj_type_btn点击弹出事件
	        proj_type_btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					final View vv = findViewById(R.id.proj_type_arrow);
					
					if (proj_type_pop_win.isShowing()) {

						proj_type_pop_win.dismiss();		
			        }
					else{
					
						RotateAnimation rot_anim = new RotateAnimation(0,180,(float) (vv.getWidth()*0.5),(float) (vv.getHeight()*0.5));
						rot_anim.setFillAfter(true);
						rot_anim.setDuration(100);
						vv.startAnimation(rot_anim);
						proj_type_pop_win.showAsDropDown(findViewById(R.id.proj_type_layout));
					}
					

				}
	        	
	        });
	        
		}
		
		
		

		private class ProjListAdapter extends BaseAdapter
		{

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return proj_list.size();
			}

			@Override
			public Object getItem(int item) {
				// TODO Auto-generated method stub
				return item;
			}

			@Override
			public long getItemId(int id) {
				// TODO Auto-generated method stub
				return id;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				
				LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.proj_list_item, null);
				TextView proj_title_text = (TextView) view.findViewById(R.id.proj_title);
				String title = proj_list.get(position).get("title");
				proj_title_text.setText(title);
				
				TextView proj_start_time = (TextView) view.findViewById(R.id.start_time);
				String start_time = proj_list.get(position).get("startTime");
				proj_start_time.setText(start_time);
				
				TextView proj_end_time = (TextView) view.findViewById(R.id.end_time);
				String end_time = proj_list.get(position).get("endTime");
				proj_end_time.setText(end_time);
				
				if(convertView == null)
				{
					convertView = view;
				}
				return convertView;
			}
			public void clearList(List<File> f) {
                int size = f.size();
                if (size > 0) {
                        f.removeAll(f);
                        proj_list_adapter.notifyDataSetChanged();
                }
        }
		}
		private OnItemClickListener proj_item_click_listener = new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				String url = application.ComposeToken(Url.ProjectContentBaseUrl) + "&id=" + proj_list.get(position).get("id");
				Log.i("proj_item_url", url);
				Intent intent = new Intent();//用以传递数据
				intent.setClass(ProjectApplyActivity.this, CommonContentActivity.class);
				intent.putExtra("url", url);
				intent.putExtra("act_class", "project");
				startActivity(intent);
			}
			
		};
		
//		private OnItemClickListener lv_cla_area_next_listener = new OnItemClickListener()
//		{
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View v, int position,
//					long id) {
//				// TODO Auto-generated method stub
//				String url = Url.DocumentContentBase + proj_list.get(position).get("id");
//				Log.i("proj_item_url", url);
//				Intent intent = new Intent();//用以传递数据
//				intent.setClass(ProjectExpress.this, ProjectApplicationContentActivity.class);
//				intent.putExtra("url", application.ComposeToken(url));
//				startActivity(intent);
//			}
//			
//		};
//		
		
		
		
		
		private class ProjSourceAdapter extends BaseAdapter{
			private Context context;
		    private List<FirstClassItem> list;
			public ProjSourceAdapter(Context context, List<FirstClassItem> list) {
		        this.context = context;
		        this.list = list;
		        Log.i("~~~~~~~~~~~~", "~~~~~~~~~~~~~~~~" + list.size());
		    }
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return proj_src_list.size();
				
			}

			@Override
			public Object getItem(int item) {
				// TODO Auto-generated method stub
				return item;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				ViewHolder holder;
				LinearLayout layout = new LinearLayout(ProjectApplyActivity.this);
				//layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				if (position == selectedPosition){
					ImageView iv = new ImageView(ProjectApplyActivity.this);
					iv.setImageResource(R.drawable.icon_green_point);
					layout.addView(iv);
					layout.setBackground(getResources().getDrawable(R.drawable.shape_dark_blue));
				  }else{
		            layout.setBackground(getResources().getDrawable(R.drawable.shape_light_blue));
				}
				layout.setGravity(Gravity.CENTER);
				TextView tv = new TextView(ProjectApplyActivity.this);
				tv.setTextColor(getResources().getColor(R.color.white));
				tv.setTextSize(15);
				tv.setText(proj_src_list.get(position).getName());
				tv.setPadding(5, 20, 0, 20);
				layout.addView(tv);
				convertView = layout;
				
				
				if(convertView == null)
				convertView = tv;
				return convertView;
			}
			
			private int selectedPosition = 0;

			    public void setSelectedPosition(int selectedPosition) {
			        this.selectedPosition = selectedPosition;
			    }

			    public int getSelectedPosition() {
			        return selectedPosition;
			    }
			    private class ViewHolder {
			        TextView nameTV;
			    }
		}
		
private class ProjSourceNextAdapter extends BaseAdapter{
	   private Context context;
       private List<SecondClassItem> list;

       public ProjSourceNextAdapter(Context context, List<SecondClassItem> list){
            this.context = context;
            this.list = list;
     }
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return proj_src_next_list.size();
				
			}

			@Override
			public Object getItem(int item) {
				// TODO Auto-generated method stub
				return item;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				LinearLayout layout = new LinearLayout(ProjectApplyActivity.this);
				//layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				
				if (position == selectedPosition){
					ImageView iv = new ImageView(ProjectApplyActivity.this);
					iv.setImageResource(R.drawable.icon_green_point);
					iv.setPadding(40, 10, 0, 10);
					layout.addView(iv);
					layout.setBackground(getResources().getDrawable(R.color.dark_blue));
				  }else{
		            layout.setBackground(getResources().getDrawable(R.color.light_blue));
				}
				//layout.setBackground(getResources().getDrawable(R.color.light_blue));
				layout.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
				TextView tv = new TextView(ProjectApplyActivity.this);
				tv.setTextColor(getResources().getColor(R.color.white));
				tv.setTextSize(15);
				tv.setText(proj_src_next_list.get(position).getName());
				if(position == selectedPosition)
					tv.setPadding(10, 10, 0, 10);
				else
					tv.setPadding(50, 10, 0, 10);
				
				layout.addView(tv);
				convertView = layout;
				return convertView;
			}
			
			
			private int selectedPosition = 0;

		    public void setSelectedPosition(int selectedPosition) {
		        this.selectedPosition = selectedPosition;
		    }

		    public int getSelectedPosition() {
		        return selectedPosition;
		    }
		    private class ViewHolder {
		        TextView nameTV;
		    }
			
		}



 private class ProjTypeAdapter extends BaseAdapter
 {
	   private Context context;
       private List<FirstClassItem> list;

       public ProjTypeAdapter(Context context, List<FirstClassItem> list){
            this.context = context;
            this.list = list;
     }
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return proj_type_list.size();
				
			}

			@Override
			public Object getItem(int item) {
				// TODO Auto-generated method stub
				return item;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				LinearLayout layout = new LinearLayout(ProjectApplyActivity.this);
				//layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				
				if (position == selectedPosition){
					ImageView iv = new ImageView(ProjectApplyActivity.this);
					iv.setImageResource(R.drawable.icon_green_point);
					iv.setPadding(40, 10, 0, 10);
					layout.addView(iv);
					
					layout.setBackground(getResources().getDrawable(R.drawable.shape_dark_blue));
				  }else{
		            layout.setBackground(getResources().getDrawable(R.drawable.shape_light_blue));
				}
				//layout.setBackground(getResources().getDrawable(R.color.light_blue));
				layout.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
				TextView tv = new TextView(ProjectApplyActivity.this);
				tv.setTextColor(getResources().getColor(R.color.white));
				tv.setTextSize(15);
				tv.setText(proj_src_next_list.get(position).getName());
				if(position == selectedPosition)
					tv.setPadding(10, 10, 0, 10);
				else
					tv.setPadding(50, 10, 0, 10);
				
				layout.addView(tv);
				convertView = layout;
				return convertView;
			}
			
			
			private int selectedPosition = 0;

		    public void setSelectedPosition(int selectedPosition) {
		        this.selectedPosition = selectedPosition;
		    }

		    public int getSelectedPosition() {
		        return selectedPosition;
		    }
		    private class ViewHolder {
		        TextView nameTV;
		    }
 }

//刷新右侧ListView
    private void updateSecondListView(List<SecondClassItem> list2,
                                  ProjSourceNextAdapter secondAdapter) {
    	proj_src_next_list.clear();
    	proj_src_next_list.addAll(list2);
        secondAdapter.notifyDataSetChanged();
    }

    private void handleResult(int firstId, int secondId, String selectedName){
        String text = "first id:" + firstId + ",second id:" + secondId;
        Toast.makeText(ProjectApplyActivity.this,text,Toast.LENGTH_SHORT).show();
  
    }


}
