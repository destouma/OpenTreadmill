package fr.destouesse.blelibrary.device;

import static fr.destouesse.blelibrary.constants.EBleDeviceErrorCode.BLE_DEVICE_ERROR_CHARACTERISTIC_READ_FAILED;
import static fr.destouesse.blelibrary.constants.EBleDeviceErrorCode.BLE_DEVICE_ERROR_CHARACTERISTIC_WRITE_FAILED;
import static fr.destouesse.blelibrary.constants.EBleDeviceErrorCode.BLE_DEVICE_ERROR_DESCRIPTOR_READ_FAILED;
import static fr.destouesse.blelibrary.constants.EBleDeviceErrorCode.BLE_DEVICE_ERROR_DESCRIPTOR_WRITE_FAILED;
import static fr.destouesse.blelibrary.constants.EBleDeviceErrorCode.BLE_DEVICE_ERROR_OTHER;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import fr.destouesse.blelibrary.constants.EBleCommandCode;

public class BleDevice implements IBleDevice {
    private static final String LOG_TAG = "BleDevice";

    private Context context;
    private static BluetoothGatt gatt;
    private IBleDeviceCallbacks callbacks;
    private BleCommandQueue commandQueue;
    private BluetoothDevice device;

    private HashMap<UUID,BleService> bleServices;

