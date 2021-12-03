package com.mazenetsolutions.mzs119.skst_new.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

public class ConnectionDetector {

	private Context context;

	public ConnectionDetector(Context context) {
		this.context = context;
	}

	public boolean isConnectedToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null)
					for (int i = 0; i < 1; i++)
						if (info.getState() == NetworkInfo.State.CONNECTED) {
							return true;
						}
		}
		return false;
	}

	public boolean isConnected(Context applicationContext) {


			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

				Network[] activeNetworks = cm.getAllNetworks();
				for (Network n: activeNetworks) {
					NetworkInfo nInfo = cm.getNetworkInfo(n);
					if(nInfo.isConnected())
						return true;
				}

			} else {
				NetworkInfo[] info = cm.getAllNetworkInfo();
				if (info != null)
					for (NetworkInfo anInfo : info)
						if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
							return true;
						}
			}

			return false;



	}
}