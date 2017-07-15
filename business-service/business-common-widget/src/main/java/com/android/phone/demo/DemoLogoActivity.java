package com.android.phone.demo;

import android.os.Bundle;

import com.android.phone.assistant.LogoActivity;

public class DemoLogoActivity extends LogoActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public String regMainActivityClassName() {
		// TODO Auto-generated method stub
		
		return "com.android.phone.demo.ConfigMainPageActivity";
	}
}
