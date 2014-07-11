
package com.example.powerchessapp;


//check mate does not work, implement if time



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


@SuppressLint({ "NewApi", "DefaultLocale" })
public class PlayingView extends Activity{
	
	
	GameRules game;
	
	
	
	
	int x=-1;
	int y=-1;
	ImageButton[][] buttonArr = new ImageButton[8][8];
	String Chapter;
	String Section;
	boolean Custom =false;
	Timer timer;
	
	
	
	
	//calls all the inits
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playing_view);

        @SuppressWarnings("unused")
		TopBar tb = new TopBar(this,"");
				
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.hide();
		
		createChessBoard();
		
		initUndoButton();
		
		
		
        Intent intent = getIntent();
        if(intent.getStringExtra("Chapter")!=null){
        	
        	Chapter = intent.getStringExtra("Chapter").toString();
        	Section = intent.getStringExtra("Section").toString();
        	customizedGame();
        	
        }else{
			//initChessBoard();
        	//game = new GameRules(GameRules.REGULAR_GAME);
            //game.setContextAndActivity(this,this.getActivity());
            
			//initChessBoardTest();
			
			//timer (doAsynchronousTask, 0, 60000*Integer.parseInt(SP.getString("refreshInterval", "3600000")));

			
        }
        
        
		
	}
	
	//handles revisits to the page
	@Override
	public void onResume(){
		super.onResume();
		
		initTimer();
	}
	
	
	//timer for the clock
	public void initTimer(){
		
		Log.d("timer","timer init");
		final Handler handler = new Handler();
    	//timer = new Timer();
	    TimerTask timerTask = new TimerTask() {       
	        @Override
	        public void run() {
	            handler.post(new Runnable() {
	                @Override
					public void run() { 
	                	updateTimeView();
	                	//Log.d("timer","timer");
	                }
	            });
	        }
	    };   
	    
	    timer = new Timer();
	    timer.schedule(timerTask, 0,1000);
	    
	}
	
	//adds tet to the text area
	public void updateTimeView(){
		TextView tv = (TextView)this.findViewById(R.id.timer);
		String text = tv.getText().toString();
		
		String hours = text.substring(0, 2);
		String minutes = text.substring(3, 5);
		String seconds = text.substring(6);
		
		
		int hoursInt = Integer.parseInt(hours);
		int minutesInt = Integer.parseInt(minutes);
		int secondsInt = Integer.parseInt(seconds);
		
		secondsInt++;
		
		if(secondsInt>=60){
			minutesInt++;
			secondsInt=0;
			if(minutesInt>=60){
				hoursInt++;
				minutesInt=0;
			}
		}
		
		hours = String.valueOf(hoursInt);
		minutes = String.valueOf(minutesInt);
		seconds = String.valueOf(secondsInt);
		
		if(hours.length()<2)
			hours="0"+hours;
		
		if(minutes.length()<2)
			minutes="0"+minutes;
		
		if(seconds.length()<2)
			seconds="0"+seconds;
		
		tv.setText(hours+":"+minutes+":"+seconds);
		
	}

	
	
	
	
	public void customizedGame(){
		
		new getStrings().execute();
		
	}
	

	//called to reinitalize the board
	public void initChessBoardTest(){
		
		String chessBoard = game.getChessBoard();
		
		setBoard(chessBoard);
		
		updateTurnLabel();
		
	}
	
	//updates the text for the turn label
	public void updateTurnLabel(){
		TextView tv = (TextView)findViewById(R.id.turnIndicator);
		String s="";
		if(game.inCheck())
			s=": Check";
		if(game.whoseTurn()=='b')
			tv.setText("Blacks turn"+s);
		else
			tv.setText("Whites turn"+s);
	}
	
	
	
	public void setBoard(String passedVal){

		String[][] stringArr = parseString(passedVal);
		
		setUpBoardFromString(stringArr);
				
						
	
	}
	
	//inits the board based off the string from the db
	public void setUpBoardFromString(String[][] arr){
		
		
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				
				String s = arr[i][j];
				//Log.d("String",""+ s+i+""+j);
				if(s.equals("00")){
					buttonArr[i][j].setImageResource(0);
					buttonArr[i][j].setTag("");
					continue;
				}
				buttonArr[i][j].setTag(s);
				int resourceId = getResources().getIdentifier (s, "drawable", "com.example.powerchessapp");

				buttonArr[i][j].setImageResource(resourceId);
				
			}
		}
	}
	
	//parses the string and sets the array for the board
	public String[][] parseString(String passedVal){
		
		String[][] stringArr = new String[8][8];

		
		
		
		
		for(int i=0; i<8; i++)
			for(int j=0; j<8; j++)
				stringArr[i][j]=String.valueOf(passedVal.substring((i*8+j)*2, (i*8+j+1)*2));

		return stringArr;
	}
	
	
		
		
		
		

		
		//displays the board with the pieces
		public void createChessBoard(){
			BoxRelativeLayout sv;
			final RelativeLayout mainLayout=(RelativeLayout)findViewById(R.id.chessBoard);
			final int c[] = {Color.parseColor("#E9E3D5"),Color.parseColor("#9D6F4B")};
			
	        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,1f);
	        //params.setMargins(1,1,1,1);
			LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
	        for(int i=0;i<8;i++){
	        	LinearLayout row = new LinearLayout(this);
	        	row.setTag("Row"+i);
	        	row.setId(300+i);
	        	row.setBackgroundColor(Color.GRAY);
	        	row.setBackgroundColor(Color.rgb((int)(i/7*255),(int)(i/7*255) , (int)(i/7*255)));
	        	
	        	RelativeLayout.LayoutParams rowParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
	        	if(i!=0)
	        		rowParams.addRule(RelativeLayout.BELOW,mainLayout.findViewWithTag("Row"+(i-1)).getId());
	        	row.setLayoutParams(rowParams);
				for(int j=0;j<8;j++){
					
					//BUTTON IDs: 0
					//VIEW IDs: 100
					//ROW IDs: 300
					
					//EVERYTHING IS 1-8
					
					
					
					sv=new BoxRelativeLayout(this);
					sv.setLayoutParams(params);
					sv.setBackgroundColor(c[(j+i)%2]);
		        	//sv.setBackgroundColor(Color.rgb((int)(i/7.0*255),(int)(i/7.0*255) , (int)(i/7.0*255)));

					sv.setPadding(2,2,2,2);
					sv.setTag("View"+(j+1)+""+(i+1));
					sv.setId(Integer.parseInt((1+""+(j+1)+""+(i+1))));


					ImageButton button = new ImageButton(this);
					buttonArr[i][j]=button;
					button.setLayoutParams(buttonParams);
					button.setId(Integer.parseInt((j+1)+""+(i+1)));
					//button.setTag("Box"+(j+1)+""+(i+1));
					button.setBackgroundColor(c[(j+i)%2]);
					
					button.setOnClickListener(new OnClickListener(){
						
						@SuppressLint("NewApi")
						@Override
						public void onClick(View v){
							BoxRelativeLayout par = (BoxRelativeLayout)v.getParent();

							int currX = v.getId()%10;
							int currY = (v.getId()-currX)/10;

							if(x==-1 && y==-1){
								//par.setBackgroundColor(((ColorDrawable)v.getBackground()).getColor());
								
								if(!par.getTag().equals("")){
									par.setBackgroundColor(Color.BLUE);
									x=currX;
									y=currY;
									ArrayList<int[]> moves = game.legalPosition(currX-1, currY-1);
									for(int[] arr:moves){
										int futureX=arr[0];
										int futureY=arr[1];
										
										BoxRelativeLayout newPar = (BoxRelativeLayout)(buttonArr[futureX][futureY].getParent());
										
										RelativeLayout greenShade = new RelativeLayout(PlayingView.this);
										RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
										params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
										
										greenShade.setBackgroundColor(Color.argb(50, 0, 255, 0));
										
										greenShade.setTag("greenShade");
										
										greenShade.setLayoutParams(params);
										
										newPar.addView(greenShade);

										
									}
										
								}
							}else{
								
								
								BoxRelativeLayout lastView = (BoxRelativeLayout)mainLayout.findViewWithTag("View"+(y)+""+(x));
								
								ArrayList<int[]> moves = game.legalPosition(x-1, y-1);
								
								for(int[] arr:moves){
									
									int futureX=arr[0];
									int futureY=arr[1];
									
									BoxRelativeLayout newPar = (BoxRelativeLayout)(buttonArr[futureX][futureY].getParent());
									
									newPar.removeView(newPar.findViewWithTag("greenShade"));
									
								}

								//.d("Tag",lastView.getTag()+"");

								lastView.setBackgroundColor(((ColorDrawable)lastView.getChildAt(0).getBackground()).getColor());
								
								int[] loc = game.nextMove();

								if(game.move(x-1, y-1, currX-1, currY-1)){
									
									if(loc!=null){
										//int hintX = loc[0];
										//int hintY = loc[1];
									
										//BoxRelativeLayout newPar = (BoxRelativeLayout)(buttonArr[hintX][hintY].getParent());
									
										//newPar.removeView(newPar.findViewWithTag("redShdae"));
										
										int hintX = loc[0];
										int hintY = loc[1];
									
										//BoxRelativeLayout newPar = (BoxRelativeLayout)(buttonArr[hintX][hintY].getParent());
									
										par.setBackgroundColor(((ColorDrawable)par.getChildAt(0).getBackground()).getColor());
										
										//newPar.removeView(newPar.findViewWithTag("redShdae"));
									}
									
									
									
									
									Log.d("move","pass");
									
									ImageButton previousImgBtn = (ImageButton)lastView.getChildAt(0);
									
									Log.d("Piece", ""+previousImgBtn.getX() + ", " + previousImgBtn.getY());
									
									String imgRes = previousImgBtn.getTag().toString();
									int resourceId = getResources().getIdentifier (imgRes, "drawable", "com.example.powerchessapp");
									
									previousImgBtn.setImageResource(0);
									
									
									
									
									ImageView img = new ImageView(PlayingView.this);
									img.setTag("slidingPic1");
									
									TranslateAnimation slide = setAnimation( previousImgBtn, (ImageButton) v , img);
									
									slide.setDuration(1500);
									
									slide.setAnimationListener(new Animation.AnimationListener() {            
								        @Override
								        public void onAnimationEnd(Animation animation) {
								        	//mainLayout.removeView(mainLayout.findViewWithTag("slidingPic1"));
								        	computerAnimation();
								        	//initChessBoardTest();
								        }

										@Override
										public void onAnimationRepeat(
												Animation animation) {
											// TODO Auto-generated method stub
											
										}

										@Override
										public void onAnimationStart(
												Animation animation) {
											// TODO Auto-generated method stub
											
										}
								    });
									slide.setFillAfter(true);
									img.setAnimation(slide);
					
									
								}else
								{
									if(game.wrongCounter>=3)
										showFirstHint();
									if(game.wrongCounter>=6)
										showSecondHint();
								}
								x=-1;
								y=-1;
								
								
							}
								
						}
						
					});
					
					sv.addView(button);
					row.addView(sv);
				}
				mainLayout.addView(row);
			}
		}
		
		
		//handles displaying the first hint
		public void showFirstHint(){
			int[] loc = game.nextMove();
			int x = loc[0];
			int y = loc[1];
			
			BoxRelativeLayout curr = (BoxRelativeLayout)(buttonArr[x][y].getParent());
			
			curr.setBackgroundColor(Color.RED);
			
			/*
			RelativeLayout greenShade = new RelativeLayout(PlayingView.this);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			
			greenShade.setBackgroundColor(Color.argb(50, 255, 0, 0));
			
			greenShade.setTag("redShade");
			
			greenShade.setLayoutParams(params);
			
			curr.addView(greenShade);
			*/
		}
		
		//handles displaying the second hint
		public void showSecondHint(){
			int[] loc = game.nextMove();
			int x = loc[2];
			int y = loc[3];
			
			BoxRelativeLayout curr = (BoxRelativeLayout)(buttonArr[x][y].getParent());
			
			curr.setBackgroundColor(Color.RED);
			/*
			RelativeLayout greenShade = new RelativeLayout(PlayingView.this);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			
			greenShade.setBackgroundColor(Color.argb(50, 255, 0, 0));
			
			greenShade.setTag("redShade");
			
			greenShade.setLayoutParams(params);
			
			curr.addView(greenShade);
			*/
		}
		
		//handles when the game is won
		public void winGame(){
			LinearLayout textArea = (LinearLayout)findViewById(R.id.textAreaLayout);
			RelativeLayout mainArea = (RelativeLayout)findViewById(R.id.playingViewOutterView);
			Button next = new Button(this);
			next.setId(777);
			next.setText("Next");
			next.setBackgroundResource(0);
			next.setTextColor(Color.WHITE);
			next.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent nextActivity = new Intent(PlayingView.this, TitlePage.class);
					nextActivity.putExtra("Chapter",""+Integer.parseInt(Chapter)); 
        			nextActivity.putExtra("Section",""+(Integer.parseInt(Section)+1));
        			startActivity(nextActivity);
					
				}
			});
			
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			
			
			Button restart = new Button(this);
			restart.setId(7777);
			restart.setText("Restart");
			restart.setBackgroundResource(0);
			restart.setTextColor(Color.WHITE);
			restart.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent nextActivity = new Intent(PlayingView.this, PlayingView.class);
					nextActivity.putExtra("Chapter",""+Chapter); 
        			nextActivity.putExtra("Section",""+Section);
        			startActivity(nextActivity);
					
				}
			});
			
			RelativeLayout.LayoutParams paramsRestart = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			paramsRestart.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			paramsRestart.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			
			next.setLayoutParams(params);
			restart.setLayoutParams(paramsRestart);
			
			mainArea.addView(next);
			mainArea.addView(restart);
			
			RelativeLayout.LayoutParams paramsText = (RelativeLayout.LayoutParams)textArea.getLayoutParams();
			paramsText.addRule(RelativeLayout.ABOVE,next.getId());
			
			
			
			for(int i = 0; i<8; i++){
				for(int j = 0; j<8; j++){
					buttonArr[i][j].setOnClickListener(null);
				}
			}
			
			ImageButton undo = (ImageButton)findViewById(R.id.undoButton);
			undo.setOnClickListener(null);
			
			timer.cancel();
		
			final TextView tv = (TextView)findViewById(R.id.textArea);
			final ScrollView sv = (ScrollView)findViewById(R.id.textAreaScrollView);
			sv.post(new Runnable() { 
			    public void run() { 
			        sv.scrollTo(0, tv.getHeight()); 
			    } 
			}); 
		}

		//makes the animations animated
		public void computerAnimation(){
			
			final RelativeLayout mainLayout=(RelativeLayout)findViewById(R.id.chessBoard);

			ImageView img = new ImageView(PlayingView.this);
			img.setTag("slidingPic2");
			
			int []loc = game.appMove();
			
			if(loc==null){
	        	mainLayout.removeView(mainLayout.findViewWithTag("slidingPic1"));
	        	initChessBoardTest();
				winGame();
				return;
			}
			
			ImageButton previousImgBtn = buttonArr[loc[0]][loc[1]];
			ImageButton v = buttonArr[loc[2]][loc[3]];
			
			TranslateAnimation slide = setAnimation( previousImgBtn, v , img);
			previousImgBtn.setImageResource(0);
			slide.setDuration(1500);
			
			slide.setAnimationListener(new Animation.AnimationListener() {            
		        @Override
		        public void onAnimationEnd(Animation animation) {
		        	mainLayout.removeView(mainLayout.findViewWithTag("slidingPic1"));
		        	mainLayout.removeView(mainLayout.findViewWithTag("slidingPic2"));

		        	
		        	initChessBoardTest();
		        }

				@Override
				public void onAnimationRepeat(
						Animation animation) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onAnimationStart(
						Animation animation) {
					// TODO Auto-generated method stub
					
				}
		    });
			
			img.setAnimation(slide);
			
		}
		
		
		public TranslateAnimation setAnimation(ImageButton prevButton, ImageButton afterButton, ImageView img){
			
			String imgRes = prevButton.getTag().toString();
			int resourceId = getResources().getIdentifier (imgRes, "drawable", "com.example.powerchessapp");
			
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(prevButton.getWidth(), prevButton.getHeight());
			
			BoxRelativeLayout view = (BoxRelativeLayout)prevButton.getParent();
			LinearLayout row = (LinearLayout)view.getParent();
			final RelativeLayout mainView = (RelativeLayout)row.getParent();
			
			float xLocInBtn = prevButton.getX();
			float xLocInView = xLocInBtn + view.getX();
			//float xLocInRow = xLocInView + row.getX();
			
			
			float yLocInBtn = prevButton.getY();
			float yLocInView = yLocInBtn + view.getY();
			float yLocInRow = yLocInView + row.getY();
			
			params.leftMargin = (int) xLocInView;
			params.topMargin = (int) yLocInRow;
			
			img.setImageResource(resourceId);
			img.setLayoutParams(params);
			
			
			mainView.addView(img);
			
			Log.d("pic","x:" + img.getX() + "y:" + img.getY());
			
			
			BoxRelativeLayout newView = (BoxRelativeLayout)afterButton.getParent();
			LinearLayout newRow = (LinearLayout)newView.getParent();
			
			float xLocInBtnNew = afterButton.getX();
			float xLocInViewNew = xLocInBtnNew + newView.getX();
			
			
			float yLocInBtnNew = afterButton.getY();
			float yLocInViewNew = yLocInBtnNew + newView.getY();
			float yLocInRowNew = yLocInViewNew + newRow.getY();
			
			TranslateAnimation slide = new TranslateAnimation(0, xLocInViewNew - xLocInView, 0, yLocInRowNew - yLocInRow);
			
			return slide;
			
			
		}
		
		
		
		public Activity getActivity(){
			return this;
		}
	
		
		
		//class to make the boxes square
		public class BoxRelativeLayout extends RelativeLayout
	    {
	    	public BoxRelativeLayout(Context context) {
				super(context);
				// TODO Auto-generated constructor stub
			}

			@Override 
	    	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
				super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			    int size=getMeasuredWidth();
			    setMeasuredDimension(size, size);
	    	}

	    }


		//update text area
		public void updateTextArea(String stringToAdd) {
			// TODO Auto-generated method stub
			
			final TextView tv = (TextView)findViewById(R.id.textArea);
			String s = tv.getText().toString();
			s=s+"\n" + stringToAdd;
			tv.setText(stringToAdd);
			final ScrollView sv = (ScrollView)findViewById(R.id.textAreaScrollView);
			sv.post(new Runnable() { 
			    public void run() { 
			        sv.scrollTo(0, tv.getHeight()); 
			    } 
			}); 
			
			
		}
		
		//clears the text area
		public void clearTextArea(){
			TextView tv = (TextView)findViewById(R.id.textArea);
			tv.setText("");
		}
		
		//makes the undo button undo
		public void initUndoButton(){
			
			ImageView img = (ImageView)findViewById(R.id.undoButton);
			TextView time = (TextView)findViewById(R.id.timer);
			int sss=time.getLineHeight();
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(sss, sss);
			
			RelativeLayout board = (RelativeLayout)this.findViewById(R.id.mainView);
			params.addRule(RelativeLayout.BELOW, board.getId());
			params.addRule(RelativeLayout.ALIGN_LEFT, board.getId());
			
			img.setLayoutParams(params);
			img.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String s = game.getHistoryMove();
					if(s=="empty")
						return;
					setBoard(s);
					updateTurnLabel();
					
				}
			});
			
			
			
		}
		
		//makes the promotion alert work
		public void promotionAlert(final int x, final int y, final char color){
			
			//.d("Promotion","Promotion3");
			final Dialog alert = new Dialog(PlayingView.this);
			
	    	alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    	alert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	    	alert.setContentView(R.layout.promotion_alert);
	    	TextView title = (TextView) alert.findViewById(R.id.title);
	    	title.setText("Choose a Piece:");
	    	
	    	RelativeLayout rl = (RelativeLayout) alert.findViewById(R.id.alertPieceArea);
	    	
	    	LinearLayout outter = new LinearLayout(PlayingView.this);
	    	
	    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	    	outter.setLayoutParams(params);
	    	
	    	LinearLayout ll;
	    	LinearLayout.LayoutParams llParams;// = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,1);
	    	
	    	ImageButton img;
	    	LinearLayout.LayoutParams imgParams;
	    	
	    	String[] pieces = { 
	    			color+"r",
	    			color+"n",
	    			color+"b",
	    			color+"q"
	    	};
	    	
	    	for(int i=0; i<pieces.length; i++){
	    		ll = new LinearLayout(PlayingView.this);
	    		llParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1);
	    		
	    		ll.setLayoutParams(llParams);
	    		
	    		img = new ImageButton(PlayingView.this);
				int resourceId = getResources().getIdentifier (pieces[i], "drawable", "com.example.powerchessapp");
				img.setImageResource(resourceId);
				imgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				img.setTag(pieces[i]);
				
				img.setLayoutParams(imgParams);
				img.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						char c = v.getTag().toString().charAt(1);
						String s = ""+c;
						c=s.toUpperCase().charAt(0);
						game.promotion(c, color, x, y);
						String chessBoard = game.getChessBoard();
						
						setBoard(chessBoard);
						
						alert.dismiss();
					}
				});
				
				ll.addView(img);
				outter.addView(ll);
				
	    	}
	    	rl.addView(outter);
	    	
			alert.show();


        }
        
		
		//displays all the labels correctly
		class getStrings extends AsyncTask<String, String, String> {

			String titleText;
			String questionText;
			String startBoard;
			String colorsTurn;
			String moveTree;
			String[][] answerText;
			String[][] wrongText;
			
			@Override
			protected String doInBackground(String... parameters) {
				// TODO Auto-generated method stub
				
				
				String url = "http://web.njit.edu/~afo4/cs491/getLessonInformation.php";
		        
				List<NameValuePair> params = new ArrayList<NameValuePair>();
		    	params.add(new BasicNameValuePair("chapnum",Chapter));
		    	params.add(new BasicNameValuePair("sectnum",Section));
		    	
		        
		        HttpClient httpclient = new DefaultHttpClient();
		        String paramString = URLEncodedUtils.format(params, "UTF-8");
		        HttpGet httpget = new HttpGet(url+"?"+paramString);
		        try{
		        	
		           	HttpResponse response = httpclient.execute(httpget);
		        	String jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
		        	
		        	//.d("JSON",jsonResult);
		        	
		        	
		        	JSONObject jsonOutterObject = new JSONObject(jsonResult);
	            	JSONArray jsonArray = jsonOutterObject.getJSONArray("LessonInfo");
	            	JSONObject jsonObject = jsonArray.getJSONObject(0);

		        	
		        	
		        	titleText = jsonObject.getString("title_text");
		        	questionText = jsonObject.getString("question_text");
		        	startBoard = jsonObject.getString("start_board");
		        	colorsTurn = jsonObject.getString("color_to_move");
		        	moveTree = jsonObject.getString("move_tree");
		        	
		        	jsonArray = jsonObject.getJSONArray("answer_text");
		        	answerText = new String[jsonArray.length()][2];
		        	for(int i=0;i<jsonArray.length();i++){
		        		JSONObject obj = jsonArray.getJSONObject(i);
		        		answerText[i][0]=obj.getString("comment_num");
		        		answerText[i][1]=obj.getString("text");
		        	}
		        	
		        	jsonArray = jsonObject.getJSONArray("wrong_text");
		        	wrongText = new String[jsonArray.length()][2];
		        	for(int i=0;i<jsonArray.length();i++){
		        		JSONObject obj = jsonArray.getJSONObject(i);
		        		wrongText[i][0]=obj.getString("comment_num");
		        		wrongText[i][1]=obj.getString("text");
		        	}
		        	
		        	
		        	
		        	
		        	return "1";
		        	
		        }catch(JSONException e){
		        	e.printStackTrace();
		        //}catch(ClientProtocolException e){
		        //	e.printStackTrace();
		        //}catch(IOException e){
		        	//e.printStackTrace();
		        }catch(NullPointerException e){
		        	e.printStackTrace();
		        }
		        catch(Exception e){
		        	e.printStackTrace();
		        }
		        
		        return "0";
			}
			
			
			private StringBuilder inputStreamToString(InputStream is){
		    	String rLine="";
		    	StringBuilder answer=new StringBuilder();
		    	BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    	
		    	try{
		    		while((rLine=rd.readLine())!=null){
		    			answer.append(rLine);
		    		}
		    	}catch(IOException e){
		    		e.printStackTrace();
		    	}
		    	return answer;
		    	
		    }
			
			
			@Override
			protected void onPostExecute(String s){
								
				//Log.d("answer/wrong", answerText[0][0] + " " + answerText[0][1] + " " +answerText[1][0] + " " + answerText[1][1]+"," + wrongText[0][0] + " " + wrongText[0][1] + " " +wrongText[1][0] + " " + wrongText[1][1]);

				game = new GameRules(GameRules.LESSON, startBoard,colorsTurn.charAt(0),moveTree,answerText,wrongText);
	            game.setContextAndActivity(PlayingView.this,PlayingView.this.getActivity());
	            
	            TextView tv = (TextView)PlayingView.this.findViewById(R.id.playingTitle);
	            tv.setText(titleText);
	           
	            tv = (TextView)PlayingView.this.findViewById(R.id.textArea);
	            tv.setText(questionText);

				initChessBoardTest();
	            //initTimer();
				
			}
			
		}
		
		
		
	    
	

}
