package com.niunet.assistivetouch;


import com.android.assistivetouch.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebActivity extends Activity{
	private ProgressBar mProgressBar;
	private WebView mWebView;
	private String mUrl = null;
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mUrl = getIntent().getStringExtra("url");
		
		setContentView(R.layout.web_view_layout);  
		
		mProgressBar = (ProgressBar) this.findViewById(R.id.web_loading_progress);
		mWebView = (WebView) this.findViewById(R.id.web_view);
		
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl(mUrl);
		
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {//自己处理子链接的显示，不然会启动系统浏览器进行加载
				// TODO Auto-generated method stub
				view.loadUrl(url);
				
				return super.shouldOverrideUrlLoading(view, url);
			}
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				
				mProgressBar.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				
				mProgressBar.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()){
			mWebView.goBack();
			
			return true;
		}else{
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
}
