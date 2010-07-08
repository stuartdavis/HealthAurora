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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PostActivity extends Activity{
	
	private RelativeLayout picTakingScreen;
	private RelativeLayout previewholder;
	private ScrollView scrollView;
	private ImageView yourPicture;
	private String posttext;
	private RadioGroup radiogroup;
	private RadioButton didThisButton;
	private EditText entry;
	private SeekBar healthScale;
	private TextView healthinessText;
	private int healthiness;
	private Boolean didIt;
	private Button poster;
	private Button cancel;
	private Button morebutton;
	private RelativeLayout picselectscreen;
	private Button cancelfrompicselect;
	private Button firstpost;
	private GridView gridview;
	private ImageView selectedpic;
	private RelativeLayout confirmscreen;
	private TextView confirmedtext;
	private ImageView foodconfirmedpic;
	private ImageView moodconfirmedpic;
	private TextView extras;
	private Button finalcancel;
	private Button confirm;
	private MoodAdapter moodAdapter;
	private int selectedPosition;
	private ProgressDialog dialog;
	
	Camera camera;
	Preview preview;
	boolean previewMade = false;
	private static final String TAG = "CameraDemo";
	
	public void onCreate(Bundle savedInstanceState){
		try{ 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_activity_layout);
		Log.e("log_tag", "STARTING POSTACTIVITY ONCREATE");

		picTakingScreen = (RelativeLayout) findViewById(R.id.picTakingScreen);
		previewholder = (RelativeLayout) findViewById(R.id.previewholder);
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		yourPicture = (ImageView) findViewById(R.id.yourPicture);
		picselectscreen = (RelativeLayout) findViewById(R.id.picselectscreen);
		cancelfrompicselect = (Button) findViewById(R.id.cancelfrompicselect);
		confirmscreen = (RelativeLayout) findViewById(R.id.confirmscreen);
		morebutton = (Button) findViewById(R.id.morebutton);
		cancel = (Button) findViewById(R.id.cancel);
		radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
		didThisButton = (RadioButton) findViewById(R.id.didThisButton);
		entry = (EditText) findViewById(R.id.entry);
		healthScale = (SeekBar) findViewById(R.id.healthScale);
		healthinessText = (TextView) findViewById(R.id.healthinessText);
        poster = (Button) findViewById(R.id.next);
        firstpost = (Button) findViewById(R.id.firstpost);
        gridview = (GridView) findViewById(R.id.gridview02);
        selectedpic = (ImageView) findViewById(R.id.selectedpic);
        confirmedtext = (TextView) findViewById(R.id.confirmedtext);
        foodconfirmedpic = (ImageView) findViewById(R.id.foodconfirmedpic);
        moodconfirmedpic = (ImageView) findViewById(R.id.moodconfirmedpic);
        extras = (TextView) findViewById(R.id.extras);
        finalcancel = (Button) findViewById(R.id.finalcancel);
        confirm = (Button) findViewById(R.id.confirm);
        
        picselectscreen.setVisibility(8);
		scrollView.setVisibility(8);
		confirmscreen.setVisibility(8);
		
		healthScale.setKeyProgressIncrement(1);
		healthiness = -1;
        
        moodAdapter = new MoodAdapter(this);
		gridview.setAdapter(moodAdapter);
        
		preview = new Preview(this);
		previewholder.addView(preview);
        
        preview.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		preview.camera.takePicture(shutterCallback, rawCallback, jpegCallback);
        		scrollView.setVisibility(0);
        		picTakingScreen.setVisibility(8);
        		previewMade = true;
        	}
        });
		
		morebutton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		moodAdapter.populate();
        		selectedpic.setImageResource(-1);
        	}
        });
        
    	firstpost.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		if(selectedpic.getDrawable()!=null) {
	        		moodconfirmedpic.setImageDrawable(yourPicture.getDrawable());
	        		foodconfirmedpic.setImageDrawable(selectedpic.getDrawable());
	        		selectedpic.setImageResource(-1);
	        		confirmscreen.setVisibility(0);
	        		picselectscreen.setVisibility(4);
	        		String str = new String();
	        		if (didIt)
	        			str = "I did this and rated it a " + healthiness + ".";
	        		else
	        			str = "I didn't do this and rated that a " + healthiness + ".";
	        		extras.setText(str);
        		}
        		else {
        			Toast.makeText(PostActivity.this, "A picture must be selected in order to post.", Toast.LENGTH_LONG).show();
        		}
        	}
        });
    	    	
	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	selectedpic.setImageDrawable( ( (ImageView) v).getDrawable() );
	        	selectedPosition = position;
	        }
	    });

	    healthScale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				healthinessText.setText("Healthiness: 1");
				healthiness = 1;
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				healthiness = progress + 1;
				healthinessText.setText("Healthiness: " + healthiness);
			}
		});
	    
        poster.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if (radiogroup.getCheckedRadioButtonId() != -1 && healthiness != -1) {
            		posttext = entry.getText().toString();
            		confirmedtext.setText(posttext);
            		entry.setText("");
            		scrollView.setVisibility(4);
            		preview.restart();
            		picselectscreen.setVisibility(0);
            		didIt = didThisButton.isChecked();
            	}
            	else
            		Toast.makeText(PostActivity.this, "You must select a Healthiness value and indicate whether or not you did the action in order to post.", Toast.LENGTH_LONG).show();
            }
        });
        
        cancelfrompicselect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	entry.setText("");
                selectedpic.setImageResource(-1);
                healthScale.setProgress(0);
                healthiness = -1;
                healthinessText.setText("");
                radiogroup.clearCheck();
                picselectscreen.setVisibility(4);
                picTakingScreen.setVisibility(0);
            }
        });
    
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                entry.setText("");
                selectedpic.setImageResource(-1);
                healthScale.setProgress(0);
                healthiness = -1;
                healthinessText.setText("");
                radiogroup.clearCheck();
                scrollView.setVisibility(4);
                picTakingScreen.setVisibility(0);
                preview.restart();
            }
        });
        
    	finalcancel.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		entry.setText("");
                selectedpic.setImageResource(-1);
                healthScale.setProgress(0);
                healthiness = -1;
                healthinessText.setText("");
                radiogroup.clearCheck();
        		picTakingScreen.setVisibility(0);
        		confirmscreen.setVisibility(4);
        	}
        });
    	
    	confirm.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		dialog = ProgressDialog.show(PostActivity.this, "", "Posting. Please wait...", true);
        		Aurora.addTask(new PostMoodStatusTask().execute());      
        		// TODO Make this also update whether or not done (from boolean 
        		// didIt), taken picture (ImageView foodconfirmedpic) and 
        		// healthiness value (int healthiness). If image can't be taken
        		// from here we can talk about how to get it.
        			         	
        	}
        });
		} catch (Exception e) {
			Log.e("log_tag", "POSTACTIVITY ERROR: " + e.toString());
		}
		
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	    	preview.setClickable(false);
	    	if (picTakingScreen.getVisibility() == 0 && Aurora.signedIn) {
	    		Toast.makeText(PostActivity.this, "Phone must be vertical to take picture.", Toast.LENGTH_LONG).show();
	    	}
	    }
	    else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
	    	preview.setClickable(true);
	    }
	}
	
	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
