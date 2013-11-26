package edu.utexas.clee.jsf.lanir.android.service;

import java.util.ArrayList;

import edu.utexas.clee.jsf.lanir.android.model.Remote;

public interface RemoteService {

	public ArrayList<Remote> getRemotes(String serviceAddress);

	public void sendCommand(String serviceAddress, String remote, String command);
}
