package com.example.demochat07062022;

import androidx.appcompat.app.AppCompatActivity;
import io.socket.client.IO;
import io.socket.client.Socket;
import android.os.Bundle;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.1.3:3000");
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSocket.connect();
    }
}
