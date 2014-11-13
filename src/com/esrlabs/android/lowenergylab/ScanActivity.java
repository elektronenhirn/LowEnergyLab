package com.esrlabs.android.lowenergylab;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.esrlabs.android.lowenergylab.BluetoothLowEnergy.DeviceFoundCallback;
import com.esrlabs.android.lowenergylab.util.Bytes;

public class ScanActivity extends Activity {

	private ListView devicesList;
	private DevicesListAdapter devicesListAdapter = new DevicesListAdapter();
	private BluetoothLowEnergy bluetoothLE;

	public class DevicesListAdapter extends BaseAdapter {
		List<DiscoveredBluetoothDevice> devices = new ArrayList<DiscoveredBluetoothDevice>();

		final class ViewHolder {
			TextView deviceName;
			TextView deviceAddress;
			TextView deviceRSSI;
			TextView scanRecord;
		}

		public void addDevice(DiscoveredBluetoothDevice device) {
			if (!devices.contains(device)) {
				devices.add(device);
				notifyDataSetChanged();
			}
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			ViewHolder viewHolder;
			// General ListView optimization code.
			if (view == null) {
				view = ScanActivity.this.getLayoutInflater().inflate(R.layout.discovered_device, null);
				viewHolder = new ViewHolder();
				viewHolder.deviceAddress = (TextView) view.findViewById(R.id.discoveredDeviceAddress);
				viewHolder.deviceName = (TextView) view.findViewById(R.id.discoveredDeviceName);
				viewHolder.deviceRSSI = (TextView) view.findViewById(R.id.discoveredDeviceRSSI);
				viewHolder.scanRecord = (TextView) view.findViewById(R.id.discoveredDeviceScanRecord);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}

			DiscoveredBluetoothDevice device = devices.get(position);
			final String deviceName = device.getName();
			if (deviceName != null && deviceName.length() > 0)
				viewHolder.deviceName.setText(deviceName);
			else
				viewHolder.deviceName.setText(R.string.unknown_device);
			viewHolder.deviceAddress.setText(device.getAddress());
			viewHolder.deviceRSSI.setText(Long.valueOf(device.getRssi()).toString());
			viewHolder.scanRecord.setText(Bytes.bytesToHex(device.getScanRecord()));
			return view;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return devices.get(position);
		}

		@Override
		public int getCount() {
			return devices.size();
		}

		public void clear() {
			devices.clear();
			notifyDataSetChanged();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);
		bluetoothLE = new BluetoothLowEnergy(this);
		devicesList = (ListView) findViewById(R.id.deviceList);
		devicesList.setAdapter(devicesListAdapter);
	}

	@Override
	protected void onStart() {
		super.onStart();

		bluetoothLE.assureAdapterIsEnabled(this);

		startScanning();
	}

	private void startScanning() {
		devicesListAdapter.clear();

		bluetoothLE.startSearchingForDevices(new DeviceFoundCallback() {
			@Override
			public void onDeviceFound(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						devicesListAdapter.addDevice(new DiscoveredBluetoothDevice(device, rssi, scanRecord));
					}
				});
			}
		});

		invalidateOptionsMenu();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		if (!bluetoothLE.isScanning()) {
			menu.findItem(R.id.menu_stop).setVisible(false);
			menu.findItem(R.id.menu_scan).setVisible(true);
			menu.findItem(R.id.menu_refresh).setActionView(null);
		} else {
			menu.findItem(R.id.menu_stop).setVisible(true);
			menu.findItem(R.id.menu_scan).setVisible(false);
			menu.findItem(R.id.menu_refresh).setActionView(R.layout.actionbar_indeterminate_progress);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_scan:
			startScanning();
			break;
		case R.id.menu_stop:
			stopScanning();
			break;
		}
		return true;
	}

	@Override
	protected void onStop() {
		super.onStop();

		stopScanning();
	}

	private void stopScanning() {
		bluetoothLE.stopSearchingForDevices();
		invalidateOptionsMenu();
	}

}
