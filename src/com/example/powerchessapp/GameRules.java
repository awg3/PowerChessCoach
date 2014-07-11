package com.example.powerchessapp;

import Rules.ChessGame;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

public class GameRules extends ChessGame{

	
	Context con;
	Activity act;
	int wrongCounter=0;
	
	
	
	public GameRules(String str,String moveList, String[][]cMessage, String[][] wMessage) {
		super(str,"0",'0',moveList, cMessage, wMessage);
		// TODO Auto-generated constructor stub
	}
	
	public GameRules(String str, String board, char turn, String moveList, String[][]cMessage, String[][] wMessage) {
		super(str,board,turn, moveList, cMessage, wMessage);
		// TODO Auto-generated constructor stub
	}
	
	public void setContextAndActivity(Context con, Activity act){
		this.con=con;
		this.act=act;
	}

	//handles promotions
	@Override
	public void promotion(int x, int y, char color) {
		// TODO Auto-generated method stub
		
		PlayingView pv = (PlayingView) con;
		pv.promotionAlert(x,y,color);
		
		
	}

	
	//handles checks
	@Override
	public void checking(char color) {
		// TODO Auto-generated method stub
		
		PlayingView pv = (PlayingView) con;
		String s;
		if(color =='b')
			s="Black";
		else
			s="White";
		TextView tv = (TextView)act.findViewById(R.id.turnIndicator);
		String text = tv.getText().toString();
		text=text+": Check";
		tv.setText(text);
		
		
	}

	
	//displays text in the text area
	@Override
	public void showMessage(String cMessage, String wMessage, boolean b) {
		// TODO Auto-generated method stub
		PlayingView pv = (PlayingView) con;
		
		if(b)
			wrongCounter=0;
		
		if(!b)
			wrongCounter++;
		if(wrongCounter>=3)
			pv.showFirstHint();
		
		if(wrongCounter>=6)
			pv.showSecondHint();
		
		
		
		pv.updateTextArea(b?cMessage:wMessage);

		
		
	}
	
	


}
