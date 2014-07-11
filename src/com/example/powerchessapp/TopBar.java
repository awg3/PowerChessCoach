package com.example.powerchessapp;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


//initalize the top navigation bar
public class TopBar{
	
	public TopBar(final Activity act, String curClass){		
		Log.d("here","here");
		LinearLayout ll;
		String bgcolor="#c0c0c0";
		
		ll= (LinearLayout)act.findViewById(R.id.navHome);
		ll.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent nextActivity = new Intent(act, MainActivity.class);
				act.startActivity(nextActivity);
			}
		});
		ll.setBackgroundColor(Color.parseColor(bgcolor));
		
		
		ll = (LinearLayout)act.findViewById(R.id.navContents);
		ll.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent nextActivity = new Intent(act, ContentsPage.class);
				act.startActivity(nextActivity);
			}
		});
		ll.setBackgroundColor(Color.parseColor(bgcolor));
		
		
		ll = (LinearLayout)act.findViewById(R.id.navAuthors);
		ll.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent nextActivity = new Intent(act, AuthorsPage.class);
				act.startActivity(nextActivity);
			}
		});
		ll.setBackgroundColor(Color.parseColor(bgcolor));
		
		ll = (LinearLayout)act.findViewById(R.id.navAbout);
		ll.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent nextActivity = new Intent(act, AboutPage.class);
				act.startActivity(nextActivity);
			}
		});
		ll.setBackgroundColor(Color.parseColor(bgcolor));
		
			
			
		String hexColor = "#080819";
		if(curClass.equals("home")){
			act.findViewById(R.id.navHome).setBackgroundColor(Color.parseColor(hexColor));
			((TextView)act.findViewById(R.id.navHomeText)).setTextColor(Color.WHITE);
		}else if(curClass.equals("about")){
			act.findViewById(R.id.navAbout).setBackgroundColor(Color.parseColor(hexColor));
			((TextView)act.findViewById(R.id.navAboutText)).setTextColor(Color.WHITE);
		}else if(curClass.equals("authors")){
			act.findViewById(R.id.navAuthors).setBackgroundColor(Color.parseColor(hexColor));
			((TextView)act.findViewById(R.id.navAuthorsText)).setTextColor(Color.WHITE);
		}else if(curClass.equals("contents")){
			act.findViewById(R.id.navContents).setBackgroundColor(Color.parseColor(hexColor));
			((TextView)act.findViewById(R.id.navContentsText)).setTextColor(Color.WHITE);
		}
		
		
		
	}
}
