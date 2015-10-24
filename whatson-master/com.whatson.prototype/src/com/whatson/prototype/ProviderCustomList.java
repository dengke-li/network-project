package com.whatson.prototype;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ArrayAdapter to custom the list of providers
 * 
 *
 */
public class ProviderCustomList extends ArrayAdapter<String>{

	private final Activity context;
	private final String[] providers;
	
	private final Integer[] providerId;
	
	public ProviderCustomList(Activity context,	String[] providers, Integer[] providerId) {
		super(context, R.layout.list_provider, providers);
		this.context = context;
		this.providers = providers;
		this.providerId = providerId;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView= inflater.inflate(R.layout.list_provider, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
		txtTitle.setText(providers[position]);
		int picto = R.drawable.newspaper0;
		switch(position){
		case 0: picto = R.drawable.newspaper0;
		break;
		case 1: picto = R.drawable.croche;
		break;
		case 2: picto = R.drawable.playpause;
		break;
		}
		
		imageView.setImageResource(picto);//providerId[position]);
		return rowView;
	}
}
