package aurora.project;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class GamesActivity extends Activity{

	private RelativeLayout gameselect;
	private Button didtheyplay;
	private TextView didtheywon;
	private TextView didtheyplayed;
	private Button whodunnitplay;
	private TextView whodunnitwon;
	private TextView whodunnitplayed;
	
	private RelativeLayout playing;
	private TextView result;
	private ImageView didtheypic;
	private TextView whodunnittext;
	private TextView didtheyWho;
	private RadioGroup radiogroup;
	private RadioButton option1;
	private RadioButton option2;
	private RadioButton option3;
	private RadioButton option4;
	private RadioButton[] radiobuttonarr;
	private int correct;
	private Button back;
	private Button answer;
	private Boolean playingdidthey;
	
	public void onCreate(Bundle savedInstanceState){
		Log.e("log_tag", "STARTING GAMESACTIVITY ONCREATE");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.games_activity_layout);
		
		gameselect = (RelativeLayout) findViewById(R.id.gameselect);
		didtheyplay = (Button) findViewById(R.id.playdidthey);
		didtheywon = (TextView) findViewById(R.id.didtheywon);
		didtheyplayed = (TextView) findViewById(R.id.didtheyplayed);
		whodunnitplay = (Button) findViewById(R.id.playwhodunnit);
		whodunnitwon = (TextView) findViewById(R.id.whodunnitwon);
		whodunnitplayed = (TextView) findViewById(R.id.whodunnitplayed);
		
		playing = (RelativeLayout) findViewById(R.id.playing);
		result = (TextView) findViewById(R.id.result);
		didtheypic = (ImageView) findViewById(R.id.didtheypic);
		whodunnittext = (TextView) findViewById(R.id.whodunnittext);
		didtheyWho = (TextView) findViewById(R.id.didtheyWho);
		radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
		option1 = (RadioButton) findViewById(R.id.option1);
		option2 = (RadioButton) findViewById(R.id.option2);
		option3 = (RadioButton) findViewById(R.id.option3);
		option4 = (RadioButton) findViewById(R.id.option4);
		radiobuttonarr = new RadioButton[4];
		radiobuttonarr[0] = option1;
		radiobuttonarr[1] = option2;
		radiobuttonarr[2] = option3;
		radiobuttonarr[3] = option4;
		back = (Button) findViewById(R.id.back);
		answer = (Button) findViewById(R.id.answer);
		
		playing.setVisibility(4);
		
    	didtheyplay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	playingdidthey=true;
            	clearGameAnswers();
            	gameselect.setVisibility(4);
            	playing.setVisibility(0);
            	option1.setText("They did");
            	option2.setText("They didn't");
            	option3.setVisibility(4);
            	option4.setVisibility(4);
            	//TODO pic and correcttext
            	
            	correct = (int) Math.floor(Math.random()*4);
            	// fix populate game answers
            	new PopulateGameAnswers().execute();      	
            }
        });
    	
    	whodunnitplay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	playingdidthey=false;
            	clearGameAnswers();
            	gameselect.setVisibility(4);
            	playing.setVisibility(0);
            	didtheypic.setVisibility(0);
            	whodunnittext.setVisibility(0);
            	option3.setVisibility(0);
            	option4.setVisibility(0);
            	//TODO set whodunnit and correct text
            	
            	correct = (int) Math.floor(Math.random()*4);
            	new PopulateGameAnswers().execute();
            }
        });
    	
    	answer.setOnClickListener(new View.OnClickListener() {
    		//TODO update database with wins/played
			public void onClick(View v) {
				int isCorrect = 0;
				//increment plays
				if (radiobuttonarr[correct].isChecked()) {
					result.setText("That is correct!");
					isCorrect = 1;
				}
				else
					result.setText("I'm sorry, the correct answer was " + radiobuttonarr[correct].getText().toString() + ".");
				radiogroup.clearCheck();
				new UpdateScore().execute(isCorrect);
				if (playingdidthey){
					didtheyplay.performClick();
				}
				else
					whodunnitplay.performClick();
			}
		});

    	back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	gameselect.setVisibility(0);
            	playing.setVisibility(4);
            	result.setText("");
            	clearGameScores();
            	new PopulateGameScores().execute();
            }
        });
    	
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    if (newConfig.orientation == (Configuration.ORIENTATION_LANDSCAPE))
	    	Toast.makeText(GamesActivity.this, "The games only function properly while the phone is vertical.", Toast.LENGTH_LONG).show();
	}
	
	public void myOnResume(){
		playing.setVisibility(4);
		gameselect.setVisibility(0);
		
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
			Toast.makeText(GamesActivity.this, "The games only function properly while the phone is vertical.", Toast.LENGTH_LONG).show();
		clearGameScores();
		new PopulateGameScores().execute();
	}
	
	public void clearGameAnswers() {
		didtheypic.setImageDrawable(null);
		whodunnittext.setText("");
		for(int i=0; i<=3; i++) {
			radiobuttonarr[i].setText("");
		}
	}
	
	public void clearGameScores() {
		didtheyplayed.setText("");
		didtheywon.setText("");
		whodunnitwon.setText("");
		whodunnitplayed.setText("");
	}
	private class PopulateGameScores extends AsyncTask<Void, Void, JSONArray> {
		@Override
		protected JSONArray doInBackground(Void... params) {
			String result = "";
			InputStream is = null;
			JSONArray jArray = null;
			
			//send USER_ID
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("user_id", String.valueOf(Aurora.USER_ID)));
			
			//http post
			try{
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response;
				HttpPost httppost = new HttpPost("http://auroralabs.cornellhci.org/android/getGameScores.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				response = httpclient.execute(httppost);

				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			}catch(Exception e){
				Log.e("POPULATE GAME SCORES", "Error in http connection "+e.toString());
			}
			
			//get jason encoded response
			try{			
			        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			        String line = null;
			        while ((line = reader.readLine()) != null) {
			        	result += line;	        	
			        }
			        is.close();
			        
			        jArray = new JSONArray(result);
			}catch(Exception e){
			        Log.e("POPULATE GAME SCORES", "Error converting result "+e.toString());
			}
			
			return jArray;
		}
		
		@Override
    	protected void onPostExecute(JSONArray jArray){		
    		try{
    			JSONObject json_data = jArray.getJSONObject(0);
				didtheywon.setText(json_data.getString("num_correct"));
				didtheyplayed.setText(json_data.getString("times_played"));
				
				json_data = jArray.getJSONObject(1);
				whodunnitwon.setText(json_data.getString("num_correct"));
				whodunnitplayed.setText(json_data.getString("times_played"));
				
            } catch(Exception e) {
                Log.e("POPULATE GAME SCORES", "Error setting scores "+e.toString());
                
            }
    	}
    }
	
	private class PopulateGameAnswers extends AsyncTask<Void, Void, JSONArray> {
		@Override
		protected JSONArray doInBackground(Void... params) {
			String result = "";
			InputStream is = null;
			JSONArray jArray = null;
			String url = "";
			
			if(playingdidthey)
				//TODO make didthey query
				url = "http://auroralabs.cornellhci.org/android/getAssociateAnswers.php";
			else
				url = "http://auroralabs.cornellhci.org/android/getWhodunnitAnswers.php";
			
			//send USER_ID
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("user_id", String.valueOf(Aurora.USER_ID)));
			
			//http post
			try{
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response;
				HttpPost httppost = new HttpPost(url);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				response = httpclient.execute(httppost);

				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			}catch(Exception e){
				Log.e("POPULATE GAME ANSWERS", "Error in http connection "+e.toString());
			}
			
			//get jason encoded response
			try{			
				BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
				String line = null;
				while ((line = reader.readLine()) != null) {
					result += line;	        	
				}
				is.close();

				jArray = new JSONArray(result);
			}catch(Exception e){
				Log.e("POPULATE GAME ANSWERS", "Error converting result "+e.toString());
			}

			return jArray;
		}
		
		@Override
    	protected void onPostExecute(JSONArray jArray){		
    		try{
    			String key = "";
    			String answer = "";
    			JSONObject json_data = jArray.getJSONObject(0);
    			if(playingdidthey)
    				key = "notes";
    			else { //whodunnit
    				key = "username";
    				String note = json_data.getString("notes");
    				if(note.trim().equals(""))
    					note = "[blank]";
    				whodunnittext.setText(note);
    			}
    			
    			new DownloadMoodImage().execute("http://auroralabs.cornellhci.org/img/" +json_data.getString("path").replace("\\", ""));			
    			
    			//set answers
            	for (int i = 0, j = 1; i <= 3; i++){
            		if (i != correct) {
            			json_data = jArray.getJSONObject(j);		
            			j++;
            		} else {
            			json_data = jArray.getJSONObject(0);
            		}
            		answer = json_data.getString(key);
        			if(answer.trim().equals(""))
        				answer = "[blank]";
        			radiobuttonarr[i].setText(answer);	
            	}			
            } catch(Exception e) {
                Log.e("POPULATE GAME ANSWERS", "Error setting scores "+e.toString());       
            }
    	}
    }
	
	private class DownloadMoodImage extends AsyncTask<Object, Void, Bitmap> {
    	@Override
        protected Bitmap doInBackground(Object... params) {
       		String url = (String) params[0];
            InputStream is = null;
  
    		//http get
        	try{
        		HttpClient httpclient = new DefaultHttpClient();
        		HttpGet httpget = new HttpGet(url);
        		HttpResponse response = httpclient.execute(httpget);
    		    Log.e("DOWNLOAD MOOD IMAGES", url);
        		HttpEntity entity = response.getEntity();
        		is = entity.getContent();
    		}catch(Exception e){
    			Log.e("DOWNLOAD MOODS IMAGES", "Error in http connection "+e.toString());
    		}
            	
    		return (BitmapFactory.decodeStream(is));
        }
    	
    	@Override
        protected void onPostExecute(Bitmap bitmap){
    		try {
    			didtheypic.setImageBitmap(bitmap);
    		} catch (Exception e) {
    			Log.e("DOWNLOAD MOODS IMAGES", "Error setting image "+e.toString());
    		}		
        }
    }
	
	private class UpdateScore extends AsyncTask<Object, Void, JSONArray> {
		@Override
		protected JSONArray doInBackground(Object... params) {
			int isCorrect = (Integer)params[0];
			String result = "";
			InputStream is = null;
			
			int gameId = 1;
			if(!playingdidthey)
				gameId = 2;
			
			//send USER_ID
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("user_id", String.valueOf(Aurora.USER_ID)));
			nameValuePairs.add(new BasicNameValuePair("is_correct", String.valueOf(isCorrect)));
			nameValuePairs.add(new BasicNameValuePair("game_id", String.valueOf(gameId)));
			
			//http post
			try{
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response;
				HttpPost httppost = new HttpPost("http://auroralabs.cornellhci.org/android/updateGame.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				response = httpclient.execute(httppost);

				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			}catch(Exception e){
				Log.e("UPDATE SCORE", "Error in http connection "+e.toString());
			}
			
			//check response
			try{			
				BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
				String line = null;
				while ((line = reader.readLine()) != null) {
					result += line;	        	
				}
				is.close();
			}catch(Exception e){
				Log.e("UPDATE SCORE", "Error converting result "+e.toString());
			}
			
			//TODO
			if(!result.equals("OKOK")) {
				
				Toast.makeText(GamesActivity.this, "Error updating score", Toast.LENGTH_SHORT);
				Log.e("UPDATE SCORE", "FAILED TO UPDATE SCORE: " + result);
			}
				
			return null;
		}
    }
}
