package pt.ipbeja.pdm1.bluetooth3;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;


public class TrakingActivity extends AppCompatActivity {
    Long getLeastSignificantBits = UUID.randomUUID().getLeastSignificantBits();
    Long getMostSignificantBits = UUID.randomUUID().getMostSignificantBits();
    private final UUID uuid = new UUID(getMostSignificantBits,getLeastSignificantBits);
    private BluetoothAdapter bluetoothAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traking);



        TextView textViewname = (TextView) findViewById(R.id.TextViewNameDeviçe);
        TextView textViewRSSI = (TextView) findViewById(R.id.TextViewPower);

        Intent intent = getIntent();

        BluetoothDevice bluetoothDevice = getIntent().getExtras().getParcelable("btdevice");
        String name = null;
        if (bluetoothDevice != null) {
            name = bluetoothDevice.getName();
        }
        Integer RSSI = intent.getIntExtra("RSSI", 0);
        String Adress = null;
        if (bluetoothDevice != null) {
            Adress = bluetoothDevice.getAddress();
        }

        textViewname.setText("Dispositivo: " + name);
        textViewRSSI.setText("RSSI: " + RSSI.toString() + "dbm");


    }

    public void BtnTraking(View view) {

        BluetoothDevice bluetoothDevice = getIntent().getExtras().getParcelable("btdevice");

        // Initiate a connection request in a separate thread
        TrakingActivity.ConnectingThread t = new TrakingActivity.ConnectingThread(bluetoothDevice);
        t.start();
    }


    private class ConnectingThread extends Thread {
        private final BluetoothSocket bluetoothSocket;
        private final BluetoothDevice bluetoothDevice;

        ConnectingThread(BluetoothDevice device) {
            Toast.makeText(TrakingActivity.this, "ConnectingThread", Toast.LENGTH_SHORT).show();
            BluetoothSocket temp = null;
            bluetoothDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                Toast.makeText(TrakingActivity.this, "ConnectingThreadTry", Toast.LENGTH_SHORT).show();
                temp = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(TrakingActivity.this, "ConnectingThreadcatch", Toast.LENGTH_SHORT).show();
            }
            bluetoothSocket = temp;
        }

        public void run() {


            try {
                // This will block until it succeeds in connecting to the device
                // through the bluetoothSocket or throws an exception
                bluetoothSocket.connect();
            } catch (IOException connectException) {
                connectException.printStackTrace();
                try {
                    bluetoothSocket.close();
                } catch (IOException closeException) {
                    closeException.printStackTrace();
                }
            }

            // Code to manage the connection in a separate thread

              // manageBluetoothConnection(bluetoothSocket);

        }

        // Cancel an open connection and terminate the thread
        public void cancel() {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}










