package edu.utexas.clee.jsf.lanir.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;
import edu.utexas.clee.jsf.lanir.android.LanIrApplication;
import edu.utexas.clee.jsf.lanir.android.R;
import edu.utexas.clee.jsf.lanir.android.model.Remote;
import edu.utexas.clee.jsf.lanir.android.service.RemoteService;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";

	private static final String serviceAddress = "192.168.1.114:1337";

	private ArrayList<Remote> remotes;
	
	private String getServiceAddress() {
		return serviceAddress;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		GetRemotesTask task = new GetRemotesTask();
		task.execute(getServiceAddress());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class GetRemotesTask extends AsyncTask<String, Void, ArrayList<Remote>> {

		@Override
		protected ArrayList<Remote> doInBackground(String... params) {
			return LanIrApplication.getRemoteService().getRemotes(params[0]);
		}

		@Override
		protected void onPostExecute(ArrayList<Remote> remotes) {
			LinearLayout ll = (LinearLayout) findViewById(R.id.layout_main);
			MainActivity.this.remotes = remotes;
			for (Remote remote : remotes) {
				Button button = new Button(MainActivity.this);
				//Button button = new Button(this, null, R.style.alphaPaginationStyle);
				LinearLayout.LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				button.setLayoutParams(params);
				button.setText(remote.getName());
				button.setOnClickListener(getOnClickListener(button));
				ll.addView(button);
			}
		}
	}

	View.OnClickListener getOnClickListener(final Button button)  {
	    return new View.OnClickListener() {
	        public void onClick(View v) {
	        	MainActivity.this.openRemote(button.getText().toString());
	        }
	    };
	}
	
	private void openRemote(String remoteName) {
        Toast.makeText(MainActivity.this, remoteName, Toast.LENGTH_SHORT).show();    
		Intent intent = new Intent(this, RemoteActivity.class);
		intent.putParcelableArrayListExtra(LanIrApplication.EXTRA_REMOTES_LIST, remotes); 
		intent.putExtra(LanIrApplication.EXTRA_REMOTE_NAME, remoteName);
		startActivity(intent);        
	}
	
}
