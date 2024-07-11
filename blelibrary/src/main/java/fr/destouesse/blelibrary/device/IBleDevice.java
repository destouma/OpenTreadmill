package fr.destouesse.blelibrary.device;

import android.bluetooth.BluetoothDevice;

import java.util.UUID;

import fr.destouesse.blelibrary.constants.EBleCommandCode;

public interface IBleDevice {
    void registerCallbacks(IBleDeviceCallbacks callbacks);
    void setBluetoothDevice(BluetoothDevice device);
    void addCommand(EBleCommandCode command, UUID service, UUID characteristic, UUID descriptor);
    void addCommand(EBleCommandCode command, UUID service, UUID characteristic, UUID descriptor, byte[] data);
    void addCommand(EBleCommandCode command, UUID service, UUID characteristic, UUID descriptor, String stringData);
    void connect();
    void run();
    void disconnect();
}
