package com.esrlabs.android.lowenergylab;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BluetoothLowEnergy {
	private static final int REQUEST_ENABLE_BT = 1;
	private BluetoothAdapter bluetoothAdapter;
	private final String TAG = "BluetoothLE";
	private LeScanCallback deviceFoundCallback = new LeScanCallback() {
		@Override
		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
			Log.d(TAG, "device found : " + device.getAddress());
		}
	};

	public BluetoothLowEnergy(Context context) {
		// Initializes Bluetooth adapter.
		final BluetoothManager bluetoothManager = (BluetoothManager) context
				.getSystemService(Context.BLUETOOTH_SERVICE);
		bluetoothAdapter = bluetoothManager.getAdapter();
	}

	public void assureAdapterIsEnabled(Activity context) {
		// Ensures Bluetooth is available on the device and it is enabled. If
		// not,
		// displays a dialog requesting user permission to enable Bluetooth.
		if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			context.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
	}

	public void startSearchingForDevices() {
		boolean result = bluetoothAdapter.startLeScan(deviceFoundCallback);
		Log.d(TAG, "Started searching for BT LE devices " + result);
	}

	public void stopSearchingForDevices() {
		Log.d(TAG, "Stop searching for BT LE devices");
		bluetoothAdapter.stopLeScan(deviceFoundCallback);
	}
}
