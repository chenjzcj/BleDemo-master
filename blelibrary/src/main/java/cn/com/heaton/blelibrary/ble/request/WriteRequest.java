package cn.com.heaton.blelibrary.ble.request;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Message;

import cn.com.heaton.blelibrary.ble.Ble;
import cn.com.heaton.blelibrary.ble.BleDevice;
import cn.com.heaton.blelibrary.ble.BleHandler;
import cn.com.heaton.blelibrary.ble.BleStates;
import cn.com.heaton.blelibrary.ble.BluetoothLeService;
import cn.com.heaton.blelibrary.ble.callback.BleWriteCallback;

/**
 * Created by LiuLei on 2017/10/23.
 */
@Implement(WriteRequest.class)
public class WriteRequest<T extends BleDevice> implements IMessage {

    private BleWriteCallback<T> mBleLisenter;

    protected WriteRequest() {
        BleHandler handler = BleHandler.getHandler();
        handler.setHandlerCallback(this);
    }

    public boolean write(T device, byte[] data, BleWriteCallback<T> lisenter) {
        this.mBleLisenter = lisenter;
        boolean result = false;
        BluetoothLeService service = Ble.getInstance().getBleService();
        if (Ble.getInstance() != null && service != null) {
            result = service.wirteCharacteristic(device.getBleAddress(), data);
        }
        return result;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case BleStates.BleStatus.Write:
                if (msg.obj instanceof BluetoothGattCharacteristic) {
                    BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) msg.obj;
                    mBleLisenter.onWriteSuccess(characteristic);
                }
                break;
            default:
                break;
        }
    }
}
