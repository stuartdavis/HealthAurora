package aurora.project;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class MoodAdapter extends BaseAdapter {
    private Context mContext;
    private int[] emotions = {R.drawable.afraid_1, R.drawable.afraid_2, R.drawable.afraid_3,
    		R.drawable.tense_1, R.drawable.tense_2, R.drawable.tense_3,
    		R.drawable.excited_1, R.drawable.excited_2, R.drawable.excited_3,
    		R.drawable.delighted_1, R.drawable.delighted_2, R.drawable.delighted_3,
    		R.drawable.frustrated_1, R.drawable.frustrated_2, R.drawable.frustrated_3,
    		R.drawable.angry_1, R.drawable.angry_2, R.drawable.angry_3,
    		R.drawable.happy_1, R.drawable.happy_2, R.drawable.happy_3,
    		R.drawable.glad_1, R.drawable.glad_2, R.drawable.glad_3,
    		R.drawable.miserable_1, R.drawable.miserable_2, R.drawable.miserable_3,
    		R.drawable.sad_1, R.drawable.sad_2, R.drawable.sad_3,
    		R.drawable.calm_1, R.drawable.calm_2, R.drawable.calm_3,
    		R.drawable.satisfied_1, R.drawable.satisfied_2, R.drawable.satisfied_3,
    		R.drawable.gloomy_1, R.drawable.gloomy_2, R.drawable.gloomy_3,
    		R.drawable.tired_1, R.drawable.tired_2, R.drawable.tired_3,
    		R.drawable.sleepy_1, R.drawable.sleepy_2, R.drawable.sleepy_3,
    		R.drawable.serene_1, R.drawable.serene_2, R.drawable.serene_3};
    		
    
    private Random r;
    private Bitmap[] images;
    private Integer[] imageIds;
    private final int numImages = 16;
    //TODO
	@SuppressWarnings("unchecked")
	ArrayList<AsyncTask> tasks;

	public MoodAdapter(Context c) {
        mContext = c;
        r = new Random();
        imageIds = new Integer[numImages];
        images = new Bitmap[numImages];
        //TODO
        //tasks = new ArrayList<AsyncTask>();
    }

    public int getCount() {
        return numImages;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) { 	
    	ImageView imageView = null;
        
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(70, 70));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
            
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(emotions[3*position + r.nextInt(3)]);
    	
        return imageView;
    }
}
