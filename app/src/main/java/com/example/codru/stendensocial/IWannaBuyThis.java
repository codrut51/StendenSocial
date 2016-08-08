package com.example.codru.stendensocial;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class IWannaBuyThis extends ListActivity {
    private String BookID;
    private String StendenMail;
    private String UserName;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items;
    private ArrayList<String> mails;
    private ArrayList<String> users;
    private ArrayList<String> msgs;
    private EditText msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iwanna_buy_this);
        msg = (EditText) findViewById(R.id.message);
        BookID = getIntent().getStringExtra("BookID");
        StendenMail = getIntent().getStringExtra("StendenMail");
        UserName = getIntent().getStringExtra("UserName");
        items = new ArrayList<>();
        mails = new ArrayList<>();
        users = new ArrayList<>();
        msgs = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getListView().getContext(), android.R.layout.simple_list_item_1, items);
        getListView().setAdapter(adapter);
        getMessages();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                while(true)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getMessages();
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                }
            }
        });
        t.start();
    }

    public void addMessage(View v){
        if(!msg.getText().toString().isEmpty())
        {

        }
        uploadMessage(StendenMail,msg.getText().toString(),UserName,BookID);
        msg.setText("");
    }

    public void uploadMessage(String... b)
    {
        class uploadMsg extends AsyncTask<String,Void, String> {
            Context context;
            AlertDialog alertDialog;
            public uploadMsg(Context ctx)
            {
                context = ctx;
            }
            String user_name;
            @Override
            protected String doInBackground(String... params) {

                try{
                    String login_url = "http://192.168.1.7/uploadBookMSG.php";
                    String StendenMail = params[0];
                    String message = params[1];
                    String username = params[2];
                    String bookID = params[3];
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String post_data = URLEncoder.encode("StendenMail","UTF-8")+"="+URLEncoder.encode(StendenMail,"UTF-8")+"&"
                            +URLEncoder.encode("Message","UTF-8")+"="+URLEncoder.encode(message,"UTF-8")+"&"
                            +URLEncoder.encode("UserName","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"
                            +URLEncoder.encode("BookID","UTF-8")+"="+URLEncoder.encode(bookID,"UTF-8");
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
                //alertDialog.show();

            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }
        }
        uploadMsg message = new uploadMsg(this);
        message.execute(b);
    }
    int x;
    String[] res;
    public void getMessages() {
        class ASD extends AsyncTask<String, Void, String> {

            AlertDialog alertDialog;

            @Override
            protected String doInBackground(String... params) {

                try {
                    String login_url = "http://192.168.1.7/getBookMSG.php";
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String post_data = URLEncoder.encode("BookID","UTF-8")+"="+URLEncoder.encode(BookID,"UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
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
                alertDialog = new AlertDialog.Builder(IWannaBuyThis.this).create();
                alertDialog.setTitle("Login Error");
            }


            @Override
            protected void onPostExecute(String bitmap) {
                if (bitmap != null && !bitmap.isEmpty() && bitmap != " ") {
                    String[] str = bitmap.split("</br>");
                    int p = 0;
                    int y = 0;
                    //cheching if the output is the same
                    if (str.length > (users.size() * 3)) {
                        adapter.clear();
                        mails.clear();
                        users.clear();
                        msgs.clear();
                        for (int j = 0; j < str.length; j++) {
                            switch (p)
                            {
                                case 0:
                                    msgs.add(str[j]);
                                    break;
                                case 1:
                                    mails.add(str[j]);
                                    break;
                                case 2:
                                    users.add(str[j]);
                                    break;
                            }
                            p++;
                            if(p == 3)
                            {
                                adapter.add(users.get(y) + ": " + msgs.get(y));
                                adapter.notifyDataSetChanged();
                                y++;
                            }
                            p = p % 3;

                        }
                    } else {
                        AlertDialog ad = new AlertDialog.Builder(IWannaBuyThis.this).create();
                        ad.setMessage("Bitmap is null!");
                        //ad.show();
                    }
                }
            }
        }
        ASD b = new ASD();
        b.execute("");
    }
}

