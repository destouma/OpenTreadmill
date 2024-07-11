package fr.destouesse.blelibrary.device;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import fr.destouesse.blelibrary.constants.EBleCommandCode;

public final class BleCommand {
    private static final String LOG_TAG = "BleCommandNew";
    private EBleCommandCode command;
    private byte[] data;
    private String stringData;
    private BluetoothGattService service;
    private BluetoothGattCharacteristic characteristic;
    private BluetoothGattDescriptor descriptor;

    public BleCommand(EBleCommandCode command, BluetoothGattService service, BluetoothGattCharacteristic characteristic, BluetoothGattDescriptor descriptor, byte[] data, String stringData) {
        this.command = command;
        this.service = service;
        this.characteristic = characteristic;
        this.descriptor = descriptor;
        this.data = data;
        this.stringData = stringData;
    }

    public EBleCommandCode getCommand() {
        return command;
    }

    @SuppressLint("MissingPermission")
    public void send(BluetoothGatt gatt){
        if(gatt == null){
            return;
        }
        if(service == null){
            return;
        }
        if(characteristic == null){
            return;
        }
        switch(command){

            case BLE_COMMAND_READ_CHAR:
                gatt.readCharacteristic(characteristic);
                break;

            case BLE_COMMAND_READ_DESC:
                gatt.readDescriptor(descriptor);
                break;

            case BLE_COMMAND_WRITE_CHAR_BYTE:
                characteristic.setValue(data);
                gatt.writeCharacteristic(characteristic);
                break;

            case BLE_COMMAND_WRITE_CHAR_STRING:
                characteristic.setValue(stringData);
                gatt.writeCharacteristic(characteristic);
                break;

            case BLE_COMMAND_ENABLE_NOTIFICATION:
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                gatt.writeDescriptor(descriptor);
                gatt.setCharacteristicNotification(characteristic, true);
                break;

            case BLE_COMMAND_DISABLE_NOTIFICATION:
                descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                gatt.writeDescriptor(descriptor);
                gatt.setCharacteristicNotification(characteristic, false);
                break;

            case BLE_COMMAND_ENABLE_INDICATION:
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                gatt.writeDescriptor(descriptor);
                gatt.setCharacteristicNotification(characteristic, true);
                break;

            default:
                Log.d(LOG_TAG,"command not implemented :" + command);
        }

    }

}

