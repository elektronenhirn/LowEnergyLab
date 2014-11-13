package com.esrlabs.android.lowenergylab;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

public class BluetoothLowEnergy {
	private static final int REQUEST_ENABLE_BT = 1;
	private BluetoothAdapter bluetoothAdapter;
	private final String TAG = "BluetoothLE";
	private LeScanCallback internaldeviceFoundCallback = new LeScanCallback() {
		@Override
		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
			Log.d(TAG, "device found : " + device.getName() + " [" + device.getAddress() + "]");
			if (externaldeviceFoundCallback != null) {
				externaldeviceFoundCallback.onDeviceFound(device, rssi, scanRecord);
			}
		}
	};

	public interface DeviceFoundCallback {
		void onDeviceFound(BluetoothDevice device, int rssi, byte[] scanRecord);
	};

	DeviceFoundCallback externaldeviceFoundCallback;

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

		// determine whether BLE is supported on the device.
		if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(context, "BT Low Energy not supported", Toast.LENGTH_SHORT).show();
		}
	}

	public void startSearchingForDevices(DeviceFoundCallback deviceFoundCallback) {
		this.externaldeviceFoundCallback = deviceFoundCallback;
		boolean result = bluetoothAdapter.startLeScan(internaldeviceFoundCallback);
		if (!result) {
			Log.e(TAG, "Bluetooth LE not supported on device ");
			return;
		}
		Log.d(TAG, "Started searching for BT LE devices ");
	}

	public void stopSearchingForDevices() {
		Log.d(TAG, "Stop searching for BT LE devices");
		bluetoothAdapter.stopLeScan(internaldeviceFoundCallback);
		externaldeviceFoundCallback = null;
	}

	public boolean isScanning() {
		return externaldeviceFoundCallback != null;
	}
}