    public BleDevice(Context context){
        this.context = context;
        bleServices = new HashMap<>();
        commandQueue = new BleCommandQueue();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void connect() {
        if (gatt == null) {
            gatt = device.connectGatt(this.context, false, gattCallback,BluetoothDevice.TRANSPORT_LE);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void disconnect() {
        if (gatt != null) {
            gatt.disconnect();
            gatt.close();
            gatt = null;
        }
    }

    @Override
    public void registerCallbacks(IBleDeviceCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public void setBluetoothDevice(BluetoothDevice device) {
        this.device = device;
    }


    @Override
    public void addCommand(EBleCommandCode command, UUID service, UUID characteristic, UUID descriptor) {

        BleService bleService = service!=null ? bleServices.get(service) : null;
        BleCharacteristic bleCharacteristic = characteristic != null ? bleService.getCharacteristics().get(characteristic) : null;
        BleDescriptor bleDescriptor = descriptor != null ? bleCharacteristic.getDescriptors().get(descriptor) : null;

        commandQueue.addItem(
                command,
                bleService != null ? bleService.getBluetoothGattService():null,
                bleCharacteristic != null ? bleCharacteristic.getBluetoothGattCharacteristic():null,
                bleDescriptor != null ? bleDescriptor.getBluetoothGattDescriptor():null,
                null);
    }

    @Override
    public void addCommand(EBleCommandCode command,UUID service,UUID characteristic,UUID descriptor, byte[] data) {
        BleService bleService = bleServices.get(service);
        BleCharacteristic bleCharacteristic = bleService.getCharacteristics().get(characteristic);
        BleDescriptor bleDescriptor = bleCharacteristic.getDescriptors().get(descriptor);

        commandQueue.addItem(
                command,
                bleService != null ? bleService.getBluetoothGattService():null,
                bleCharacteristic != null ? bleCharacteristic.getBluetoothGattCharacteristic():null,
                bleDescriptor != null ? bleDescriptor.getBluetoothGattDescriptor():null,
                data);
    }

    @Override
    public void addCommand(EBleCommandCode command, UUID service, UUID characteristic, UUID descriptor, String stringData) {
        BleService bleService = bleServices.get(service);
        BleCharacteristic bleCharacteristic = bleService.getCharacteristics().get(characteristic);
        BleDescriptor bleDescriptor = bleCharacteristic.getDescriptors().get(descriptor);

        commandQueue.addItemString(
                command,
                bleService != null ? bleService.getBluetoothGattService():null,
                bleCharacteristic != null ? bleCharacteristic.getBluetoothGattCharacteristic():null,
                bleDescriptor != null ? bleDescriptor.getBluetoothGattDescriptor():null,
                stringData);
    }

    @Override
    public void run() {
        commandQueue.pollQueue(gatt);
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.d(LOG_TAG,"onConnectionStateChange:" + status + " " + newState);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.d(LOG_TAG,"STATE_CONNECTED");
                    callbacks.onBleDeviceConnected();
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.d(LOG_TAG,"STATE_DISCONNECTED");
//                    callbacks.onBleDeviceError(BLE_DEVICE_ERROR_DISCONNECTED);
                    callbacks.onBleDeviceDisconnected();
                    break;
                default:
                    Log.d(LOG_TAG,"OTHER STATE" + newState);
                    callbacks.onBleDeviceError(BLE_DEVICE_ERROR_OTHER);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            List<BluetoothGattService> services = gatt.getServices();

            for (BluetoothGattService service : services) {
                BleService bleService = new BleService(
                        service.getUuid(),
                        service);
                List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                for (BluetoothGattCharacteristic characteristic : characteristics) {
                    BleCharacteristic bleCharacteristic = new BleCharacteristic(
                            characteristic.getUuid(),
                            characteristic);
                    for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                        BleDescriptor bleDescriptor = new BleDescriptor(
                                descriptor.getUuid(),
                                descriptor
                        );
                        bleCharacteristic.addDescriptor(bleDescriptor);
                    }
                    bleService.addCharacteristic(bleCharacteristic);
                }
                bleServices.put(service.getUuid(),bleService);
            }
            callbacks.onBleDeviceServicesDiscovered(services);
            callbacks.onBleDeviceServicesDiscovered(bleServices);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt,characteristic);

            BleService bleService = bleServices.get(characteristic.getService().getUuid());
            BleCharacteristic bleCharacteristic = bleService.getCharacteristics().get(characteristic.getUuid());

            callbacks.onCharacteristicChanged(characteristic);
            callbacks.onCharacteristicChanged(bleCharacteristic);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                BleService bleService = bleServices.get(characteristic.getService().getUuid());
                BleCharacteristic bleCharacteristic = bleService.getCharacteristics().get(characteristic.getUuid());

                callbacks.onCharacteristicWrite(characteristic);
                callbacks.onCharacteristicWrite(bleCharacteristic);
            } else{
                callbacks.onBleDeviceError(BLE_DEVICE_ERROR_CHARACTERISTIC_WRITE_FAILED);
            }

            commandQueue.pollQueue(gatt);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                BleService bleService = bleServices.get(characteristic.getService().getUuid());
                BleCharacteristic bleCharacteristic = bleService.getCharacteristics().get(characteristic.getUuid());

                callbacks.onCharacteristicRead(characteristic);
                callbacks.onCharacteristicRead(bleCharacteristic);
            } else{
                callbacks.onBleDeviceError(BLE_DEVICE_ERROR_CHARACTERISTIC_READ_FAILED);
            }

            commandQueue.pollQueue(gatt);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt,descriptor,status);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                BleService bleService = bleServices.get(descriptor.getCharacteristic().getService().getUuid());
                BleCharacteristic bleCharacteristic = bleService.getCharacteristics().get(descriptor.getCharacteristic().getUuid());
                BleDescriptor bleDescriptor = bleCharacteristic.getDescriptors().get(descriptor.getUuid());

                callbacks.onDescriptorWrite(descriptor);
                callbacks.onDescriptorWrite(bleDescriptor);
            }else{
                callbacks.onBleDeviceError(BLE_DEVICE_ERROR_DESCRIPTOR_WRITE_FAILED);
            }

            commandQueue.pollQueue(gatt);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                BleService bleService = bleServices.get(descriptor.getCharacteristic().getService().getUuid());
                BleCharacteristic bleCharacteristic = bleService.getCharacteristics().get(descriptor.getCharacteristic().getUuid());
                BleDescriptor bleDescriptor = bleCharacteristic.getDescriptors().get(descriptor.getUuid());

                callbacks.onDescriptorRead(descriptor);
                callbacks.onDescriptorRead(bleDescriptor);
            }else{
                callbacks.onBleDeviceError(BLE_DEVICE_ERROR_DESCRIPTOR_READ_FAILED);
            }

            commandQueue.pollQueue(gatt);
        }
    };

}
