package com.app.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
   Button loginBtn;
   TextView forget_password,create_account;
   EditText loginEmail,loginPassword;
   FirebaseAuth auth;
   ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        loginBtn=findViewById( R.id.loginBtn );
        loginEmail=findViewById( R.id.email );
        loginPassword=findViewById( R.id.password );
        forget_password=findViewById( R.id.forgrt_password );
        create_account=findViewById( R.id.dont_have_an_account_create_account );

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle( "Login" );
        actionBar.setDisplayHomeAsUpEnabled( true );
        actionBar.setDisplayShowCustomEnabled( true );

        pd= new ProgressDialog( this );

        auth=FirebaseAuth.getInstance();

        forget_password.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(LoginActivity.this,forgetPasswordActivity.class) );

            }
        } );

        create_account.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(LoginActivity.this,RegisterActivity.class) );
            }
        } );
        loginBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=loginEmail.getText().toString().trim();
                String password=loginPassword.getText().toString().trim();
                if(email.isEmpty())
                {
                    loginEmail.setError( "Enter Email" );
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher( email ).matches())
                {
                    loginEmail.setError( "Invalid email" );
                }
                else if(password.isEmpty())
                {
                    loginPassword.setError( "Enter password" );
                }
                else {
                    login(email,password);
                }
            }
        } );



    }

    private void login(String email, String password) {
                 pd.setMessage( "please Wait" );
                 pd.show();
        auth.signInWithEmailAndPassword( email,password ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    pd.dismiss();
                    startActivity( new Intent(LoginActivity.this,homeActivity.class) );
                    finish();

                }
                else {
                    Toast.makeText( LoginActivity.this, "Failed", Toast.LENGTH_SHORT ).show();
                }


            }
        } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText( LoginActivity.this, ""+e, Toast.LENGTH_SHORT ).show();

            }
        } );
    }
}