package com.example.demochat07062022;

import androidx.appcompat.app.AppCompatActivity;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.1.3:3000");
        } catch (URISyntaxException e) {}
    }

    EditText editText;
    Button btnChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.edittext_message);
        btnChat = findViewById(R.id.button_chat);

        mSocket.connect();
        mSocket.on("alert", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String alert = data.getString("alert");
                    if (!alert.isEmpty()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, alert, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        mSocket.on("sign-in-success", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String alert = data.getString("alert");
                    String token = data.getString("token");
                    SharedPreferences sharedPreferences = getSharedPreferences("appcache", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", token);
                    editor.commit();
                    if (!alert.isEmpty()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, alert, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mSocket.on("join-chat-rom-success", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String alert = data.getString("alert");
                    if (!alert.isEmpty()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, alert, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mSocket.on("chat-success", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String alert = data.getString("alert");
                    JSONObject jsonObject = data.getJSONObject("message");
                    String content = jsonObject.getString("content");
                    if (!alert.isEmpty()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

//        signUp(new User("demo2@gmail.com", "1234567891"));
//        signIn(new User("demo1@gmail.com", "123456789"));
//        joinChatRoom();
//        chat("Xin chào");

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editText.getText().toString();
                if (!message.isEmpty()) {
                    chat(message);
                }
            }
        });
    }
    private void signUp(User user) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", user.getEmail());
            jsonObject.put("password", user.getPassword());
            mSocket.emit("sign-up", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void signIn(User user) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", user.getEmail());
            jsonObject.put("password", user.getPassword());
            mSocket.emit("sign-in", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void joinChatRoom() {
        SharedPreferences sharedPreferences = getSharedPreferences("appcache", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        if (!token.isEmpty()) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("token", token);
                mSocket.emit("join-chat-rom", jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void chat(String message) {
        SharedPreferences sharedPreferences = getSharedPreferences("appcache", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        if (!token.isEmpty()) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("token", token);
                jsonObject.put("message", message);
                mSocket.emit("chat", jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
