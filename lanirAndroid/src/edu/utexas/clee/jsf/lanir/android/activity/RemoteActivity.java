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
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import edu.utexas.clee.jsf.lanir.android.LanIrApplication;
import edu.utexas.clee.jsf.lanir.android.R;
import edu.utexas.clee.jsf.lanir.android.model.Remote;
import edu.utexas.clee.jsf.lanir.android.service.RemoteService;

public class RemoteActivity extends Activity {

	private static final String TAG = "RemoteActivity";

	private static final String serviceAddress = "192.168.1.114:1337";

	private ArrayList<Remote> remotes;
	private String remoteName;
	
	private String getServiceAddress() {
		return serviceAddress;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent intent = getIntent();
		remotes = intent.getParcelableArrayListExtra(LanIrApplication.EXTRA_REMOTES_LIST);
		remoteName = intent.getStringExtra(LanIrApplication.EXTRA_REMOTE_NAME);
		for (Remote r : remotes) {
			if (r.getName().equals(remoteName)) {
				LinearLayout ll = (LinearLayout) findViewById(R.id.layout_main);
				List<String> commands = r.getCommands();
				for (String command : commands) {
					Button button = new Button(this);
					LinearLayout.LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					button.setLayoutParams(params);
					button.setText(command);
					button.setOnClickListener(getOnClickListener(button));
					ll.addView(button);
				}
				
				break;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	View.OnClickListener getOnClickListener(final Button button)  {
	    return new View.OnClickListener() {
	        public void onClick(View v) {
	        	RemoteActivity.this.sendCommand(button.getText().toString());
	        }
	    };
	}
	
	private void sendCommand(String commandName) {
		SendCommandTask task = new SendCommandTask();
		task.execute(getServiceAddress(), remoteName, commandName);
	}
	
	private class SendCommandTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			RemoteService service = LanIrApplication.getRemoteService();
			List<Remote> remotes = service.getRemotes(params[0]);
			service.sendCommand(params[0], params[1], params[2]);
			return null;
		}

		@Override
		protected void onPostExecute(Void arg) {
		}
	}

}
