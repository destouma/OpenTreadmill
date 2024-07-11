package fr.destouesse.blelibrary.scanner;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanRecord;

public interface IBleScannerCallbacks {
    void onBleScannerScanResult(BluetoothDevice btDevice, ScanRecord record, int rssi);
    void onBleScannerScanFinished();
    void onBleScannerError(int errorCode);
}
