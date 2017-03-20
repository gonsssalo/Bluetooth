package pt.ipbeja.pdm1.bluetooth3;

import android.content.Context;
import android.content.Intent;
import android.os.ParcelUuid;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;



public class FoundBTDevicesAdapter extends ArrayAdapter<BluetoothObject> {

    private Context context;
    private ArrayList<BluetoothObject> arrayFoundDevices;

    public FoundBTDevicesAdapter(Context context, ArrayList<BluetoothObject> arrayOfAlreadyPairedDevices)
    {
        super(context, R.layout.activity_found_btdevices_adapter, arrayOfAlreadyPairedDevices);

        this.context = context;
        this.arrayFoundDevices = arrayOfAlreadyPairedDevices;
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent)
    {

        BluetoothObject bluetoothObject = arrayFoundDevices.get(position);


        // 1. Create Inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.activity_found_btdevices_adapter, parent, false);

        // 3. Get the widgets from the rowView
        TextView bt_name = (TextView) rowView.findViewById(R.id.textview_bt_scan_name);
        TextView bt_address = (TextView) rowView.findViewById(R.id.textview_bt_scan_address);
        TextView bt_signal_strength = (TextView) rowView.findViewById(R.id.textview_bt_scan_signal_strength);
        TextView bt_scan_uuid = (TextView) rowView.findViewById(R.id.textview_bt_scan_uuid);



        // 4. Set the text for each widget
        bt_name.setText(bluetoothObject.getBluetooth_name());
        bt_address.setText("address: " + bluetoothObject.getBluetooth_address());
        bt_signal_strength.setText("RSSI: " + bluetoothObject.getBluetooth_rssi() + "dbm");

        ParcelUuid uuid[] = bluetoothObject.getBluetooth_uuids();
        if (uuid != null)
            bt_scan_uuid.setText("uuid: " + uuid[0]);


        // 5. return rowView
        return rowView;

    }//end getView()





}//end class AlreadyPairedAdapter