//			Log.d(TAG, "onShutter'd");
		}
	};

	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
//			Log.d(TAG, "onPictureTaken - raw");
		}
	};

	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
/*			FileOutputStream outStream = null;
			try {
				outStream = new FileOutputStream(String.format(
						"/sdcard/%d.jpg", System.currentTimeMillis()));
				outStream.write(data);
				outStream.close();
				Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
*/
			BitmapFactory bitfac = new BitmapFactory();
			Bitmap bmp = bitfac.decodeByteArray(data, 0, data.length);
			int h = bmp.getHeight();
			int w = bmp.getWidth();
			Matrix mtx = new Matrix();
			mtx.postRotate(90);
			bmp = Bitmap.createBitmap(bmp, 0, 0, w, h, mtx, true);
			yourPicture.postInvalidate();
			yourPicture.setImageBitmap(bmp);
//			} 
		}
	};
	
	//TODO
	private class PostMoodStatusTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			String result="";
			//the mood status to add
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("user_id", String.valueOf(Aurora.USER_ID)));
			nameValuePairs.add(new BasicNameValuePair("photo_id", String.valueOf(moodAdapter.getImageId(selectedPosition))));
			nameValuePairs.add(new BasicNameValuePair("notes", confirmedtext.getText().toString()));
			InputStream is = null;
			
			//http post
			try{
			        HttpClient httpclient = new DefaultHttpClient();
			        HttpPost httppost = new HttpPost("http://auroralabs.cornellhci.org/android/postMoodStatus.php");
			        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			        HttpResponse response = httpclient.execute(httppost);
			        
			        HttpEntity entity = response.getEntity();
			        is = entity.getContent();
			}catch(Exception e){
			        Log.e("log_tag", "Error in http connection "+e.toString());
			}
			
			//convert response to string
			try{
			        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			        StringBuilder sb = new StringBuilder();
			        String line = null;
			        while ((line = reader.readLine()) != null) {
			                sb.append(line);
			        }
			        is.close();
			 
			        result=sb.toString();
			}catch(Exception e){
			        Log.e("log_tag", "Error converting result "+e.toString());
			}
			
			return result;
		}
		
		@Override
		public void onPostExecute(String result) {
			dialog.dismiss();
			picTakingScreen.setVisibility(0);
    		confirmscreen.setVisibility(4);
        	selectedpic.setImageResource(-1);
        	
        	if(result.equals("OK"))
        		Toast.makeText(PostActivity.this, "Mood posted successfully", Toast.LENGTH_SHORT).show();
        	else
        		Toast.makeText(PostActivity.this, "Error posting mood", Toast.LENGTH_SHORT).show();
        			
		}
    }
	
	public void myOnResume(){
		scrollView.setVisibility(4);
		confirmscreen.setVisibility(4);
		picselectscreen.setVisibility(4);
		picTakingScreen.setVisibility(0);
		selectedpic.setImageResource(-1);
		moodAdapter.populate();
		if (previewMade)
			preview.restart();
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			preview.setClickable(false);
			Toast.makeText(PostActivity.this, "Phone must be vertical to take picture.", Toast.LENGTH_LONG).show();
		}
			
	}
	
	public void onPause() {
		super.onPause();
		Aurora.killTasks();
	}
}
