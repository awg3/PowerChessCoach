package com.example.powerchessapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TitlePage extends Activity {
	
	String ChapterNumber;
	String SectionNumber;
	String SectionNumberRawText;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.title_page);
		
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.hide();
		
		Intent intent = getIntent();
        ChapterNumber = intent.getStringExtra("Chapter").toString();
        SectionNumber = intent.getStringExtra("Section").toString();

        @SuppressWarnings("unused")
		TopBar tb = new TopBar(this,"");
        
		initTitleText();
		
		new LoadText(ChapterNumber,SectionNumber).execute();
	}
	
	
	public void initTitleText(){
		
		
	}
	

	
	
	
	
	
	//gathers and sets the title page with the paporpiate inforamtion
    class LoadText extends AsyncTask<String, String, String> {

	
    	String ChapterNumber;
    	String SectionNumber;
    	
    	public LoadText(String ChapterNumber, String SectionNumber){
    		this.ChapterNumber=ChapterNumber;
    		this.SectionNumber=SectionNumber;
    	}
	
	
		

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return getSectionText(ChapterNumber,SectionNumber);
		}
		
		public String getSectionText(String chap, String sec){
			
			
			String url = "http://web.njit.edu/~afo4/cs491/getSectionText.php";
	        
			List<NameValuePair> params = new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("chapnum",chap));
	    	params.add(new BasicNameValuePair("sectnum",sec));
	    	
	    	Log.d("vals", chap+" " +sec);
	    	
	        
	        HttpClient httpclient = new DefaultHttpClient();
	        String paramString = URLEncodedUtils.format(params, "UTF-8");
	        HttpGet httpget = new HttpGet(url+"?"+paramString);
	        try{
	        	
	           	HttpResponse response = httpclient.execute(httpget);
	        	String jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
	        	
	        	Log.d("JSON",jsonResult);
	        	
	        	
	        	JSONObject jsonOutterObject = new JSONObject(jsonResult);
	        	String sectText = jsonOutterObject.getString("section_text");
	        	SectionNumberRawText = jsonOutterObject.getString("title");
	        	
	        	
	        	return sectText;
	        	
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
	        
	        return null;
			
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
			
			TextView tv = (TextView)TitlePage.this.findViewById(R.id.titlePageDescriptionText);
			if(!s.equals("null"))
				tv.setText(s);
			Button button = (Button)TitlePage.this.findViewById(R.id.titlePageNextButton);
			button.setVisibility(View.VISIBLE);
			
			//LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			//button.setLayoutParams(params);
			button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					Intent nextActivity = new Intent(TitlePage.this, PlayingView.class);
        			nextActivity.putExtra("Chapter",ChapterNumber); 
        			nextActivity.putExtra("Section",SectionNumber);
        			startActivity(nextActivity);
					
					
				}
			});
			
			
			tv = (TextView)TitlePage.this.findViewById(R.id.titlePageTitleText);
			tv.setText(SectionNumberRawText);
			
		}
		
		
		
		
		
		
		
		
		
    }
	
	
	
	
	
		
		
}
