package fr.destouesse.blelibrary.device;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

public class BleDeviceBuilder {
    private final IBleDevice bleDevice;

    public BleDeviceBuilder(Context context) {
        bleDevice = new BleDevice(context);
    }

    public BleDeviceBuilder registerCallbacks(IBleDeviceCallbacks callbacks) {
        bleDevice.registerCallbacks(callbacks);
        return this;
    }

    public BleDeviceBuilder setBluetoothDevice(BluetoothDevice device){
        bleDevice.setBluetoothDevice(device);
        return this;
    }

    public IBleDevice build(){
        return bleDevice;
    }
}
