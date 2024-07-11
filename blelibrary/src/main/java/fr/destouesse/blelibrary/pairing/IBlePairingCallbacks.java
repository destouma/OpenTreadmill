package fr.destouesse.blelibrary.pairing;

import android.bluetooth.BluetoothDevice;

public interface IBlePairingCallbacks {
    void onBleDevicePaired(BluetoothDevice device);
    void onBleDeviceUnPaired(BluetoothDevice device);
}
