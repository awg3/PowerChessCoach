package com.example.powerchessapp;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


@SuppressLint("NewApi")
public class AuthorsPage extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authors);
		
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.hide();
		
        @SuppressWarnings("unused")
		TopBar tb = new TopBar(this,"authors");
		
		/*ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.hide();
		
		Authors[] authors = initAuthors();
		makeUI(authors);*/
	}
	
}
