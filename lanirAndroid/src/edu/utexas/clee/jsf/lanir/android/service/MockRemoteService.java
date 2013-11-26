package edu.utexas.clee.jsf.lanir.android.service;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import edu.utexas.clee.jsf.lanir.android.model.Remote;

public class MockRemoteService implements RemoteService {

	private static final String TAG = "MockRemoteService";

	private ArrayList<Remote> remotes = new ArrayList<Remote>();

	public MockRemoteService() {
		Remote r = new Remote();
		r.setName("Foo");
		List<String> c = new ArrayList<String>();
		c.add("Foo");
		c.add("Bar");
		r.setCommands(c);
		remotes.add(r);
		r = new Remote();
		r.setName("Spam");
		c = new ArrayList<String>();
		c.add("Spam");
		c.add("Eggs");
		remotes.add(r);
	}

	@Override
	public ArrayList<Remote> getRemotes(String serviceAddress) {
		return remotes;
	}

	@Override
	public void sendCommand(String serviceAddress, String remote, String command) {
		Log.i(TAG,
				String.format(
						"I am soooooo going to send %s a request for %s command on %s remote.",
						serviceAddress, command, remote));

	}

}
