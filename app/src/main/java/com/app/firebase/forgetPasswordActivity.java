package com.app.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgetPasswordActivity extends AppCompatActivity {
EditText edForgot;
Button btnForgot;
FirebaseAuth auth;
ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_forget_password );
        edForgot =findViewById( R.id.ForgerEmail );
        btnForgot =findViewById( R.id.ForgetBtn);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Forgot Password");
        actionBar.setDisplayHomeAsUpEnabled( true );
        actionBar.setDisplayHomeAsUpEnabled( true );

        auth=FirebaseAuth.getInstance();

        pd=new ProgressDialog( this );

        btnForgot.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= edForgot.getText().toString();
                if (email.isEmpty())
                {
                    edForgot.setError( "Please enter Email" );
                }else {
                    ForgeRecoverPassword(email);
                }

                
            }
        } );
    }

    private void ForgeRecoverPassword(String email) {
        pd.setMessage( "Please Wait" );
        pd.show();
        auth.sendPasswordResetEmail(email).addOnCompleteListener( new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    pd.dismiss();
                    Toast.makeText( forgetPasswordActivity.this, "Please check the email", Toast.LENGTH_SHORT ).show();
                    startActivity( new Intent(forgetPasswordActivity.this,MainActivity.class) );
                }

            }
        } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText( forgetPasswordActivity.this, ""+e, Toast.LENGTH_SHORT ).show();
            }
        } );


    }
}