package com.example.powerchessapp;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

public class AboutPage extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_activity);
		
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.hide();
		
        @SuppressWarnings("unused")
		TopBar tb = new TopBar(this,"about");
	}


}
