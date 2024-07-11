package fr.destouesse.blelibrary.pairing;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public final class BlePairing {
    private static final String LOG_TAG = "BlePairing";

    private BluetoothDevice device;
    private Context context;
    private IBlePairingCallbacks listener;
    private BroadcastReceiver pairReceiver;
    private boolean receiverRegisterd = false;

    public BlePairing(Context context,BluetoothDevice device, IBlePairingCallbacks listner) {
        this.context = context;
        this.device = device;
        this.listener = listner;
        this.receiverRegisterd = false;
    }

    @SuppressLint("MissingPermission")
    public boolean pair() {
        pairReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                    final int state 		= intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                    final int prevState	= intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                    if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                        listener.onBleDevicePaired(device);
                    } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
                        listener.onBleDeviceUnPaired(device);
                    }
                }
            }
        };

        IntentFilter intent = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        if(receiverRegisterd){
            context.unregisterReceiver(pairReceiver);
        }
        context.registerReceiver(pairReceiver, intent);
        receiverRegisterd = true;

        try {
            return device.createBond();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void stop(){
        if(receiverRegisterd){
            receiverRegisterd = false;
            context.unregisterReceiver(pairReceiver);
        }
    }

}

