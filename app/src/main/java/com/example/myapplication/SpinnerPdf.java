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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.HashMap;

public class SpinnerPdf extends AppCompatActivity {
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
    private Spinner imageCategory;
    private String category;
    //dupl

    private CardView selectImage;
    private Button UploadImage;
    private ImageView galleryImageView;


    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner_pdf);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);

        addpdf = findViewById(R.id.addpdf1);
        pdfTitle = findViewById(R.id.pdfTitle);
        uploadpdfBtn = findViewById(R.id.uploadpdBtn);
        //pdfTextView = findViewById(R.id.pdfTextView);
        imageCategory = findViewById(R.id.image_category);

        String [] items = new String[]{"pf","cse11", "cse12", "cse13","cse14","cse15","cse21","cse22","cse23","cse24","cse25", "cse31","cse32","cse33","cse34","cse35","cse41","cse42","cse43","cse44","cse45","cse51","cse52","cse53","cse54","cse55","cse61","cse62","cse63","cse64","cse65","cse71","cse72","cse73","cse74","cse75","cse81","cse82","cse83","cse84","cse85","bem11","bem12","bem13","bem14","bem15","bem21","bem22","bem23","bem24","bem25","bem31","bem32","bem33","bem34","bem35","bem36","bem37","bem41","bem42","bem43","bem44","bem45","bem46","bem47","bem51","bem52","bem53","bem54","bem55","bem56","bem61","bem62","bem63","bem64","bem65","bem71","bem72","bem73","bem74","bem75","bem81","bem82","bem83","bem84","bem85","civ21","civ22","civ23","civ24","civ25","civ31","civ32","civ33","civ34","civ35","civ36","civ37","civ41","civ42","civ43","civ44","civ45","civ46","civ51","civ52","civ53","civ54","civ55","civ56","civ61","civ62","civ63","civ64","civ65","civ71","civ72","civ73","civ74","civ75","civ81","civ82","civ83","civ84","civ85","ec21","ec22","ec23","ec24","ec25","ec31","ec32","ec33","ec34","ec35","ec41","ec42","ec43","ec44","ec45","ec46","ec51","ec52","ec53","ec54","ec55","ec56","ec61","ec62","ec63","ec64","ec65","ec71","ec72","ec73","ec74","ec75","ec81","ec82","ec83","ec84","ec85","eee21","eee22","eee23","eee24","eee25","eee31","eee32","eee33","eee34","eee35","eee36","eee41","eee42","eee43","eee44","eee45","eee46","eee51","eee52","eee53","eee54","eee55","eee56","eee61","eee62","eee63","eee64","eee65","eee71","eee72","eee73","eee74","eee75","eee81","eee82","eee83","eee84","eee85","cseque1","cseque2","cseque3","cseque4","cseque5","cseque6","cseque7","cseque8","mechque1","mechque2","mechque3","mechque4","mechque5","mechque6","mechque7","mechque8","civilque1","civilque2","civilque3","civilque4","civilque5","civilque6","civilque7","civilque8","ecque1","ecque2","ecque3","ecque4","ecque5","ecque6","ecque7","ecque8","eeeque1","eeeque2","eeeque3","eeeque4","eeeque5","eeeque6","eeeque7","eeeque8","sun","ba11","ba12","ba13","ba14","ba15","ba16","ba21","ba22","ba23","ba24","ba25","ba26","ba31","ba32","ba33","ba34","ba35","ba36","ba41","ba42","ba43","ba44","ba45","ba46","ba51","ba52","ba53","ba54","ba55","ba56","ba57","ba61","ba62","ba63","ba64","ba65","ba66","ba77","bcm11","bcm12","bcm13","bcm14","bcm15","bcm16","bcm81","bcm21","bcm22","bcm23","bcm24","bcm25","bcm26","bcm82","bcm31","bcm32","bcm33","bcm34","bcm35","bcm36","bcm83","bcm84","bcm41","bcm42","bcm43","bcm44","bcm45","bcm46","bcm85","bcm86","bcm51","bcm52","bcm53","bcm54","bcm55","bcm56","bcm87","bcm61","bcm62","bcm63","bcm64","bcm65","bcm66","bcm88"
        ,"bca21","bca22","bca23","bca24","bca25","bca16","bca1","bca2","bca3","bca4","bca5","bca6","bca31","bca32","bca33","bca34","bca35","bca36","bca41","bca42","bca43","bca44","bca45","bca46","bca51","bca52","bca53","bca54","bca55","bca56","bca61","bca62","bca63","bca64","mathsnotes","mathsque","sciencenotes","scienceque","ssnotes","ssque","englishnotes","englishque","hindinotes","hindique","kannadanotes","kannadaque","pumathnotes","mathque","biologynotes","biologyque","physicsnotes","physicsque","chemistrynotes","chemistryque","commercenotes","commerceque","artsnotes","artsque"};
        imageCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,items));

        imageCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = imageCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        addpdf.setOnClickListener((view) -> { openGallery();});

        uploadpdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = pdfTitle.getText().toString();
                if(title.isEmpty()){
                    pdfTitle.setError("empty");
                    pdfTitle.requestFocus();
                }else if (pdfData == null){
                    Toast.makeText(SpinnerPdf.this, "please upload", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SpinnerPdf.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadData(String downloadUrl) {
        String uniquekey = databaseReference.child(category).push().getKey();

        HashMap data = new HashMap();
        data.put("pdfTitle",title);
        data.put("pdfUrl",downloadUrl);

        databaseReference.child(category).child(uniquekey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(SpinnerPdf.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                pdfTitle.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(SpinnerPdf.this, "Failed to upload", Toast.LENGTH_SHORT).show();
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
                    cursor = SpinnerPdf.this.getContentResolver().query(pdfData,null,null,null,null);
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