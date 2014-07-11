package com.example.powerchessapp;



import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.hide();
		
        @SuppressWarnings("unused")
		TopBar tb = new TopBar(this,"home");
		
		
		makeNameView();
		
	}

	
	
	
	//inits the mian page
	public void makeNameView(){
		
		TextView tv = new TextView(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,1);
		
		
		tv.setText("Power\nChess\nCoach");
		tv.setTextSize(60);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);
		tv.setPadding(0, 40, 0, 0);
		tv.setTextColor(Color.WHITE);
		tv.setId(1002);
		LinearLayout ll = (LinearLayout)findViewById(R.id.TitlePageView);
		ll.addView(tv,params);
		
		
		ImageView img = new ImageView(this);
		img.setBackgroundResource(R.drawable.chess_board);
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,1);
		
		ll.addView(img,params);
		
	}
	

	
}


