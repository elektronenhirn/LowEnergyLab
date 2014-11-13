package com.esrlabs.android.lowenergylab;

import android.app.Activity;
import android.os.Bundle;

public class ScanActivity extends Activity {

	private BluetoothLowEnergy bluetoothLE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);

		bluetoothLE = new BluetoothLowEnergy(this);
	}

	@Override
	protected void onStart() {
		super.onStart();

		bluetoothLE.assureAdapterIsEnabled(this);

		bluetoothLE.startSearchingForDevices();
	}

	@Override
	protected void onStop() {
		super.onStop();

		bluetoothLE.stopSearchingForDevices();
	}

}
