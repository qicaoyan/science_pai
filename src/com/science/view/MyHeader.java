package com.science.view;

import java.net.ContentHandler;

import com.example.science.R;
import com.science.activity.MainActivity;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyHeader extends LinearLayout {

	public TextView tv=null;
	public LinearLayout headerContentView=null;
	public String [] headcontent=null;
	public Button[] headButtons=null;
	public View.OnClickListener[] onClickListeners=null;
	public Context headContent=null;
	public ImageButton home_btn;
	public MyHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.header, this);
        
//        imageView=(ImageView) findViewById(R.id.imageView1);
//        textView=(TextView)findViewById(R.id.textView1);
        headContent=context;
        InitView();
	}


	public MyHeader(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.header, this);
//        imageView=(ImageView) findViewById(R.id.imageView1);
//        textView=(TextView)findViewById(R.id.textView1);
        headContent=context;
       InitView();
	}

	public MyHeader(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.header, this);
//        imageView=(ImageView) findViewById(R.id.imageView1);
//        textView=(TextView)findViewById(R.id.textView1);
        headContent=context;
        InitView();
	}
	
	private void InitView()
	{
		
		tv=(TextView)this.findViewById(R.id.headertitle);
		headerContentView=(LinearLayout)this.findViewById(R.id.headcontent);
		home_btn = (ImageButton) this.findViewById(R.id.doc_home_btn);
		home_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				headContent.startActivity(new Intent(headContent,MainActivity.class));
				((Activity) headContent).finish();
			}
			
		});
	}
	
	public Boolean SetHeaderText(String str)
	{
		if (tv!=null) {
			tv.setText(str);
		}
		return true;
	}
	
	//调用这个函数表示header中需要设置按钮
	
	public Boolean SetHeaderButtons(String [] str)
	{
		headerContentView.setPadding(0, 0, 0, 40);
		
		if (str!=null&&str.length>1) {
			
			headButtons=new Button[str.length];
			
			onClickListeners=new OnClickListener[str.length];
			
			for(int i=0;i<str.length;i++) {
				headButtons[i]=new Button(headerContentView.getContext());
				LayoutParams lParams=new LayoutParams(0, 0, 1);
				//headButtons[i].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				headButtons[i].setTextColor(Color.WHITE);
				headButtons[i].setText(str[i]);
				headButtons[i].setTextSize(15);
				headButtons[i].setMinWidth(0);
				headButtons[i].setMinHeight(0);
				headButtons[i].setMinimumWidth(0);
				headButtons[i].setMinimumHeight(0);
                if(i == 0)
                	headButtons[i].setBackground(getResources().getDrawable(R.drawable.left_corner_btn_empty));
                else if(i == str.length - 1)
                	headButtons[i].setBackground(getResources().getDrawable(R.drawable.right_corner_btn_empty));
                else
                	headButtons[i].setBackground(getResources().getDrawable(R.drawable.rect_btn_empty));
                //headButtions[i].setBackground(background)
				//headButtions[i].setLayoutParams(lParams);
				headButtons[i].setId(i);
				headerContentView.addView(headButtons[i]);
				headButtons[i].setOnClickListener(onClickListener);
			} 
		}

		return true;
	}
	
	 
	
	
	
	public Boolean SetOnHeadButtonClickListener(View.OnClickListener onClickListener,int index)
	{
		if(onClickListener!=null)
		{
			onClickListeners[index] = onClickListener;
		}

		return true;
	}
	
	
	
	public Boolean SetSelected(int index)
	{
		for(int i=0;i<headButtons.length;i++)
		{
			if(i == 0)
			    headButtons[i].setBackground(getResources().getDrawable(R.drawable.left_corner_btn_empty));
			else if(i == headButtons.length - 1)
				headButtons[i].setBackground(getResources().getDrawable(R.drawable.right_corner_btn_empty));
			else
				headButtons[i].setBackground(getResources().getDrawable(R.drawable.rect_btn_empty));
		   
			headButtons[i].setTextColor(Color.WHITE);
		}
		headButtons[index].setBackgroundColor(getResources().getColor(R.color.white));
		
		headButtons[index].setTextColor(getResources().getColor(R.color.light_blue));
		
		if(index == 0)
			headButtons[index].setBackground(getResources().getDrawable(R.drawable.left_corner_btn_fill));
		else if(index == headButtons.length - 1)
			headButtons[index].setBackground(getResources().getDrawable(R.drawable.right_corner_btn_fill));
		else
			headButtons[index].setBackground(getResources().getDrawable(R.drawable.rect_btn_fill));
		
		return true;
	}
	private View.OnClickListener onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(headButtons!=null)
			{
				for(int i=0;i<headButtons.length;i++)
				{
					if(i == 0)
					    headButtons[i].setBackground(getResources().getDrawable(R.drawable.left_corner_btn_empty));
					else if(i == headButtons.length - 1)
						headButtons[i].setBackground(getResources().getDrawable(R.drawable.right_corner_btn_empty));
					else
						headButtons[i].setBackground(getResources().getDrawable(R.drawable.rect_btn_empty));
				   
					headButtons[i].setTextColor(Color.WHITE);
				}
			}
			
			headButtons[v.getId()].setTextColor(getResources().getColor(R.color.light_blue));
			
			if(v.getId() == 0)
				headButtons[v.getId()].setBackground(getResources().getDrawable(R.drawable.left_corner_btn_fill));
			else if(v.getId() == headButtons.length - 1)
				headButtons[v.getId()].setBackground(getResources().getDrawable(R.drawable.right_corner_btn_fill));
			else
				headButtons[v.getId()].setBackground(getResources().getDrawable(R.drawable.rect_btn_fill));
			
			
			if (onClickListeners!=null) {
				onClickListeners[v.getId()].onClick(v);
			}
		}
	};
	

}
