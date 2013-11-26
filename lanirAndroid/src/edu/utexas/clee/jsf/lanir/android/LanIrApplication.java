package edu.utexas.clee.jsf.lanir.android;

import android.app.Application;
import edu.utexas.clee.jsf.lanir.android.service.LanIrService;
import edu.utexas.clee.jsf.lanir.android.service.MockRemoteService;
import edu.utexas.clee.jsf.lanir.android.service.RemoteService;

public class LanIrApplication extends Application {
	
	public final static String EXTRA_REMOTE_NAME = "edu.utexas.clee.jsf.lanir.REMOTE_NAME";
	public final static String EXTRA_REMOTES_LIST = "edu.utexas.clee.jsf.lanir.REMOTES_LIST";

	public static RemoteService getRemoteService() {
		return new LanIrService();
//		return new MockRemoteService();
	}

}
