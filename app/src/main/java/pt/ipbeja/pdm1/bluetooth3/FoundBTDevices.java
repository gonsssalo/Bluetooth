package pt.ipbeja.pdm1.bluetooth3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class FoundBTDevices extends ListActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<BluetoothObject> arrayOfFoundBTDevices;

    private int m_interval = 10000; // 10 seconds
    private Handler m_handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();



        displayListOfFoundDevices();

        m_handler = new Handler();
       //startRepeatingTask();
    }


    Runnable m_statusChecker = new Runnable()
    {
        @Override
        public void run() {


            displayListOfFoundDevices();


            Toast.makeText(getApplicationContext(), "reapeting", Toast.LENGTH_SHORT).show();
            m_handler.postDelayed(m_statusChecker, m_interval);
        }
    };

    public void startRepeatingTask()
    {
        m_statusChecker.run();
    }

    public void stopRepeatingTask()
    {
        m_handler.removeCallbacks(m_statusChecker);
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

                    // 1. Pass context and data to the custom adapter
                    FoundBTDevicesAdapter adapter = new FoundBTDevicesAdapter(getApplicationContext(), arrayOfFoundBTDevices);
                    // 2. setListAdapter



                    setListAdapter(adapter);

                    Toast.makeText(getApplicationContext(), "displayListOfFoundDevices", Toast.LENGTH_SHORT).show();



               }


            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        //arrayOfFoundBTDevices = new ArrayList<BluetoothObject>();
        ListView lv = getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long id)
            {



                // Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row
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
    protected void onDestroy ()
   {

       super.onDestroy();
       stopRepeatingTask();
       Toast.makeText(getApplicationContext(), "onDestroy", Toast.LENGTH_SHORT).show();
   }


    @Override
    protected void onStop ()
    {

        super.onStop();
        Toast.makeText(getApplicationContext(), "onStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume ()
    {

        super.onResume();
        Toast.makeText(getApplicationContext(), "onResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart ()
    {

        super.onRestart();
        Toast.makeText(getApplicationContext(), "onRestart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart ()
    {

        super.onStart();
        Toast.makeText(getApplicationContext(), "onStart", Toast.LENGTH_SHORT).show();
    }

}
