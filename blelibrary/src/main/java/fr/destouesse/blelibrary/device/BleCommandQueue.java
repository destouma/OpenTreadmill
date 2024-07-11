package fr.destouesse.blelibrary.device;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;

import java.util.concurrent.ConcurrentLinkedQueue;

import fr.destouesse.blelibrary.constants.EBleCommandCode;

public final class BleCommandQueue {
    private static final String LOG_TAG = "BleCommandQueueNew";
    private ConcurrentLinkedQueue<BleCommand> queue;

    BleCommandQueue(){
        queue = new ConcurrentLinkedQueue<>();
    }

    public synchronized void addItem(EBleCommandCode command, BluetoothGattService service, BluetoothGattCharacteristic characteristic, BluetoothGattDescriptor descriptor, byte[] data){
        queue.add(new BleCommand(command,service,characteristic,descriptor,data,null));
    }

    public synchronized void addItemString(EBleCommandCode command, BluetoothGattService service, BluetoothGattCharacteristic characteristic, BluetoothGattDescriptor descriptor, String data){
        queue.add(new BleCommand(command,service,characteristic,descriptor,null,data));
    }

    public synchronized void pollQueue(BluetoothGatt gatt){
        BleCommand cmd = queue.poll();
        if(cmd != null){
            cmd.send(gatt);
        }
    }

}
