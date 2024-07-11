package fr.destouesse.blelibrary.device;

import android.bluetooth.BluetoothGattService;

import java.util.HashMap;
import java.util.UUID;

public final class BleService {
    private UUID uuid;
    private BluetoothGattService bluetoothGattService;
    private HashMap<UUID,BleCharacteristic> bleCharacteristicHashMap;

    BleService(UUID uuid,BluetoothGattService bluetoothGattService){
        this.uuid = uuid;
        this.bluetoothGattService = bluetoothGattService;
        this.bleCharacteristicHashMap = new HashMap<>();
    }

    public void addCharacteristic(BleCharacteristic characteristic){
        this.bleCharacteristicHashMap.put(characteristic.getUuid(),characteristic);
    }

    public UUID getUUID() {
        return uuid;
    }

    public HashMap<UUID,BleCharacteristic> getCharacteristics() {
        return bleCharacteristicHashMap;
    }

    public BluetoothGattService getBluetoothGattService() {
        return bluetoothGattService;
    }

}
