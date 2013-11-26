package edu.utexas.clee.jsf.lanir.android.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.google.gson.Gson;

import edu.utexas.clee.jsf.lanir.android.model.Remote;

public class LanIrService implements RemoteService {

	private static final String TAG = "LanIrService";

	public ArrayList<Remote> getRemotes(String serviceAddress) {
		Log.d(TAG, "Fixing to request remotes from server.");
		String url = String.format("http://%s/", serviceAddress);
		DefaultHttpClient client = new DefaultHttpClient();

		HttpGet getRequest = new HttpGet(url);

		ArrayList<Remote> result = null;

		try {

			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				Log.w(TAG, "Error " + statusCode + " for URL " + url);
				return null;
			}

			HttpEntity getResponseEntity = getResponse.getEntity();

			InputStream source = getResponseEntity.getContent();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					source));
			StringBuilder builder = new StringBuilder();
			try {
				for (String line = null; (line = reader.readLine()) != null;) {
					builder.append(line).append("\n");
				}
			} catch (IOException e) {
				Log.w(TAG, "Error reading url: " + url, e);
			}

			String json = builder.toString();
			Gson gson = new Gson();
			GetRemotesResponse getRemotesResponse = (GetRemotesResponse) gson.fromJson(json, GetRemotesResponse.class);
			result = getRemotesResponse.remotes;
		} catch (IOException e) {
			getRequest.abort();
			Log.w(TAG, "Error for URL " + url, e);
		}
		return result;
	}
	
	private class GetRemotesResponse {
		public ArrayList<Remote> remotes;
	}
	
	public void sendCommand(String serviceAddress, String remote, String command) {
		Log.d(TAG, "Fixing to send command to server.");

		HttpClient client = new DefaultHttpClient();
		String url = String.format("http://%s/remotes/%s/%s", serviceAddress, remote, command);
		HttpPost post = new HttpPost(url);

		try {
			client.execute(post);
		} catch (ClientProtocolException e) {
			Log.e(TAG, "Exception sending command", e);
		} catch (IOException e) {
			Log.e(TAG, "Exception sending command", e);
		}
	}
	
}
