package me.morasquad.wicard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RecieveWiCardActivity extends AppCompatActivity {

    TextView connectionStatus;
    TextView fullNameText;
    TextView emailText;
    TextView addressText;
    TextView mobileNumText;
    Button saveWiCard, deleteWiCard;
    SqliteHelper sqliteHelper;
    String fullName, email, mobileNumber, address;

    WifiManager wifiManager;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;

    BroadcastReceiver mReciever;
    IntentFilter mIntentFilter;

    List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;

    static  final int MESSAGE_READ = 1;

    ServerClass serverClass;
    ClientClass clientClass;
    SendRecieve sendRecieve;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve_wi_card);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("WiCard : Receive WiCard");

        fullNameText = (TextView) findViewById(R.id.fullNameTxtRec);
        emailText = (TextView) findViewById(R.id.emailTxtRec);
        addressText = (TextView) findViewById(R.id.addressTxtRec);
        mobileNumText = (TextView) findViewById(R.id.mobileNumTxtRec);
        connectionStatus = (TextView) findViewById(R.id.connectonStatusRec);
        saveWiCard = (Button) findViewById(R.id.save_wicard);
        deleteWiCard = (Button) findViewById(R.id.delete_wicard);

        saveWiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = sqliteHelper.saveRecievedWiCard(email, address, mobileNumber, fullName);

                if(result){;
                    Toast.makeText(RecieveWiCardActivity.this, "Saved Successfully!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(RecieveWiCardActivity.this, "Failed to Saving!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        initialLoading();
        initialListner();
        discoverPeers();




    }
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_READ:
                    byte[] readBuff = (byte[]) msg.obj;
                    String tempMsg = new String(readBuff, 0,msg.arg1);
                    String[] parts = tempMsg.split("&");
                    fullName = parts[0];
                    email = parts[1];
                    address = parts[2];
                    mobileNumber = parts[3];
                    fullNameText.setText(fullName);
                    emailText.setText(email);
                    addressText.setText(address);
                    mobileNumText.setText(mobileNumber);
                    break;
            }
            return true;
        }
    });

    private void discoverPeers() {

        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                connectionStatus.setText("Discovery Started!");
            }

            @Override
            public void onFailure(int i) {
                connectionStatus.setText("Discovery Starting Failed!");
            }
        });
    }

    private void initialListner() {

        if(wifiManager.isWifiEnabled()){
            return;
        }else {
            wifiManager.setWifiEnabled(true);
        }


    }

    private void initialLoading() {

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);

        mReciever = new WifiDirectBroadcastRecieverforReciever(mManager, mChannel, this);
        mIntentFilter = new IntentFilter();

        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }



    WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            if(!peerList.getDeviceList().equals(peers)){
                peers.clear();
                peers.addAll(peerList.getDeviceList());

                deviceNameArray = new String[peerList.getDeviceList().size()];
                deviceArray = new WifiP2pDevice[peerList.getDeviceList().size()];

                int index = 0;

                for (WifiP2pDevice device : peerList.getDeviceList()){
                    deviceNameArray[index] = device.deviceName;
                    deviceArray[index] = device;
                    index++;
                }

            }

            if(peers.size() == 0){
                Toast.makeText(RecieveWiCardActivity.this, "No Devices Found!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    };

    WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            final InetAddress groupOwnerAddress = wifiP2pInfo.groupOwnerAddress;

            if(wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner){
                connectionStatus.setText("Sender");
                serverClass = new ServerClass();
                serverClass.start();
            }else if (wifiP2pInfo.groupFormed){
                connectionStatus.setText("Receiver");
                clientClass = new ClientClass(groupOwnerAddress);
                clientClass.start();
            }
        }
    };


    public boolean onOptionsItemSelected(MenuItem item) {
        int id  = item.getItemId();

        if(id == android.R.id.home){
            SendUsertoMainActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void SendUsertoMainActivity() {

        Intent home = new Intent(RecieveWiCardActivity.this, MainActivity.class);
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(home);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReciever, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReciever);
    }

    public class ServerClass extends Thread{
        Socket socket;
        ServerSocket serverSocket;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(8888);
                socket = serverSocket.accept();
                sendRecieve = new SendRecieve(socket);
                sendRecieve.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class ClientClass extends Thread{

        Socket socket;
        String hostAdd;

        public ClientClass(InetAddress hostAddress){

            hostAdd = hostAddress.getHostAddress();
            socket = new Socket();
        }

        @Override
        public void run() {

            try {
                socket.connect(new InetSocketAddress(hostAdd, 8888),500);

                sendRecieve = new SendRecieve(socket);
                sendRecieve.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SendRecieve extends Thread{

        private Socket socket;
        private InputStream inputStream;
        private OutputStream outputStream;

        public SendRecieve(Socket skt) throws IOException {
            socket = skt;
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();


        }

        @Override
        public void run() {

            byte[] buffer = new byte[1024];
            int bytes;

            while (socket != null){
                try {
                    bytes = inputStream.read(buffer);
                    if(bytes > 0){
                        handler.obtainMessage(MESSAGE_READ, bytes, -1,buffer).sendToTarget();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        public void write(byte[] bytes){
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
