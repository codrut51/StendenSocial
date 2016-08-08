package com.example.codru.stendensocial;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
/*
 * Made by Marin Codrut, Tim Vaneker, Smith Chen and Flavian Gall
 */
public class SignUp extends AppCompatActivity {

    private Bitmap bitmap;
    ImageButton img;
    private int PICK_IMAGE_REQUEST = 1;
    EditText stendenMail;
    private Uri filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        img = (ImageButton) findViewById(R.id.imageButton2);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent i = new Intent(SignUp.this,SignIn.class);
            finish();
            startActivity(i);
        }
        return true;
    }
    public void chooseImage(View v)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
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
                bitmap = Bitmap.createScaledBitmap(bitmap, 550, 700, false);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                img.setImageBitmap(bitmap);
            } catch (IOException e) {
                AlertDialog ad = new AlertDialog.Builder(this).create();
                ad.setMessage("Is something wrong there!");
                ad.show();
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    public void signUp(View v)
    {
        EditText firstName = (EditText) findViewById(R.id.firstN);
        EditText secondName = (EditText) findViewById(R.id.secondN);
        EditText password = (EditText) findViewById(R.id.password);
        EditText stendenMail = (EditText) findViewById(R.id.mail);
        String fn = firstName.getText().toString();
        String sn = secondName.getText().toString();
        String pass = password.getText().toString();
        String mail = stendenMail.getText().toString();
        String type = "signup";
        if(fn.length() > 0 && fn != null)
        {

            if(sn.length() > 0 && sn != null)
            {
                if(pass.length() >= 8 && pass != null)
                {
                    if(mail.contains("@student.stenden.com") || mail.contains("@stenden.com"))
                    {
                        BackgroundWork backgroundWork = new BackgroundWork(this);
                        backgroundWork.execute(type,fn,sn,pass,mail,getStringImage(bitmap));

                    }else{
                        AlertDialog ad = new AlertDialog.Builder(this).create();
                        ad.setTitle("Mail ERROR");
                        ad.setMessage("The mail should contain '@stenden.com' or '@student.stenden.com'");
                        ad.show();
                    }
                }else{
                    AlertDialog ad = new AlertDialog.Builder(this).create();
                    ad.setTitle("Password ERROR");
                    ad.setMessage("The password should be more than 8 characters like: '12345678'");
                    ad.show();
                }
            }else{
                AlertDialog ad = new AlertDialog.Builder(this).create();
                ad.setTitle("Second Name ERROR");
                ad.setMessage("You type an wrong second name!");
                ad.show();
            }
        }else{
            AlertDialog ad = new AlertDialog.Builder(this).create();
            ad.setTitle("First Name ERROR");
            ad.setMessage("You type an wrong first name!");
            ad.show();
        }

    }
}
