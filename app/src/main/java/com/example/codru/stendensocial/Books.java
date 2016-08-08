package com.example.codru.stendensocial;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import static android.graphics.Bitmap.*;

public class Books extends AppCompatActivity {

    private int PICK_IMAGE_REQUEST = 100;
    private Uri filePath;
    private Bitmap bitmap;
    private EditText title;
    private EditText ISBN;
    private EditText condition;
    private EditText price;
    private ImageButton image;
    private String StendenMail;
    private String Username;
    private ArrayList<String> bookTitle;
    private ArrayList<String> bookisbn;
    private ArrayList<String> bookCondition;
    private ArrayList<String> bookPrice;
    private Thread t;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);
        image = (ImageButton) findViewById(R.id.Img);
        title = (EditText) findViewById(R.id.bTitle);
        ISBN = (EditText) findViewById(R.id.bISBN);
        condition = (EditText) findViewById(R.id.bCondition);
        price = (EditText) findViewById(R.id.bPrice);
        final Button b = (Button) findViewById(R.id.button);
        StendenMail = getIntent().getStringExtra("StendenMail");
        Username = getIntent().getStringExtra("UserName");
        b.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                try {
                    Bitmap bitmap;
                    if(title.getText().toString().isEmpty() || title.getText().toString() == null ||
                            ISBN.getText().toString().isEmpty() || ISBN.getText().toString().contains(" ") ||
                            condition.getText().toString().isEmpty() || price.getText().toString().isEmpty() ||
                            price.getText().toString().contains(" ")){
                        bitmap = null;
                    }else{
                        bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                    }

                    String img = getStringImage(bitmap);
                    DbConnect conn = new DbConnect(Books.this);
                    conn.execute(StendenMail, title.getText().toString(), ISBN.getText().toString(), condition.getText().toString(), price.getText().toString(), img);
                    title.setText("");
                    ISBN.setText("");
                    condition.setText("");
                    price.setText("");
                    image.setImageBitmap(Bitmap.createBitmap(10,20,Config.RGB_565));
                } catch (Exception r) {
                    AlertDialog ad = new AlertDialog.Builder(Books.this).create();
                    ad.setTitle("Image Error");
                    ad.setMessage("Message: " + r.getMessage() + " please fill all the fields!");
                    ad.show();
                }
            }
        });

    }

    public void goToBuyBooks(View v) {
        Intent i = new Intent(Books.this, ListOfBooks.class);
        i.putExtra("StendenMail", StendenMail);
        i.putExtra("UserName", Username);
        startActivity(i);
    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //bitmap.reconfigure(90,50, Bitmap.Config.RGB_565);
                bitmap = createScaledBitmap(bitmap, 550, 700, false);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(CompressFormat.JPEG, 100, bytes);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                AlertDialog ad = new AlertDialog.Builder(this).create();
                ad.setMessage("Is something wrong there!");
                ad.show();
            }
        }
    }


    public void chooseImage(View v) {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}

