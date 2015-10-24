package com.whatson.prototype.resultDisplayers;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.whatson.prototype.ActivityModel;
import com.whatson.prototype.R;
import com.whatson.prototype.resultTypes.TextContent;

/**
 * Activity dedicated to display a text result
 * 
 *
 */
public class TextContentDisplayer extends ActivityModel{

	private TextView caption;
	private WebView navig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_content);
		caption = (TextView) findViewById(R.id.caption);

		//get the text content infos from the previous list of contents that matches the user emotions
		TextContent tc = getTextContentInfos();

		Log.d("display text", tc.getUrl()+" "+tc.getTitle());
		
		//load the article
		loadWebPage(tc);

	}

	/**
	 * Treat notation action on the content
	 * @param v
	 */
	public void fillStar(View v) {

		if(v.getId()==R.id.star1 || v.getId()==R.id.star2 || v.getId()==R.id.star3 || v.getId()==R.id.star4 || v.getId()==R.id.star5){
			ImageView img = (ImageView) findViewById(v.getId());
			Log.d("image tag",img.getTag()+"");
			fill(Integer.valueOf((String) img.getTag()));
		}
	}


	private void fill(int j) {
		ImageView img = null;
		for(int i=1;i<=j;i++){
			switch(i){
			case 5:img = (ImageView) findViewById(R.id.star5);break;
			case 4:img = (ImageView) findViewById(R.id.star4);break;
			case 3:img = (ImageView) findViewById(R.id.star3);break;
			case 2:img = (ImageView) findViewById(R.id.star2);break;
			case 1:img = (ImageView) findViewById(R.id.star1);break;

			}
			img.setImageDrawable(getResources().getDrawable(R.drawable.star_full));
		}
		for(int i=(j+1);i<=5;i++){
			switch(i){
			case 5:img = (ImageView) findViewById(R.id.star5);break;
			case 4:img = (ImageView) findViewById(R.id.star4);break;
			case 3:img = (ImageView) findViewById(R.id.star3);break;
			case 2:img = (ImageView) findViewById(R.id.star2);break;
			case 1:img = (ImageView) findViewById(R.id.star1);break;

			}
			img.setImageDrawable(getResources().getDrawable(R.drawable.star));
		}
		//save in database
		saveResultNotation(getExternalContentId(),j);

	}

	/**
	 * Save the notation of this content (REST call or simpler)
	 * @param externalContentId
	 * @param j
	 */
	private void saveResultNotation(int externalContentId, int j) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Load web content article in the webview
	 */
	private void loadWebPage(TextContent tc) {
		caption.setText(tc.getTitle()+"\n"+tc.getAuthor()+" ("+tc.getSource()+")");
		navig = (WebView)findViewById(R.id.webkit);
		navig.loadUrl(tc.getUrl());
		navig.getSettings().setJavaScriptEnabled(true);		
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
