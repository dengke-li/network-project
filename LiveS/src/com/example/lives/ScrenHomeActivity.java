package com.example.lives;



import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

public class ScrenHomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_scren_home);
	}

	public void searchLive(View v) {
		Intent intent = new Intent(ScrenHomeActivity.this, SearchLiveActivity.class);
		ScrenHomeActivity.this.startActivity(intent);
	}

	public void seeMylives(View v) {
		Intent intent = new Intent(ScrenHomeActivity.this,SeeLiveActivity.class);
		ScrenHomeActivity.this.startActivity(intent);
	}
	
	public void addlive(View v) {
		Intent intent = new Intent(ScrenHomeActivity.this, AddLiveActivity.class);
		ScrenHomeActivity.this.startActivity(intent);
	}



	

	public void buttonExit(View v) {
		dialogExit(ScrenHomeActivity.this);
	}

	public void buttonConfiguration(View v) {
		dialogReturn(ScrenHomeActivity.this);
	}
	


	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (((keyCode == KeyEvent.KEYCODE_BACK) || (keyCode == KeyEvent.KEYCODE_HOME))
				&& event.getRepeatCount() == 0) {
			dialogExit(ScrenHomeActivity.this);
		}
		return false;

		// end onKeyDown
	}

	public static void dialogExit(Context context) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("Voulez vous quittez?");
		builder.setTitle("Confirmer");
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setPositiveButton("Quitter",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						android.os.Process.killProcess(android.os.Process
								.myPid());
					}
				});

		builder.setNegativeButton("Annuler",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builder.create().show();
	}
	public void dialogReturn(Context context) {
		LayoutInflater factory = LayoutInflater.from(this);
		final View alertDialogView = factory.inflate(R.layout.main21, null);
		AlertDialog.Builder builder = new Builder(this);
		builder.setView(alertDialogView);		
		builder.setTitle("Mode de fonctionement");
		builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Getting Array of Contacts
				dialog.dismiss();

			}
		});

		builder.create().show();
	}

}

