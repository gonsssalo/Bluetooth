package pt.ipbeja.pdm1.bluetooth3;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivity(discoverableIntent);
    }


    public void btnScanForDevices(View view) {

        // Start this on a new activity without passing any data to it
        Intent intent = new Intent(this, FoundBTDevices.class);
        startActivity(intent);
    }
}
