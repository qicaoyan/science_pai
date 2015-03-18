package com.science.activity;

import com.example.science.R;
import com.example.science.R.layout;
import com.example.science.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CommentDetailActivity extends Activity {

	private ListView comment_list_view;
	private int comment_num;
	private CommentListAdapter comment_list_adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.comment_list);
        initVariable();
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.comment_detail, menu);
        return true;
    }
    
    public void initVariable()
    {
    	comment_num = 5;
    	comment_list_adapter = new CommentListAdapter();
    }
    
    public void initViews()
    {
    	comment_list_view = (ListView)findViewById(R.id.comment_list_view);
    	comment_list_view.setAdapter(comment_list_adapter);
    }
    
    
    private class CommentListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return comment_num;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v = getLayoutInflater().inflate(R.layout.comment_item, null);
            convertView = v;
			final TextView comment_detail_tv = (TextView) v.findViewById(R.id.comment_detail);
			final TextView comment_show_more = (TextView)v.findViewById(R.id.show_more);
			comment_show_more.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					comment_detail_tv.setMaxLines(comment_detail_tv.getMaxLines() + 2);
				}
				
			});
			return convertView;
		}
    	
    }
}
