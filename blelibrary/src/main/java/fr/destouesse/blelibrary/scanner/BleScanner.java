package fr.destouesse.blelibrary.scanner;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

public final class BleScanner {
    private static final String LOG_TAG = "BleScanner";

    private Handler handler;
    private static BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager;
    private BluetoothLeScanner leScanner;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    private long scan_period;
    private IBleScannerCallbacks listener;

    public BleScanner(Context context, IBleScannerCallbacks listner) {
        scan_period = 10000;
        this.listener = listner;
        handler = new Handler();
        bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    public void setTimeout(long _time_millis) {
        scan_period = _time_millis;
    }

    @SuppressLint("MissingPermission")
    public void start() {
        leScanner = bluetoothAdapter.getBluetoothLeScanner();

        settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();

        filters = new ArrayList<>();

        handler.postDelayed(() -> {
            leScanner.stopScan(scanCallback);
            listener.onBleScannerScanFinished();
        }, scan_period);

        leScanner.startScan(filters, settings, scanCallback);
    }

    @SuppressLint("MissingPermission")
    public void stop() {
        if (leScanner != null) {
            handler.removeCallbacksAndMessages(null);
            leScanner.stopScan(scanCallback);
        }
    }

    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            listener.onBleScannerScanResult(result.getDevice(),result.getScanRecord(), result.getRssi());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
        }

        @Override
        public void onScanFailed(int errorCode) {
            listener.onBleScannerError(errorCode);
        }
    };

}

