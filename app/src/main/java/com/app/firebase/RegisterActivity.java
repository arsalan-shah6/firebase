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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
EditText allReadyHaveAnAccount;
TextView reg_userName,reg_Password,reg_email;
Button register;
FirebaseAuth auth;
ProgressDialog pd;
    String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );
        allReadyHaveAnAccount=findViewById( R.id.alerady_have_account );
        reg_userName=findViewById( R.id.reg_userName );
        reg_email=findViewById( R.id.reg_email );
        reg_Password=findViewById( R.id.reg_password );
        register=findViewById( R.id.reg_button );

        auth=FirebaseAuth.getInstance();

        pd=new ProgressDialog( this );

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle( "Register" );
        actionBar.setDisplayShowCustomEnabled( true );
        actionBar.setDisplayHomeAsUpEnabled( true );

allReadyHaveAnAccount.setOnClickListener( new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startActivity( new Intent(RegisterActivity.this,LoginActivity.class) );
    }
} );

        register.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 userName=reg_userName.getText().toString().trim();
                String email=reg_email.getText().toString().trim();
                String password=reg_Password.getText().toString().trim();
                if(userName.isEmpty())
                {
                    reg_userName.setError( "Enter Username" );
                }
               else if(email.isEmpty())
                {
                    reg_email.setError( "Enter Email" );
                }
               else if(!Patterns.EMAIL_ADDRESS.matcher( email ).matches())
                {
                    reg_email.setError( "Invalid Email Address" );
                }
               else if(password.isEmpty())
                {
                    reg_Password.setError( "Enter Password" );
                }
               else if(password.length()<6)
                {
                    Toast.makeText( RegisterActivity.this, "Password must be greater then 6 character", Toast.LENGTH_SHORT ).show();

                }
               else {
                   registerUser( email , password);
                }
            }
        } );


    }

    private void registerUser(String email, String password) {
        pd.setMessage( "please Wait" );
        pd.show();
        auth.createUserWithEmailAndPassword( email , password ).
                addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            pd.dismiss(); 
                            FirebaseUser firebaseUser= auth.getCurrentUser();
                            String email= firebaseUser.getEmail();
                            String uid=firebaseUser.getUid();
                            HashMap<Object,String> hashMap =new HashMap<>();
                            hashMap.put( "uid",uid );
                            hashMap.put( "email",email );
                            hashMap.put( "userName",userName );
                            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference=firebaseDatabase.getReference("user");
                            databaseReference.child( uid ).setValue( hashMap );
                            startActivity( new Intent(RegisterActivity.this,homeActivity.class) );
                        }else {
                            Toast.makeText( RegisterActivity.this, "failed", Toast.LENGTH_SHORT ).show();
                            pd.dismiss();
                        }

                    }
                } ).
                addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText( RegisterActivity.this, ""+e, Toast.LENGTH_SHORT ).show();
                        pd.dismiss();
                    }
                } );
    }
}