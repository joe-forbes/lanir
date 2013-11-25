package edu.utexas.clee.jsf.lanir.android;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void sendMessage(View view) {
		SendCommandTask task = new SendCommandTask();
		task.execute();

	}

	private class SendCommandTask extends
			AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			Log.d(TAG, "Fixing to send command to server.");

			HttpClient client = new DefaultHttpClient();
			String url = "http://192.168.1.114:1337/remotes/Yamaha_RAV302/MUTE";
			HttpPost post = new HttpPost(url);

			try {
				client.execute(post);
			} catch (ClientProtocolException e) {
				Log.e(TAG, "Exception sending command", e);
			} catch (IOException e) {
				Log.e(TAG, "Exception sending command", e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void arg) {
		}
	}

}
