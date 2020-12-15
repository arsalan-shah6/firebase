package com.app.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class homeActivity extends AppCompatActivity {
     FirebaseAuth auth;
     RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );
        auth=FirebaseAuth.getInstance();
        recyclerView=findViewById( R.id.recycle_view );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.main_menu,menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logout){
         auth.signOut();
         startActivity( new Intent(homeActivity.this,MainActivity.class) );
        }
        if(item.getItemId()==R.id.add_post)
        {
            startActivity(new Intent(homeActivity.this,AddPostActivity.class) );
        }


        return super.onOptionsItemSelected( item );

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }
}