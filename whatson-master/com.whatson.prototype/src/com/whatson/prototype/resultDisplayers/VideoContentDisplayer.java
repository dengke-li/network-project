package com.whatson.prototype.resultDisplayers;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.whatson.prototype.ActivityModel;
import com.whatson.prototype.Providers;
import com.whatson.prototype.R;
import com.whatson.prototype.resultTypes.TextContent;
import com.whatson.prototype.serverCalls.SimpleHttpClient;

public class VideoContentDisplayer extends ActivityModel{

	private TextView caption;
	// Declare variables
	ProgressDialog pDialog;
	VideoView videoview;

	// Insert your Video URL
	String VideoURL;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the layout from video_main.xml
		setContentView(R.layout.video_content);
		caption = (TextView) findViewById(R.id.caption);
		// Find your VideoView in your video_main.xml layout
		videoview = (VideoView) findViewById(R.id.VideoView);

		TextContent tc = getTextContentInfos();
		loadVideo(tc);




	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	private void loadVideo(TextContent tc) {
		caption.setText(tc.getTitle()+"\n"+tc.getAuthor()+" ("+tc.getSource()+")");

		// Execute StreamVideo AsyncTask
		String folder = null;
		switch(Providers.selectedProvider){
		case 1: folder = "musics/";
		break;
		case 2: folder = "videos/";
		break;
		}
		VideoURL = SimpleHttpClient.SERVERADDRESS+folder+tc.getUrl();
		
		// Create a progressbar
		pDialog = new ProgressDialog(VideoContentDisplayer.this);
		// Set progressbar title
		pDialog.setTitle("Android Video Streaming Tutorial");
		// Set progressbar message
		pDialog.setMessage("Buffering...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		// Show progressbar
		pDialog.show();

		try {
			// Start the MediaController
			MediaController mediacontroller = new MediaController(
					VideoContentDisplayer.this);
			mediacontroller.setAnchorView(videoview);
			// Get the URL from String VideoURL
			Uri video = Uri.parse(VideoURL);
			videoview.setMediaController(mediacontroller);
			videoview.setVideoURI(video);

		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}

		videoview.requestFocus();
		videoview.setOnPreparedListener(new OnPreparedListener() {
			// Close the progress bar and play the video
			public void onPrepared(MediaPlayer mp) {
				pDialog.dismiss();
				videoview.start();
			}
		});
	}

	private TextContent getTextContentInfos(){
		int index = getExternalContentId();
		Log.d("pos", index+" "+ResultListDisplayer.contents.size());
		if(index==0)
			return null;
		return (TextContent) ResultListDisplayer.contents.get(index-1);
	}
	private int getExternalContentId(){
		Bundle extras = getIntent().getExtras();
		if(extras == null)
			return 0;
		return extras.getInt(ResultListDisplayer.CONTENT);
	}	
}
