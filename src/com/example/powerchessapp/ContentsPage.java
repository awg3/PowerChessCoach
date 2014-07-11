package com.example.powerchessapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

public class ContentsPage extends Activity{

	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	    	//int k=1/0;
	    	
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.contents_page);
	        
	        new makeList().execute();
	        
	        @SuppressWarnings("unused")
	        TopBar tb = new TopBar(this,"contents");
	        
	        ActionBar actionBar = this.getActionBar();
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.hide();
	        
	 }
	 
	 
	 
	 
	
	 
	 
	 
	class makeList extends AsyncTask<String, String, ArrayList<ArrayList<String>>> {
	
		
		
		ArrayList<ArrayList<String>> list ;
		
		
		@Override
    	protected void onPreExecute(){
			list = new ArrayList<ArrayList<String>>();

		}
		
		
		//gets the data from the db
		@Override
		protected ArrayList<ArrayList<String>> doInBackground( String... params) {
			// TODO Auto-generated method stub
						
			String url = "http://web.njit.edu/~afo4/cs491/getChapterInformation.php";

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);

            try{
            	
               	HttpResponse response = httpclient.execute(httpget);
            	String jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
            	
            	Log.d("JSON",jsonResult);
            	
            	
            	JSONObject jsonOutterObject = new JSONObject(jsonResult);
            	JSONArray jsonArray = jsonOutterObject.getJSONArray("ChapterInfo");
            	//Log.d("STRING",jsonArray.toString());
            	for(int j=0;j<jsonArray.length();j++){
            		ArrayList<String> arrList = new ArrayList<String>();
            		
            		JSONObject jsonObject = jsonArray.getJSONObject(j);
	            	String serverType = jsonObject.getString("chapter_num");
	            	arrList.add(serverType);
	            	
            		JSONArray arr = jsonObject.getJSONArray("section_info");
	            	for(int i=0;i<arr.length();i++){
	            		
	            		JSONObject c = arr.getJSONObject(i);          		
	            		
	            		String secNum=c.getString("section_num");
	            		String secTitle = c.getString("title_text");
	            		
	            		arrList.add(secNum);
	            		arrList.add(secTitle);
	            		
	            	}
	            	list.add(arrList);
	            	
            	}
            	//All the exceptions set invalid to true to prompt for new url
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

            Log.d("here","after");
            return list;
            
            
            
            
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
		
		//populates the list
		@Override
		protected void onPostExecute(ArrayList<ArrayList<String>> arr){
			
			for(ArrayList<String> outter:list){
				for(String s:outter){
					Log.d("List", s);
				}
				Log.d("List", "Next");
			}
			
			makeELList();
			
		}
		
		//actually populates the list
		public void makeELList(){
        	
	    	final ExpandableListView el = new ExpandableListView(ContentsPage.this);
	    	el.setBackgroundColor(Color.parseColor("#080819"));
	    	LinearLayout.LayoutParams listParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
	    	el.setLayoutParams(listParams);
	    	el.setId(1157);
	    	el.setTag("ExpandableList");
	    	
	    	List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
	        List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();

	        Map<String, String> curGroupMap = new HashMap<String, String>();
	        //groupData.add(curGroupMap);
	        
	        //curGroupMap.put("Name", "Overall Status");
	         
	        List<Map<String, String>> children = new ArrayList<Map<String, String>>();
	        Map<String, String> curChildMap = new HashMap<String, String>();
	        
	        
	        //curChildMap.put("Name","Executive Summary");
	        
	        for(ArrayList<String> arr:list){
	        	for(int i=0; i<arr.size(); i++){
	        		if(i==0){
	        			String chapTitle="";
	        			String chapNum = arr.get(i);
	        			chapTitle = arr.get(i+2);
	        			
	        			curGroupMap.put("Name",  chapTitle);
	        			i+=2;
	        		} else{
	        			
	        			String title = arr.get(i);
	        			String desc = arr.get(i+1);
	        			curChildMap.put("Name",desc);
	        			i++;
	        			children.add(curChildMap);
			        	curChildMap = new HashMap<String, String>();
	        		}
		        	
	        	}
	        	groupData.add(curGroupMap);
	        	childData.add(children);
	        	curGroupMap = new HashMap<String, String>();
	        	children = new ArrayList<Map<String, String>>();
	        }
	        
	        /*
	        curChildMap = new HashMap<String, String>();
	        children.add(curChildMap);
	        curChildMap.put("Name","Mail Delivery");
	*/
	       // childData.add(children);
	        
	       // #080819
	        
	        ColorDrawable devidrColor = new ColorDrawable(Color.parseColor("#ffffff"));
	        el.setDivider(devidrColor);
	        el.setChildDivider(devidrColor);
	        el.setDividerHeight(2);
	        
	        // Sets up the adapter
	        ExpandableListAdapter mAdapter = new SimpleExpandableListAdapter(
	                ContentsPage.this,
	                
	                groupData,
	                R.layout.group_row,
	                new String[] {"Name"},
	                new int[] {R.id.row_name},
	                
	                childData,
	                R.layout.child_row,
	                new String[] {"Name"},
	                new int[] {R.id.grp_child}
	                ){
	        	
	        	@SuppressLint("NewApi")
				@Override
	        	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent){
	        		
	        		View v = super.getGroupView(groupPosition, isExpanded, convertView, parent);
	        		TextView tv = (TextView) v.findViewById(R.id.row_name);
	        		
	        		
	        		String[] pieces = { 
	    	    			"wp",
	        				"wr",
	    	    			"wn",
	    	    			"wb",
	    	    			"wq",
	    	    			"wk"
	    	    	};
	        		
	        		int i = groupPosition % 6;
	        		
	        		int resourceId = getResources().getIdentifier (pieces[i], "drawable", "com.example.powerchessapp");
					Drawable img = getResources().getDrawable(resourceId);
					
					Bitmap b = ((BitmapDrawable)img).getBitmap();
				    Bitmap bitmapResized = Bitmap.createScaledBitmap(b, tv.getLineHeight(), tv.getLineHeight(), false);
				    
				    img = new BitmapDrawable(getResources(), bitmapResized);
					
					//img.setBounds(left, top, right, bottom)
					//imgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					//img.setTag(pieces[i]);
					
	        		
	        		
	        		tv.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
	        		//tv.setCompoundDrawablesRelative(img, null, null, null);

					//tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.wp, 0, 0, 0);
	        		return v;
	                
	        	}
	        	/*
	        	@Override
	        	public View newGroupView(boolean isExpanded, ViewGroup parent){
	        		return layoutInflater.inflate(R.layout.group_row, null, false);
	        	}
	        	*/
	        	
	        };
	        
 	        
	        
	        el.setAdapter(mAdapter);
	        
	        //sets a onclicklistener for the 1st selection, Executive Summary
	        el.setOnChildClickListener(new OnChildClickListener(){
	        	@Override
	            public boolean onChildClick(ExpandableListView parent, View v, int group_position, int child_position, long id)
	            { 
	        		//if(group_position==0 && child_position==0){
	        			
	        			//timer.cancel();
	        			//Intent nextActivity = new Intent(MainActivity.this, ServerStatus.class);
	        			//nextActivity.putExtra("status", "All"); 
	        			//nextActivity.putExtra("serverType","All"); 
	        			//startActivity(nextActivity);
	        		
	        			Log.d("TEST","hi1");
	        			TextView secNum = (TextView)v.findViewById(R.id.grp_child);
	        			/*
	        			String secNumText = secNum.getText().toString();
	        			int startIndex = 0;
	        			int endIndex = secNumText.indexOf(":");
	        			String secString = secNumText.substring(startIndex,endIndex);
	        			*/
	        			String secString = ""+(child_position+1);
	        			
	        			/*
	        			Log.d("TEST","hi2");
	        			TextView chapNum = (TextView)parent.getChildAt(group_position).findViewById(R.id.row_name);
	        			String chapNumText = chapNum.getText().toString();
	        			startIndex = 0;
	        			endIndex = chapNumText.indexOf(":");
	        			String chapString = chapNumText.substring(startIndex,endIndex);
	        			*/
	        			
	        			String chapString = ""+(group_position+1);
	        			
	        			Log.d("Chapter", chapString);
	        			Log.d("Section", secString);
	        			
	        			
	        			parent.collapseGroup(group_position);
	        			
	        			Log.d("TEST","hi3");
	        			
	        			Intent nextActivity = new Intent(ContentsPage.this, TitlePage.class);
	        			nextActivity.putExtra("Chapter",chapString); 
	        			nextActivity.putExtra("Section",secString);
	        			startActivity(nextActivity);
	        			
	        			
	        			//parent.getChildAt(child_position).
	        			

	        		//}
	        		/*else if(group_position==0 && child_position==1){
	        			
	        			AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
	        			alert.setTitle("Additional Details:");
	        			alert.setMessage("This feature has yet to be added");
	        			alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	        				
	        				@Override
	        				public void onClick(DialogInterface dialog, int which) {
	        					// TODO Auto-generated method stub
	        					
	        				}
	        			});
	        			alert.show();
	        			
	        			parent.collapseGroup(group_position);
	        		}*/
	        		return false;
	            }
	        });
	        el.setOnGroupExpandListener(new OnGroupExpandListener() {
	            int previousGroup = -1;

	            @Override
	            public void onGroupExpand(int groupPosition) {
	                if(groupPosition != previousGroup)
	                    el.collapseGroup(previousGroup);
	                previousGroup = groupPosition;
	            }
	        });
			LinearLayout temp = (LinearLayout)findViewById(R.id.contentsLinearLayout);
	        temp.addView(el);
	        
	    }
		
	}

	
	
}
