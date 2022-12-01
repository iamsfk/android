package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    CardView UploadNotice,addGalleryImage, addEbook,addpdfspin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UploadNotice = findViewById(R.id.addNotice);
        addGalleryImage = findViewById(R.id.addGalleryImage);

        addEbook=findViewById(R.id.addEbook);
        addpdfspin = findViewById(R.id.addPdfspin);
        addpdfspin.setOnClickListener(this);
        UploadNotice.setOnClickListener(this);
        addGalleryImage.setOnClickListener(this);
        addEbook.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){

            case R.id.addNotice:
                 intent = new Intent(MainActivity.this,UploadNotice.class);
                startActivity(intent);
                break;
            case R.id.addGalleryImage:
                 intent = new Intent(MainActivity.this,UploadImage.class);
                startActivity(intent);
                break;
            case R.id.addEbook:
                intent = new Intent(MainActivity.this,UploadPdfActivity.class);
                startActivity(intent);
                break;
            case R.id.addPdfspin:
                intent = new Intent(MainActivity.this,SpinnerPdf.class);
                startActivity(intent);
                break;
        }
    }
}
