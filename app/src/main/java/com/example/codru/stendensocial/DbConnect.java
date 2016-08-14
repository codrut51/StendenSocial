package com.example.codru.stendensocial;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by codru on 13/07/2016.
 */
public class DbConnect extends AsyncTask<String,Void, String> {
    Context context;
    AlertDialog alertDialog;
    public DbConnect(Context ctx)
    {
        context = ctx;
    }
    String user_name;
    @Override
    protected String doInBackground(String... params) {

        try{
            String login_url = "http://192.168.1.2/books.php";
            String StendenMail = params[0];
            String bTitle = params[1];
            String bISBN = params[2];
            String bCondition = params[3];
            String bPrice = params[4];
            String bImage = params[5];
            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String post_data = URLEncoder.encode("StendenMail","UTF-8")+"="+URLEncoder.encode(StendenMail,"UTF-8")+"&"
                    +URLEncoder.encode("bTitle","UTF-8")+"="+URLEncoder.encode(bTitle,"UTF-8")+"&"
                    +URLEncoder.encode("bISBN","UTF-8")+"="+URLEncoder.encode(bISBN,"UTF-8")+"&"
                    +URLEncoder.encode("bCondition","UTF-8")+"="+URLEncoder.encode(bCondition,"UTF-8")+"&"
                    +URLEncoder.encode("bPrice","UTF-8")+"="+URLEncoder.encode(bPrice,"UTF-8")+"&"
                    +URLEncoder.encode("bImage","UTF-8")+"="+URLEncoder.encode(bImage,"UTF-8");
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
        alertDialog.show();

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}

