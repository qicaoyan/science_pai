package com.science.activity;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import com.example.science.R;
import com.science.adapter.DocumentListAdapter;
import com.science.adapter.DocumentTagAdapter;
import com.science.interfaces.OnLoadingStateChangedListener;
import com.science.json.JsonDcumentListHandler;
import com.science.services.MyApplication;
import com.science.util.Url;
import com.science.view.MyHeader;
import com.science.view.MyImageButton;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Message;
public class DocumentExpressActivity extends Activity implements OnLoadingStateChangedListener{
	private Context context;
	private MyHeader header_view;
	private MyImageButton chinese_doc_btn;
	private MyImageButton english_doc_btn;
	private MyImageButton working_doc_btn;
	private MyImageButton nsf_doc_btn;
	private TableLayout tag_tab_layout;
	private RelativeLayout other_tag_layout;
	private ListView  doc_list_view;
	
	private MyImageButton fold_btn;
	private MyImageButton[] doc_btns;
	private WindowManager wm;
	private DocListAdapter doc_list_adapter;
	private View doc_more_view;
	private TextView other_tag_tv;
	private int rows;
	private int num_cols;//表示一行有几个
	private boolean  hide;//表示折叠按钮是否处于折叠状态
	private int tag_tv_width;
	private int tag_tv_height;
	private int[] tags_state;//记录8个标签的状态
	private String other_tag_str;
	/*与数据解析有关*/
	private JsonDcumentListHandler json = null;
	private String strUrl = null;
	private List<Map<String,String>> doc_list;
	
	
	
