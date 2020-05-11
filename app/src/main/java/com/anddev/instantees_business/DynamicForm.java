package com.anddev.instantees_business;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

public class DynamicForm extends AppCompatActivity {

    EditText model_name,company_name,price,processor,front_camera,rear_camera,battery;
    CheckBox color_one,color_two,color_three,color_four,color_five;
    ImageView imageView;
    Button add;


    String Color_one = "Null";
    String Color_two = "Null";
    String Color_three = "Null";
    String Color_four = "Null";
    String Color_five = "Null";

    FirebaseUser firebaseUser;


    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    private final int IMAGE_REQUEST = 1;

    private Uri imageUri;

    StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_form);

        model_name = findViewById(R.id.model_name);
        company_name = findViewById(R.id.company_name);
        price = findViewById(R.id.price);
        processor = findViewById(R.id.model_processor);
        front_camera = findViewById(R.id.model_frontcamera);
        rear_camera = findViewById(R.id.model_rearcamera);
        battery = findViewById(R.id.model_battery);

        color_one = findViewById(R.id.colour_black);
        color_two = findViewById(R.id.colour_white);
        color_three = findViewById(R.id.colour_grey);
        color_four = findViewById(R.id.colour_blue);
        color_five = findViewById(R.id.colour_red);

        imageView = findViewById(R.id.mobile_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IMAGE_REQUEST);
            }
        });




        color_one.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Color_one = "Black";
            }
        });
        color_two.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Color_two = "White";
            }
        });
        color_three.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Color_three = "Grey";
            }
        });
        color_four.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Color_four = "Blue";
            }
        });
        color_five.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Color_five = "Red";
            }
        });

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Model_name = model_name.getText().toString();
                String Company_name = company_name.getText().toString();
                String Price = price.getText().toString();
                String Processor = processor.getText().toString();
                String Front_camera = front_camera.getText().toString();
                String Rear_camera = rear_camera.getText().toString();
                String Battery = battery.getText().toString();
                addData(Model_name, Company_name, Price, Processor, Front_camera, Rear_camera, Battery, Color_one, Color_two, Color_three, Color_four, Color_five);
                uploadImage();
            }



        });




    }

    private void addData(String model_name, String company_name, String price, String processor, String front_camera, String rear_camera, String battery, String color_one, String color_two, String color_three, String color_four, String color_five) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        HashMap<String,Object> map = new HashMap<>();
        map.put("Model Name",model_name);
        map.put("Company Name",company_name);
        map.put("Price",price);
        map.put("Processor",processor);
        map.put("Front Camera",front_camera);
        map.put("Rear Camera",rear_camera);
        map.put("Battery",battery);
        map.put("Color One",color_one);
        map.put("Color Two",color_two);
        map.put("Color Three",color_three);
        map.put("Color Four",color_four);
        map.put("Color Five",color_five);
        map.put("Admin Id",firebaseUser.getUid());



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products");

        databaseReference.push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(DynamicForm.this, "Product Added", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            imageUri = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                imageUri);
                imageView.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }



    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if(imageUri != null){

            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation< UploadTask.TaskSnapshot,Task<Uri> >() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if(! task.isSuccessful()){
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();


                        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Products");
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("imageURL",mUri);
                        hashMap.put("Admin Id",firebaseUser.getUid());
                        reference.push().setValue(hashMap);




                        pd.dismiss();

                    }
                    else{
                        Toast.makeText(DynamicForm.this,"Failed!",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DynamicForm.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });

        }else{
            Toast.makeText(DynamicForm.this,"No Image Selected",Toast.LENGTH_SHORT).show();
        }


    }

}
