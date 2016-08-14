package com.example.codru.stendensocial;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class buyBooks extends AppCompatActivity {
    public TextView buytitle;
    public TextView buyisbn;
    public TextView buycondition;
    public TextView buyprice;
    public TextView bUsername;
    public ImageView image;
    public String bTitle;
    public String bIsbn;
    public String bCondition;
    public String bPrice;
    public String bID;
    public String bOwner;
    public String bOwnerName;
    public String StendenMail;
    public String UserName;
    public Button delete;
    public Button buy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_books);
        delete = (Button) findViewById(R.id.delete);
        buy = (Button) findViewById(R.id.buy);
        delete.setVisibility(View.INVISIBLE);
        delete.setClickable(false);
        buytitle = (TextView) findViewById(R.id.buyTitle);
        buyisbn = (TextView) findViewById(R.id.buyISBN);
        buycondition = (TextView) findViewById(R.id.buyCOND);
        buyprice = (TextView) findViewById(R.id.buyPRICE);
        bUsername = (TextView) findViewById(R.id.Username);
        image = (ImageView) findViewById(R.id.imageS);
        StendenMail = getIntent().getStringExtra("StendenMail");
        UserName = getIntent().getStringExtra("UserName");
        bID = getIntent().getStringExtra("bookID");
        getImage(bID);
        bOwner = getIntent().getStringExtra("Owner");
        bOwnerName = getIntent().getStringExtra("OwnerName");
        bUsername.setText(bOwnerName);
        if(StendenMail.equals(bOwner) || StendenMail.contains(bOwner)){
            delete.setVisibility(View.VISIBLE);
            delete.setClickable(true);
        }

        if(StendenMail.equals("admin"))
        {
            delete.setVisibility(View.VISIBLE);
            delete.setClickable(true);
        }
        bTitle = getIntent().getStringExtra("Title");
        bIsbn = getIntent().getStringExtra("ISBN");
        bCondition = getIntent().getStringExtra("Condition");
        bPrice = getIntent().getStringExtra("Price");
        String s = buyisbn.getText().toString() + bIsbn;
        buytitle.setText(bTitle);
        buyisbn.setText(s);
        buycondition.setText(buycondition.getText().toString() + bCondition);
        buyprice.setText(buyprice.getText().toString() + bPrice);

    }

    public void buyBook(View v){
        Intent i = new Intent(buyBooks.this,IWannaBuyThis.class);
        i.putExtra("StendenMail",StendenMail);
        i.putExtra("UserName",UserName);
        i.putExtra("BookID",bID);
        startActivity(i);
    }

    public void deleteBook(View V)
    {
        class deleteMsg extends AsyncTask<String,Void, String> {
            Context context;
            AlertDialog alertDialog;
            public deleteMsg(Context ctx)
            {
                context = ctx;
            }
            String user_name;
            @Override
            protected String doInBackground(String... params) {

                try{
                    String login_url = "http://192.168.1.2/deleteBookMSG.php";
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String post_data = URLEncoder.encode("BookID","UTF-8")+"="+URLEncoder.encode(bID,"UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result = "";
                    String line;
                    while((line = bufferedReader.readLine()) != null)
                    {
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                }catch (Exception ex){

                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Login Error");
            }

            @Override
            protected void onPostExecute(String aVoid) {

                alertDialog.setMessage("Message: " + aVoid);
//                alertDialog.show();

            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }
        }
        deleteMsg message = new deleteMsg(this);
        message.execute();
        Intent i = new Intent(this,ListOfBooks.class);
        i.putExtra("StendenMail",StendenMail);
        i.putExtra("UserName",UserName);
        startActivity(i);
    }
    private void getImage(String id) {
        class GetImage extends AsyncTask<String,Void,Bitmap>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(buyBooks.this, "Wait for image to load...", null,true,true);
            }

            @Override
            protected void onPostExecute(Bitmap b) {
                super.onPostExecute(b);
                loading.dismiss();
                image.setImageBitmap(b);
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                String id = params[0];
                String add = "http://192.168.1.2/getBooks.php?id="+id;
                URL url = null;
                String result = null;
                Bitmap image = null;
                try {
                    url = new URL(add);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(),"iso-8859-1"));
                    result = "";
                    String line;
                    while((line = bufferedReader.readLine()) != null)
                    {
                        result += line;
                    }
                    bufferedReader.close();
                    httpURLConnection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return image;
            }
        }

        GetImage gi = new GetImage();
        gi.execute(id);
    }

}

