package com.esrlabs.android.lowenergylab;

import android.bluetooth.BluetoothDevice;

public class DiscoveredBluetoothDevice {
	final private BluetoothDevice bluetoothDevice;
	final private long rssi;
	final private byte[] scanRecord;

	public DiscoveredBluetoothDevice(BluetoothDevice bluetoothDevice, long rssi, byte[] scanRecord) {
		this.bluetoothDevice = bluetoothDevice;
		this.rssi = rssi;
		this.scanRecord = scanRecord;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bluetoothDevice == null) ? 0 : bluetoothDevice.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DiscoveredBluetoothDevice other = (DiscoveredBluetoothDevice) obj;
		if (bluetoothDevice == null) {
			if (other.bluetoothDevice != null)
				return false;
		} else if (!bluetoothDevice.equals(other.bluetoothDevice))
			return false;
		return true;
	}

	public BluetoothDevice getBluetoothDevice() {
		return bluetoothDevice;
	}

	public long getRssi() {
		return rssi;
	}

	public String getName() {
		return getBluetoothDevice().getName();
	}

	public String getAddress() {
		return getBluetoothDevice().getAddress();
	}

	public byte[] getScanRecord() {
		return scanRecord;
	}

}
