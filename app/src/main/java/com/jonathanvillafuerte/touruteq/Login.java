package com.jonathanvillafuerte.touruteq;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    private EditText user;
    private Button btn;

    String datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = (EditText)findViewById(R.id.user);
        btn = (Button) findViewById(R.id.btnSendData);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Login.this, Geofencing.class );
                Bundle b = new Bundle();
                b.putString("Nombre", user.getText().toString());
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }
}
