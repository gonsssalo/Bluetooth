package pt.ipbeja.pdm1.bluetooth3;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;



public class FoundBTDevices extends ListActivity {



    public BluetoothAdapter mBluetoothAdapter;
    private ArrayList<BluetoothObject> arrayOfFoundBTDevices;
    //ArrayList of device names
    ArrayList<String> DevicesNamesArrayList = new ArrayList<>();
    //ArrayList of RSSI
    ArrayList<Integer> RSSIArrayList = new ArrayList<>();
    //ArrayList of MacAdress
    ArrayList<String> MacAdressArrayList = new ArrayList<>();
    //ArrayList of Devices
    ArrayList<BluetoothDevice> DevicesArrayList = new ArrayList<>();

    String uuidS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        displayListOfFoundDevices();


    }

    private void displayListOfFoundDevices() {


        arrayOfFoundBTDevices = new ArrayList<>();

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
                    bluetoothObject.setBluetooth_uuids(device.getUuids());
                        bluetoothObject.setBluetooth_rssi(rssi);



                       // arrayOfFoundBTDevices.remove(bluetoothObject);
                        arrayOfFoundBTDevices.add(bluetoothObject);

                        // Add device information in Arrays
                    DevicesArrayList.add(DevicesArrayList.size(),device);
                        DevicesNamesArrayList.add(DevicesNamesArrayList.size(), device.getName());
                        RSSIArrayList.add(RSSIArrayList.size(), rssi);
                        MacAdressArrayList.add(MacAdressArrayList.size(), device.getAddress());
device.getUuids();

                        // 1. Pass context and data to the custom adapter
                        FoundBTDevicesAdapter adapter = new FoundBTDevicesAdapter(getApplicationContext(), arrayOfFoundBTDevices);
                        // 2. setListAdapter

                        setListAdapter(adapter);
                    }

               }

        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        // get the ListView
        ListView lv = getListView();
        // Responds to the click of a list element
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long id)
            {


                Intent intent = new Intent(FoundBTDevices.this, TrakingActivity.class);




                BluetoothDevice selectedDevice = DevicesArrayList.get(position);




                //Send device and RSSI
                intent.putExtra("btdevice", selectedDevice);
                intent.putExtra("RSSI", RSSIArrayList.get(position));


               startActivity(intent);

            }
        });

    }



    @Override
    protected void onPause()
    {
        super.onPause();
        //stop discovery
        mBluetoothAdapter.cancelDiscovery();
       // unregisterReceiver(mreceiver);
        Toast.makeText(getApplicationContext(), "onPause", Toast.LENGTH_SHORT).show();
    }


    }







