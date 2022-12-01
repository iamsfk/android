package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class UploadPdfActivity extends AppCompatActivity {
    private CardView addpdf;
    private EditText pdfTitle;
    private Button uploadpdfBtn;
    private final int REQ=1;
    private Uri pdfData;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    String downloadUrl = "";
    private ProgressDialog pd;
    private TextView pdfTextView;
    private String pdfName,title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);

        addpdf = findViewById(R.id.addpdf);
        pdfTitle = findViewById(R.id.pdfTitle);
        uploadpdfBtn = findViewById(R.id.uploadpdfBtn);
        pdfTextView = findViewById(R.id.pdfTextView);


        addpdf.setOnClickListener((view) -> { openGallery();});

        uploadpdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = pdfTitle.getText().toString();
                if(title.isEmpty()){
                    pdfTitle.setError("empty");
                    pdfTitle.requestFocus();
                }else if (pdfData == null){
                    Toast.makeText(UploadPdfActivity.this, "please upload", Toast.LENGTH_SHORT).show();
                }else{
                    uploadpdf();
                }
            }
        });

    }

    private void uploadpdf() {
        pd.setTitle("please wait...");
        pd.setMessage("uploading pdf");
        pd.show();
        StorageReference reference = storageReference.child("pdf/"+pdfName+":"+System.currentTimeMillis()+".pdf");
        reference.putFile(pdfData)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uri = uriTask.getResult();
                        uploadData(String.valueOf(uri));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(UploadPdfActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadData(String downloadUrl) {
        String uniquekey = databaseReference.child("pdf").push().getKey();

        HashMap data = new HashMap();
        data.put("pdfTitle",title);
        data.put("pdfUrl",downloadUrl);

        databaseReference.child("pdf").child(uniquekey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(UploadPdfActivity.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                 pdfTitle.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadPdfActivity.this, "Failed to upload", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
       Intent intent = new Intent();
       intent.setType("application/pdf");
       intent.setAction(Intent.ACTION_GET_CONTENT);
       startActivityForResult(Intent.createChooser(intent,"select pdf file"),REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ && resultCode == RESULT_OK){
            pdfData = data.getData();

           if(pdfData.toString().startsWith("content://")){
               Cursor cursor = null;
               try {
                   cursor = UploadPdfActivity.this.getContentResolver().query(pdfData,null,null,null,null);
                   if (cursor != null && cursor.moveToFirst()){
                       int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                       cursor.moveToFirst();
                       pdfName = cursor.getString(columnIndex);
                   }
               } catch (IllegalArgumentException e) {
                   e.printStackTrace();
               }

           }else if(pdfData.toString().startsWith("file://")){
                pdfName = new File(pdfData.toString()).getName();
           }
        }
    }
}