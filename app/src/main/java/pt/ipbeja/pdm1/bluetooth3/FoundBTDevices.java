package pt.ipbeja.pdm1.bluetooth3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class FoundBTDevices extends ListActivity {

   private FoundBTDevicesAdapter  foundBTDevicesAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<BluetoothObject> arrayOfFoundBTDevices;
    ArrayList<String> DevicesArrayList = new ArrayList<String>();
    ArrayList<Integer> RSSIArrayList = new ArrayList<Integer>();

    private Timer autoUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        displayListOfFoundDevices();

    }



    private void displayListOfFoundDevices() {

        arrayOfFoundBTDevices = new ArrayList<BluetoothObject>();

        // start looking for bluetooth devices
        mBluetoothAdapter.startDiscovery();

        // Discover new devices
        // Create a BroadcastReceiver for ACTION_FOUND
        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the bluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    // Get the "RSSI" to get the signal strength as integer,
                    // but should be displayed in "dBm" units
                    int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);

                    // Create the device object and add it to the arrayList of devices
                    BluetoothObject bluetoothObject = new BluetoothObject();
                    bluetoothObject.setBluetooth_name(device.getName());
                    bluetoothObject.setBluetooth_address(device.getAddress());
                    bluetoothObject.setBluetooth_rssi(rssi);


                  //  arrayOfFoundBTDevices.clear();
                   // arrayOfFoundBTDevices.add(bluetoothObject);
                    arrayOfFoundBTDevices.remove(bluetoothObject);
                    arrayOfFoundBTDevices.add(bluetoothObject);


                    DevicesArrayList.add(DevicesArrayList.size(),device.getName());
                    RSSIArrayList.add(RSSIArrayList.size(),rssi);

                        Toast.makeText(getApplicationContext(),"Devices N: " + DevicesArrayList.size() +  device.getName(), Toast.LENGTH_SHORT).show();


                    // 1. Pass context and data to the custom adapter
                    FoundBTDevicesAdapter adapter = new FoundBTDevicesAdapter(getApplicationContext(), arrayOfFoundBTDevices);
                    // 2. setListAdapter



                    setListAdapter(adapter);

               }

            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);


        ListView lv = getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long id)
            {

                int posição = position;


                Intent intent = new Intent(FoundBTDevices.this, TrakingActivity.class);
               //Send name and RSSI
                intent.putExtra("name", DevicesArrayList.get(posição));
                intent.putExtra("RSSI", RSSIArrayList.get(posição));

                startActivity(intent);
            }
        });


    }



    @Override
    protected void onPause()
    {
        super.onPause();

        mBluetoothAdapter.cancelDiscovery();

        Toast.makeText(getApplicationContext(), "onPause", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        autoUpdate = new Timer();
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        updateList();
                    }
                });
            }
        }, 0, 10000); // updates each 10 secs
    }

    private void updateList(){

        arrayOfFoundBTDevices.clear();
        Toast.makeText(this, "update", Toast.LENGTH_SHORT).show();

    }

}
