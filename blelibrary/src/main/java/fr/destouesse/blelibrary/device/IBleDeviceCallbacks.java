package fr.destouesse.blelibrary.device;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import fr.destouesse.blelibrary.constants.EBleDeviceErrorCode;

public interface IBleDeviceCallbacks {
    // BLE connect/disconnect
    void onBleDeviceConnected();
    void onBleDeviceDisconnected();
    // BLE services callbacks
    void onBleDeviceServicesDiscovered(List<BluetoothGattService> services);
    void onBleDeviceServicesDiscovered(HashMap<UUID, BleService> services);

    // BLE characteristics callbacks
    void onCharacteristicRead(BluetoothGattCharacteristic characteristic);
    void onCharacteristicRead(BleCharacteristic characteristic);
    void onCharacteristicWrite(BluetoothGattCharacteristic characteristic);
    void onCharacteristicWrite(BleCharacteristic characteristic);
    void onCharacteristicChanged(BluetoothGattCharacteristic characteristic);
    void onCharacteristicChanged(BleCharacteristic characteristic);

    // BLE descriptors callbacks
    void onDescriptorRead(BluetoothGattDescriptor descriptor);
    void onDescriptorRead(BleDescriptor descriptor);
    void onDescriptorWrite(BluetoothGattDescriptor descriptor);
    void onDescriptorWrite(BleDescriptor descriptor);

    // BLE error callbacks
    void onBleDeviceError(EBleDeviceErrorCode errorCode);

}