    private static final int UPDATE_TAG_VIEW = 0x00; 
    private static final int UPDATE_DOC_LIST = 0x01;
    private static final int UPDATE_DOC_LIST_FAIL = 0x02;
    private MyApplication application;
    private static final int DEFAULT_ID = 111;
    private static final int DEFAULT_SOURCE = 0;
    private static final StringBuffer DEFAULT_KEYWORDS = new StringBuffer("城市化");
    public static  final boolean HIDE = false;
    public static final boolean SHOW = true;
    private StringBuffer current_keyword;
    private int all_keywords_num;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.document_express);
		initVariable();
		initViews();
	    requestData();//刚启动的时候刷新一次数据
		setOnClickListener();
	}
	
	
	public void initVariable() 
	{
		rows = 1;
		num_cols = 4;
		hide = true;
		tag_tv_width = 0;
		tag_tv_height = 0;
		context = this;
		json = new JsonDcumentListHandler();

		application = (MyApplication) this.getApplication();
		doc_list = null;
		tags_state = new int[8];
		for(int i = 0;i < 8;i++)
		{
			tags_state[i] = 0;
		}
		other_tag_str = "";
		//设置doc_list_adapter的值
		doc_list_adapter = new DocListAdapter();
		strUrl = Url.DocumentLIST;
		strUrl = application.ComposeToken(strUrl);
		try {
			strUrl += "&Id="  + DEFAULT_ID + "&" + "source=" + DEFAULT_SOURCE + 
					"&keyWords=" + URLEncoder.encode(DEFAULT_KEYWORDS.toString(),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		all_keywords_num = application.non_null_keywords_list.size();
	    rows = (all_keywords_num + 3)/4;
	}
	
	private void requestData()
	{
		
        MyThread thread = new MyThread(0);
        new Thread(thread).start(); 
	}
	
	
	public void initViews()
	{
		wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		int screen_width = wm.getDefaultDisplay().getWidth();
		int screen_height = wm.getDefaultDisplay().getHeight();
		
		//获取文献速递页面的header并设置
		header_view = (MyHeader) findViewById(R.id.doc_express_common_header_layout);
		header_view.SetHeaderText("文献速递");
		String[] header_button_strs = {"中文文献","英文文献","工作文献","NSF"};
		header_view.SetHeaderButtons(header_button_strs);

		

		doc_list_view = (ListView)findViewById(R.id.doc_list_view);
		doc_list_view.setCacheColorHint(Color.argb(255, 0, 0, 0));
		doc_list_view.setSelector(R.drawable.list_item_selector);
		doc_list_view.setOnItemClickListener(doc_item_click_listener);
		doc_list_view.setAdapter(doc_list_adapter);
		//其他标签
		other_tag_tv = (TextView)findViewById(R.id.tag_input);
		other_tag_tv.setText(other_tag_str);
		other_tag_tv.setOnClickListener(other_tag_click_listener);
		
		/*加载更多按钮*/
		doc_more_view = (LinearLayout) getLayoutInflater().inflate(R.layout.listmoredata, null);

		
		fold_btn = (MyImageButton)findViewById(R.id.fold_btn);
		tag_tab_layout = (TableLayout)findViewById(R.id.tag_tab_layout);
		other_tag_layout = (RelativeLayout)findViewById(R.id.other_tag_layout);

		setTagLayout(true);

		

	
	}
	
	
	
	private void setTagLayout(boolean hide)
	{
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		fold_btn.measure(w, h);
		if(all_keywords_num != 0)
		tag_tv_width = (getWindowManager().getDefaultDisplay().getWidth() - fold_btn.getMeasuredWidth())/(all_keywords_num < 4?all_keywords_num:4);
		else
		tag_tv_width = 0;
		
		if(hide)
		tag_tv_height =  fold_btn.getMeasuredHeight();
		else
		tag_tv_height = fold_btn.getMeasuredHeight()/2;
		
		int temp_rows = rows;
		
		if(hide)
			temp_rows = 1;
		else
			temp_rows = rows;
		
		for(int j = 0;j < temp_rows;j++)
		{
			TableRow tr = new TableRow(this);
		  for(int i = 0;i < num_cols;i++)
		  {
			  if(j*num_cols + i >= all_keywords_num)
				  break;
			TextView tv = new TextView(this);
			tv.setBackground(getResources().getDrawable(R.drawable.shape_light_blue));
			tv.setText(application.non_null_keywords_list.get(j*num_cols + i));
			tv.setGravity(Gravity.CENTER);
			tv.setTextSize(15.0f);
			tv.setTextColor(Color.WHITE);
			tv.setMinimumWidth(tag_tv_width);
			tv.setMinimumHeight(tag_tv_height);
			int tag_id = j*num_cols + i;
			tv.setTag(tag_id);
			tv.setOnClickListener(tag_click_listener);
			tr.addView(tv);
		  }
		  
		  tag_tab_layout.addView(tr);
		}
	}
	
	

	
	public void setOnClickListener()
	{


		//设置标题上的三个按钮的监听事件
        OnClickListener doc_type_listener = new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					if(doc_list != null)
					doc_list.clear();
					doc_list_adapter.notifyDataSetChanged();
					strUrl = Url.DocumentLIST;
					strUrl = application.ComposeToken(strUrl);
					strUrl += "&Id=111&" + "source=" + v.getId() + "&keyWords=" + URLEncoder.encode("城市化","utf-8");
				    requestData();
				    DocumentExpressActivity.this.onLoadingBegin();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}    
			}
        	
        };
		
        header_view.SetOnHeadButtonClickListener(doc_type_listener, 0);
        header_view.SetOnHeadButtonClickListener(doc_type_listener, 1);
        header_view.SetOnHeadButtonClickListener(doc_type_listener, 2);
        header_view.SetOnHeadButtonClickListener(doc_type_listener, 3);
        header_view.SetSelected(0);
        
        //设置fold按钮的监听事件
        fold_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hide = !hide;
				if(hide)
					v.setBackground(getResources().getDrawable(R.drawable.fold));
				else
				    v.setBackground(getResources().getDrawable(R.drawable.unfold));
				handler.sendEmptyMessage(DocumentExpressActivity.UPDATE_TAG_VIEW);
			}
        	
        });
	}
	
	
	


	
	
	
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			 
			//更新TAG_VIEW
			case DocumentExpressActivity.UPDATE_TAG_VIEW:
				if(hide)
				{
					rows = 1;
					num_cols = 4;
					setTagLayout(false);
				}
				else
				{
					rows = 2;
					num_cols = 4;
				}
				//在更新之前先把所有的tag_tab_layout子View删掉
			
				tag_tab_layout.removeAllViews();
				
				
				
				if(hide)
					other_tag_layout.setVisibility(View.GONE);
				else
					other_tag_layout.setVisibility(View.VISIBLE);
				
				break;
			
			    
				
			case DocumentExpressActivity.UPDATE_DOC_LIST:
				DocumentExpressActivity.this.onLoadingSucceed();
				//findViewById(R.id.doc_loading_mask_layout).setVisibility(View.GONE);
				doc_list_adapter.notifyDataSetChanged();
				doc_list_view.setAdapter(doc_list_adapter);
				//doc_list_view.addFooterView(doc_more_view);
				break;
			case DocumentExpressActivity.UPDATE_DOC_LIST_FAIL:
				DocumentExpressActivity.this.onLoadingFail();
				break;
				
			}
		}
	};

	
	
	
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
				
				
				url = new URL(strUrl);
				URLConnection con = url.openConnection();
				con.connect();
				InputStream input = con.getInputStream();
				List<Map<String,String>> temp = null;
				temp = json.getListItems(input);
				if(temp != null)
				{
					
					doc_list = temp;
					handler.sendEmptyMessage(DocumentExpressActivity.UPDATE_DOC_LIST);
				}else
				{
					handler.sendEmptyMessage(DocumentExpressActivity.UPDATE_DOC_LIST_FAIL);
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
	
	
	
	
	private class DocListAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(doc_list != null)
			return doc_list.size();
			else
			return 0;
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
			View view = inflater.inflate(R.layout.doc_list_item, null);
			TextView doc_title_text = (TextView) view.findViewById(R.id.doc_title);
			String title = doc_list.get(position).get("title");
			doc_title_text.setText(title);
			if(convertView == null)
			{
				convertView = view;
			}
			return convertView;
		}
		
	}
	
	
	
	//为ListView每一个item设置OnClick事件
	private OnItemClickListener doc_item_click_listener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			// TODO Auto-generated method stub
			String url = application.ComposeToken(Url.DocumentDetailBase) + "&id=" + doc_list.get(position).get("id");
			Log.i("doc_item_url", url);
			Intent intent = new Intent();//用以传递数据
			intent.setClass(DocumentExpressActivity.this, CommonContentActivity.class);
			intent.putExtra("url", application.ComposeToken(url));
			intent.putExtra("act_class", "document");
			startActivity(intent);
		}
		
	};
	
	
	
	private OnClickListener tag_click_listener = new OnClickListener()
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int tag_id = (Integer) v.getTag();
			if(tags_state[tag_id] == 0)//表明还未选中
			{
				v.setBackground(getResources().getDrawable(R.color.dark_blue));
				tags_state[tag_id] = 1;
			}
			else
			{
				v.setBackground(getResources().getDrawable(R.drawable.shape_light_blue));
				tags_state[tag_id] = 0;				
			}
		}
		
	};
	
	
	private EditText other_input_ed ;
	
	private OnClickListener other_tag_click_listener = new OnClickListener()
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			other_input_ed = new EditText(DocumentExpressActivity.this);
			other_input_ed.setText(other_tag_str);
			new AlertDialog.Builder(DocumentExpressActivity.this)
			.setTitle("请输入其他标签")
			.setIcon(android.R.drawable.ic_dialog_info)
			.setView(other_input_ed)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					other_tag_str = other_input_ed.getText().toString();
					other_tag_tv.setText(other_tag_str);
				}
			})
			.setNegativeButton("取消",null)
			.show();


			
			
			
			
			
			
		}
		
	};
	
	
	
	
	//设置加载事件
	@Override
	public void onLoadingSucceed() {
		// TODO Auto-generated method stub
		findViewById(R.id.doc_loading_mask_layout).setVisibility(View.GONE);
		Toast.makeText(DocumentExpressActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onLoadingFail() {
		// TODO Auto-generated method stub
		findViewById(R.id.doc_loading_mask_layout).setVisibility(View.GONE);
		Toast.makeText(DocumentExpressActivity.this, "加载失败！", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onLoadingBegin() {
		// TODO Auto-generated method stub
		findViewById(R.id.doc_loading_mask_layout).setVisibility(View.VISIBLE);
	}


	@Override
	public void setOnLoadingStateChangedListener(OnLoadingStateChangedListener l) {
		// TODO Auto-generated method stub
		l = this;
	}
	
	
	
	
	
	@Override
	public void onResume()
	{
        super.onResume();

	}
	
	
}
