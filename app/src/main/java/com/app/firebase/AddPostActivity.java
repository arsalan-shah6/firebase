package com.app.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

public class AddPostActivity extends AppCompatActivity {
EditText BlogTitle,BlogDescription;
Button UploadButton;
ImageView blogImage;
Uri image_uri =null;
ProgressDialog pd;
FirebaseAuth auth;
public static final int GALLERY_IMAGE_CODE=100;
 public  static final int CAMERA_IMAGE_CODE=200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        permission();
        setContentView( R.layout.activity_add_post );
        BlogTitle=findViewById( R.id.title_blog );
        BlogDescription=findViewById( R.id.description_blog );
        UploadButton=findViewById( R.id.upload_Btn );
        blogImage=findViewById( R.id.PostImage );
        pd=new ProgressDialog(this );
        auth=FirebaseAuth.getInstance();
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle( "Add Post" );
        actionBar.setDisplayShowCustomEnabled( true );
        actionBar.setDisplayHomeAsUpEnabled( true );

        blogImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePickDialog();
            }
        } );

   UploadButton.setOnClickListener( new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           String title=BlogTitle.getText().toString();
           String description=BlogDescription.getText().toString();
           if (title.isEmpty())
           {
               BlogTitle.setError( "Blog title is empty" );
           }else if (description.isEmpty())
           {
               BlogDescription.setError( "Blog description is empty" );
           }else { uploadData(title,description);}
       }
   } );

    }

    private void uploadData(String title, String description) {
        pd.setMessage( "Uploading" );
        pd.show();
        final String timestamp=String.valueOf(System.currentTimeMillis());
        String filepath= "Post/"+"post"+timestamp;
        if (blogImage.getDrawable()!=null)
        {
            Bitmap bitmap=((BitmapDrawable)blogImage.getDrawable()).getBitmap();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            bitmap.compress( Bitmap.CompressFormat.PNG,100,baos );
            byte[] data=baos.toByteArray();

            StorageReference reference= FirebaseStorage.getInstance().getReference().child( filepath );
            reference.putBytes( data ).
                    addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            String downloadUri=uriTask.getResult().toString();
                            if (uriTask.isSuccessful())
                            {
                                FirebaseUser user=auth.getCurrentUser();
                                HashMap<String,Object> hashMap=new HashMap<>();
                                hashMap.put("uid",user.getUid()  );
                                hashMap.put("uEmail", user.getEmail() );
                                hashMap.put("pid", timestamp );
                                hashMap.put("pTitle",  title);
                                hashMap.put("pImage", downloadUri );
                                hashMap.put("pDescription", description );
                                hashMap.put("pTime", timestamp );


                                DatabaseReference ref= FirebaseDatabase.getInstance().getReference("post");
                                ref.child( timestamp ).setValue( hashMap ).addOnSuccessListener( new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        pd.dismiss();
                                        Toast.makeText( AddPostActivity.this, "Post Published", Toast.LENGTH_SHORT ).show();
                                        BlogTitle.setText( "" );
                                        BlogDescription.setText( "" );
                                        blogImage.setImageURI( null );
                                        image_uri=null;
                                        startActivity( new Intent(AddPostActivity.this,homeActivity.class) );
                                    }
                                } ).addOnFailureListener( new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pd.dismiss();
                                        Toast.makeText( AddPostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT ).show();

                                    }
                                } );


                            }
                        }
                    } ).addOnFailureListener( new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText( AddPostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT ).show();
                    pd.dismiss();
                }
            } );


        }

    }

    private void imagePickDialog() {

        String[] option={"Camera","Gallery"};
        AlertDialog.Builder alertdialog=new AlertDialog.Builder( this );
        alertdialog.setTitle( "Choose Image From" );
     alertdialog.setItems( option, new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialogInterface, int i) {
             if(i==0)
             {
                 CameraPick();
             }
             if(i==1){
                 GalleryPick();
             }
         }
     } );
        alertdialog.create().show();
    }

    private void GalleryPick()
    {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType( "image/*" );
        startActivityForResult( intent,GALLERY_IMAGE_CODE );

    }

    private void CameraPick() {
        ContentValues contentValues=new ContentValues();
        contentValues.put( MediaStore.Images.Media.TITLE,"Temp Pick" );
        contentValues.put( MediaStore.Images.Media.TITLE,"Temp desc" );
        image_uri=getContentResolver().insert( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues );
        Intent intent=new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
        intent.putExtra( MediaStore.EXTRA_OUTPUT,image_uri );
        startActivityForResult( intent,CAMERA_IMAGE_CODE );

    }
    private void permission() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK)
        {
            if (requestCode==GALLERY_IMAGE_CODE)
            {
                image_uri=data.getData();
                blogImage.setImageURI( image_uri );
            }
            if (requestCode==CAMERA_IMAGE_CODE)
            {

                blogImage.setImageURI( image_uri );

            }

        }
        super.onActivityResult( requestCode, resultCode, data );
    }
}