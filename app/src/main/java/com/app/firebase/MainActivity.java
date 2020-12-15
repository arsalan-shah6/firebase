package com.app.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
Button loginBtn,registerBtn;
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        loginBtn=findViewById( R.id.login );
        registerBtn=findViewById( R.id.register );
        auth=FirebaseAuth.getInstance();


        loginBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(MainActivity.this,LoginActivity.class) );

            }
        } );
        registerBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(MainActivity.this,RegisterActivity.class) );

            }
        } );
    }

    @Override
    protected void onStart() {
        FirebaseUser user=auth.getCurrentUser();
        if(user!=null)
        {
            startActivity( new Intent(MainActivity.this,homeActivity.class) );
        }
        super.onStart();
    }
}