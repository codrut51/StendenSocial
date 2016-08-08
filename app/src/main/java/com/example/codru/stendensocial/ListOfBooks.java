package com.example.codru.stendensocial;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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
import java.util.ArrayList;

public class ListOfBooks extends ListActivity {
    private ImageView image;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> bookID;
    private ArrayList<String> bookStendenMail;
    private ArrayList<String> bookTitle;
    private ArrayList<String> bookISBN;
    private ArrayList<String> bookCondition;
    private ArrayList<String> bookPrice;
    private ArrayList<String> bookUserN;
    private String[] books;
    private String StendenMail;
    private String Username;
    int xyz = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_books);
        TextView cas = (TextView) findViewById(R.id.book123);
        StendenMail = getIntent().getStringExtra("StendenMail");
        Username = getIntent().getStringExtra("UserName");
        //cas.setText(StendenMail);
        ArrayList<String> items = new ArrayList<>();
        ListView list = (ListView) findViewById(android.R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent buy = new Intent(ListOfBooks.this, buyBooks.class);
                buy.putExtra("Owner",bookStendenMail.get(i));
                buy.putExtra("OwnerName",bookUserN.get(i));
                buy.putExtra("UserName",Username);
                buy.putExtra("StendenMail",StendenMail);
                buy.putExtra("bookID",bookID.get(i));
                buy.putExtra("Title",bookTitle.get(i));
                buy.putExtra("ISBN",bookISBN.get(i));
                buy.putExtra("Condition",bookCondition.get(i));
                buy.putExtra("Price",bookPrice.get(i));
                startActivity(buy);
            }
        });
        bookID = new ArrayList<>();
        bookStendenMail = new ArrayList<>();
        bookTitle = new ArrayList<>();
        bookISBN = new ArrayList<>();
        bookCondition = new ArrayList<>();
        bookPrice = new ArrayList<>();
        bookUserN = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getListView().getContext(), android.R.layout.simple_list_item_1, items);
        getListView().setAdapter(adapter);
        getBook();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {

                    }
                    getBook();
                }
            }
        });
        t.start();
    }
    String[] res;
    int x = 0;

    public void getBook() {
        class ASD extends AsyncTask<String,Void, String> {

            AlertDialog alertDialog;

            @Override
            protected String doInBackground(String... params) {

                try {
                    String login_url = "http://192.168.1.7/buyBooks.php";
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
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
                } catch (Exception ex) {

                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                alertDialog = new AlertDialog.Builder(ListOfBooks.this).create();
                alertDialog.setTitle("Login Error");
            }


            @Override
            protected void onPostExecute(String bitmap) {
                if (bitmap != null) {
                    String[] str = bitmap.split("</br>");
                    try{
                        x = res.length;
                    }catch (Exception x){

                    }

                    int p = 0;
                    //cheching if the output is the same
                    if(str.length > x || str.length < x)
                    {
                        bookID.clear();
                        bookStendenMail.clear();
                        bookTitle.clear();
                        adapter.clear();
                        bookISBN.clear();
                        bookCondition.clear();
                        bookPrice.clear();
                        for (int j = 0; j < str.length; j++) {
                            switch (p) {
                                case 0:
                                    bookUserN.add(str[j]);
                                    break;
                                case 1:
                                    bookID.add(str[j]);
                                    break;
                                case 2:
                                    bookStendenMail.add(str[j]);
                                    break;
                                case 3:
                                    bookTitle.add(str[j]);
                                    adapter.add(str[j]);
                                    adapter.notifyDataSetChanged();
                                    break;
                                case 4:
                                    bookISBN.add(str[j]);
                                    break;
                                case 5:
                                    bookCondition.add(str[j]);
                                    break;
                                case 6:
                                    bookPrice.add(str[j]);
                                    break;
                            }
                            p++;
                            p = p % 7;
                        }
                    }
                }else{
                    AlertDialog ad = new AlertDialog.Builder(ListOfBooks.this).create();
                    ad.setMessage("Bitmap is null!");
                    //ad.show();
                }
            }
        }
        ASD b = new ASD();
        b.execute("");
    }
}


