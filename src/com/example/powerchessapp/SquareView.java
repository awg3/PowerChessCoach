package com.example.powerchessapp;


import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;


public class SquareView extends RelativeLayout {

	  public SquareView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    int size = getMeasuredWidth();
	    Log.d("Hi","Hi");
	    super.onMeasure(size, size);

	}
}



