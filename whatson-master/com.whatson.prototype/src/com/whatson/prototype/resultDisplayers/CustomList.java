package com.whatson.prototype.resultDisplayers;

import com.whatson.prototype.Providers;
import com.whatson.prototype.R;
import com.whatson.prototype.R.id;
import com.whatson.prototype.R.layout;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ArrayAdapter to custom the list of matching results of a suggestion
 * 
 *
 */
public class CustomList extends ArrayAdapter<String>{

	private final Activity context;
	private final String[] web;
	private final Integer[] imageId;
	
	public CustomList(Activity context,
			String[] web, Integer[] imageId) {
		super(context, R.layout.list_single, web);
		this.context = context;
		this.web = web;
		this.imageId = imageId;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView= inflater.inflate(R.layout.list_single, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
		txtTitle.setText(web[position]);
		int picto = 0;
		switch(Providers.selectedProvider){
		case 0:picto = R.drawable.newspaper0;
		break;
		case 1:picto = R.drawable.croche;
		break;
		case 2:picto = R.drawable.playpause;
		break;
		}
		imageView.setImageResource(picto);
		return rowView;
	}
}
