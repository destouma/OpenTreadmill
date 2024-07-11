package fr.destouesse.blelibrary.device;

import android.bluetooth.BluetoothGattDescriptor;

import java.util.UUID;

public final class BleDescriptor {
    private UUID uuid;
    private BluetoothGattDescriptor bluetoothGattDescriptor;

    BleDescriptor(UUID uuid,BluetoothGattDescriptor bluetoothGattDescriptor){
        this.uuid = uuid;
        this.bluetoothGattDescriptor = bluetoothGattDescriptor;
    }

    public UUID getUuid() {
        return uuid;
    }

    public BluetoothGattDescriptor getBluetoothGattDescriptor() {
        return bluetoothGattDescriptor;
    }
}
