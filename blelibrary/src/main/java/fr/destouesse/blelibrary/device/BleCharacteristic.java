package fr.destouesse.blelibrary.device;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.HashMap;
import java.util.UUID;

public final class BleCharacteristic {
    private UUID uuid;
    private BluetoothGattCharacteristic bluetoothGattCharacteristic;
    private HashMap<UUID,BleDescriptor> bleDescriptorHashMap;

    BleCharacteristic(UUID uuid,BluetoothGattCharacteristic bluetoothGattCharacteristic){
        this.uuid = uuid;
        this.bluetoothGattCharacteristic = bluetoothGattCharacteristic;
        this.bleDescriptorHashMap = new HashMap<>();
    }

    public void addDescriptor(BleDescriptor descriptor){
        this.bleDescriptorHashMap.put(descriptor.getUuid(),descriptor);
    }

    public UUID getUuid() {
        return uuid;
    }

    public BluetoothGattCharacteristic getBluetoothGattCharacteristic() {
        return bluetoothGattCharacteristic;
    }

    public HashMap<UUID,BleDescriptor> getDescriptors() {
        return bleDescriptorHashMap;
    }
}
