package com.science.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;


import com.example.science.R;
import com.science.json.JsonLoginHandler;
import com.science.services.FunctionManage;
import com.science.services.MyApplication;
import com.science.util.Url;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Android_DialogActivity extends Activity {
	
		static final int MSG_SHOW_COMPLETE=4;
		static final int MSG_SHOW_ERROR=5;
		static final int MSG_SHOW_CANCEL=6;
	
		public ProgressDialog p_dialog;  
		public MyHandler myHandler=null;
		public String name=null;
		public String pass=null;
		public static String loginState=null;
		public static String sidString=null;
		public static String result=null;
		public static JsonLoginHandler jsonLoginHandler=new JsonLoginHandler(); 
		
		public EditText editTextUserName=null;
		public EditText editTextPassWord=null;
		
		public TextView tvRegister=null;
		public TextView tvForgetPassWord=null;
		
		public ImageButton btnLogin=null;
		public ImageButton btnQQ=null;
		public ImageButton btnWeiBo=null;
		public ImageButton btnWeiXin=null;
		public ImageButton btnRenRen=null;
		
		public MyApplication myApplication=null;
		public FunctionManage functionManage=null;
		
	    @Override  
	    public void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState); 
	        
	        myHandler=new MyHandler();
	        
	        myApplication=(MyApplication)this.getApplication();
	        functionManage=new FunctionManage(Android_DialogActivity.this);
	        
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        
	        ShareSDK.initSDK(this);
	        setContentView(R.layout.logindialog);
	        
	        InitView();
	        SetOnClickeListener();
	        
//	        LayoutInflater factory = LayoutInflater  
//                    .from(Android_DialogActivity.this);  
//            final View DialogView = factory.inflate(R.layout.logindialog, null);
//            editTextUserName=(EditText)DialogView.findViewById(R.id.AccountEditText);
//            editTextPassWord=(EditText)DialogView.findViewById(R.id.PasswordEidtText);
//            
//            AlertDialog dlg = new AlertDialog.Builder(  
//                    Android_DialogActivity.this)  
//                    .setTitle("登陆框")  
//                    .setView(DialogView)  
//                    .setPositiveButton("确定",  
//                            new DialogInterface.OnClickListener() {  
//
//                                @Override  
//                                public void onClick(  
//                                        DialogInterface dialog,  
//                                        int which) {  
//                                    // TODO Auto-generated method  
//                                    // stub  
//                                    p_dialog = ProgressDialog  
//                                            .show(Android_DialogActivity.this,  
//                                                    "请等待",  
//                                                    "正在为您登录...",  
//                                                    true);
//                                    
//                                    userName=editTextUserName.getText().toString().trim();
//                                    userPass=editTextPassWord.getText().toString().trim();
//                                    
//                            		MyThread mThread=new MyThread();
//                            		new Thread(mThread).start();
//                                }  
//                            })  
//                    .setNegativeButton("取消",  
//                            new DialogInterface.OnClickListener() {  
//
//                                @Override  
//                                public void onClick(  
//                                        DialogInterface dialog,  
//                                        int which) {  
//                                    // TODO Auto-generated method  
//                                    // stub  
//                                    Android_DialogActivity.this  
//                                            .finish();  
//                                }  
//                            }).create();  
//            dlg.show();  
	    }  
		@Override
		protected void onStop() {
			// TODO Auto-generated method stub
			ShareSDK.stopSDK(this);
			super.onStop();
		}
	    public void InitView()
	    {
	          editTextUserName=(EditText)findViewById(R.id.AccountEditText);
	          editTextPassWord=(EditText)findViewById(R.id.PasswordEidtText);
	          
	          tvRegister=(TextView)findViewById(R.id.logindialogregister);
	          tvForgetPassWord=(TextView)findViewById(R.id.logindialogfogetpassword);
	          
	          btnLogin=(ImageButton)findViewById(R.id.logindialoglogin);
	          btnQQ=(ImageButton)findViewById(R.id.logindialogqqlogin);
	          btnWeiBo=(ImageButton)findViewById(R.id.logindialogweibologin);
	          btnWeiXin=(ImageButton)findViewById(R.id.logindialogweixinlogin);
	          btnRenRen=(ImageButton)findViewById(R.id.logindialogrenrenlogin);
	    }
	    
	    public void SetOnClickeListener()
	    {
	    	tvForgetPassWord.setOnClickListener(onClickListener);
	    	tvRegister.setOnClickListener(onClickListener);
	    	btnLogin.setOnClickListener(onClickListener);
	    	btnQQ.setOnClickListener(onClickListener);
	    	btnWeiBo.setOnClickListener(onClickListener);
	    	btnWeiXin.setOnClickListener(onClickListener);
	    	btnRenRen.setOnClickListener(onClickListener);
	    }
	    
	    private PlatformActionListener platformActionListener=new PlatformActionListener() {
			
	    	//授权失败
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				System.out.println("授权失败");
				Message msg=new Message();
				msg.what=MSG_SHOW_ERROR;
				myHandler.sendMessage(msg);
			}

			//授权成功
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
				arg0.removeAccount();
				System.out.println("授权成功");
				Message msg=new Message();
				msg.what=MSG_SHOW_COMPLETE;
				
				String id=arg2.get("id").toString();
				String name=arg2.get("name").toString();
	  			String description=arg2.get("description").toString();
	  			String profile_image_url=arg2.get("profile_image_url").toString();
	  			String str="ID: "+id+";\n"+
	  						"用户名： "+name+";\n"+
	  						"描述："+description+";\n"+
	  						"用户头像地址："+profile_image_url;
	  			System.out.println("用户资料: "+str);
	  			Bundle data=new Bundle();//----map
				data.putString("information", str);
				msg.setData(data);
				//发送消息
				myHandler.sendMessage(msg);
			}

			//授权取消
			public void onCancel(Platform arg0, int arg1) {
				System.out.println("取消授权");
				Message msg=new Message();
				msg.what=MSG_SHOW_CANCEL;
				myHandler.sendMessage(msg);
			}
		};
	    
	    private View.OnClickListener onClickListener=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.logindialogregister:
					//ShareContent();
					functionManage.showShare();
					break;
				case R.id.logindialogfogetpassword:
					
					break;
				case R.id.logindialoglogin:
					MyThread myThread=new MyThread();
					new Thread(myThread).start();
                  p_dialog = ProgressDialog  
                  .show(Android_DialogActivity.this,  
                          "请等待",  
                          "正在为您登录...",  
                          true);
					break;
				case R.id.logindialogqqlogin:
					Platform qq=ShareSDK.getPlatform(QQ.NAME);
					//禁用SSO授权（禁用客户端授权）
					qq.SSOSetting(true);
					qq.setPlatformActionListener(platformActionListener);
					//移除授权，以达到每次都可以打开授权登录页面
					qq.removeAccount();
					//登录
					qq.showUser(null);
					break;
				case R.id.logindialogweibologin:
					Platform weibo=ShareSDK.getPlatform(SinaWeibo.NAME);
					//禁用SSO授权（禁用客户端授权）
					weibo.SSOSetting(true);
					weibo.setPlatformActionListener(platformActionListener);
					//移除授权，以达到每次都可以打开授权登录页面
					weibo.removeAccount();
					//登录
					weibo.showUser(null);
					break;
				case R.id.logindialogweixinlogin:
					Platform weixin=ShareSDK.getPlatform(Wechat.NAME);
					//禁用SSO授权（禁用客户端授权）
					weixin.SSOSetting(true);
					weixin.setPlatformActionListener(platformActionListener);
					//移除授权，以达到每次都可以打开授权登录页面
					weixin.removeAccount();
					//登录
					weixin.showUser(null);
					break;
				case R.id.logindialogrenrenlogin:
					Platform renren=ShareSDK.getPlatform(Renren.NAME);
					//禁用SSO授权（禁用客户端授权）
					renren.SSOSetting(true);
					renren.setPlatformActionListener(platformActionListener);
					//移除授权，以达到每次都可以打开授权登录页面
					renren.removeAccount();
					//登录
					renren.showUser(null);
					break;
				default:
					break;
				}
			}
		};
	    
	    private class MyHandler extends Handler
	    {

			@Override
			public void dispatchMessage(Message msg) {
				// TODO Auto-generated method stub
				super.dispatchMessage(msg);
			}


			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what==1) {
					Intent data=new Intent();  
		            data.putExtra("result", "success");  
		            //请求代码可以自己设置，这里设置成20  
		            setResult(200, data);
		            //保存用户名和密码
		            if(name!=null&&pass!=null)
		            {
		            	functionManage.SaveLoginInfo(name,pass);
		            }
		            //关闭掉这个Activity
		            Toast.makeText(Android_DialogActivity.this, "success", Toast.LENGTH_LONG).show();
		            finish();  
				}
				else if (msg.what==2) {
					Intent data=new Intent();  
		            data.putExtra("result", "faile");  
		            //请求代码可以自己设置，这里设置成20  
		            setResult(400, data);  
		            //关闭掉这个Activity  
		            //finish();
		            Toast.makeText(Android_DialogActivity.this, "fail", Toast.LENGTH_LONG).show();
				}
				else if(msg.what== MSG_SHOW_ERROR) {
				Toast.makeText(Android_DialogActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
				}
			
				else if(msg.what==MSG_SHOW_COMPLETE){
					Intent data=new Intent();  
		            data.putExtra("result", "success");  
		            //请求代码可以自己设置，这里设置成20  
		            setResult(200, data);  
		            //关闭掉这个Activity
		            Toast.makeText(Android_DialogActivity.this, "success", Toast.LENGTH_LONG).show();
		              
					Bundle userData=msg.getData();
					String informa=userData.getString("information");
					finish();
				}
				else if(msg.what== MSG_SHOW_CANCEL){
				Toast.makeText(Android_DialogActivity.this, "取消授权", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
				// TODO Auto-generated method stub
				return super.sendMessageAtTime(msg, uptimeMillis);
			}
	    	
	    }
	    
	    public class MyThread implements Runnable
	    {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try { 
					Log.v("Login", "start");
					name=editTextUserName.getText().toString();
					pass=editTextPassWord.getText().toString();
                    String result=myApplication.Login(name,pass);
                    Log.v("Login", result);
                    Log.v("Login", "faile");
                    if (result==null) {
						myHandler.sendEmptyMessage(2);
					}
                    else{
						myHandler.sendEmptyMessage(1);
					}
                } catch (Exception e) {  
                    e.printStackTrace();  
                } finally {  
                	if(p_dialog!=null)
                	{
                		p_dialog.dismiss();
                	}
                    //  
                }  
            }  
			
	    	
	    }

	    public  String Login()
		{
			if (name==null||pass==null) {
				return null;
			}
			else {
				String strUrlString=Url.LOGINURL;
				strUrlString+="name=";
				strUrlString+=name;
				strUrlString+="&";
				strUrlString+="pass=";
				strUrlString+=pass;

				try {
					URL url = new URL(strUrlString);
//					Log.i("NewsListUrl", DataUrlKeys.NEWS_LIST_URL + pass);
					URLConnection con;
					con = url.openConnection();
					con.connect();
					InputStream input = con.getInputStream();
					sidString=jsonLoginHandler.getListItems(input);
					myApplication.sidString=sidString;
					return sidString;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally
				{
					return sidString;
				}

			}
		}
	    
	    public void ShareContent()
	    {
	    	ShareSDK.initSDK(this);
	    	 OnekeyShare oks = new OnekeyShare();
	    	 //关闭sso授权
	    	 oks.disableSSOWhenAuthorize(); 
	    	 
	    	// 分享时Notification的图标和文字
	    	 oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
	    	 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
	    	 oks.setTitle(getString(R.string.share));
	    	 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
	    	 oks.setTitleUrl("http://sharesdk.cn");
	    	 // text是分享文本，所有平台都需要这个字段
	    	 oks.setText("我是分享文本");
	    	 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
	    	 oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
	    	 // url仅在微信（包括好友和朋友圈）中使用
	    	 oks.setUrl("http://sharesdk.cn");
	    	 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
	    	 oks.setComment("我是测试评论文本");
	    	 // site是分享此内容的网站名称，仅在QQ空间使用
	    	 oks.setSite(getString(R.string.app_name));
	    	 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
	    	 oks.setSiteUrl("http://sharesdk.cn");
	    	 
	    	// 启动分享GUI
	    	 oks.show(this);
	    }
}