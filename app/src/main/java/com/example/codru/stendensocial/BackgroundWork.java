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

/*
 * Made by Marin Codrut, Tim Vaneker, Smith Chen and Flavian Gall
 */
public class BackgroundWork extends AsyncTask<String,Void, String> {
    Context context;
    AlertDialog alertDialog;
    String user;
    boolean isThere;
    public BackgroundWork(Context ctx)
    {
        context = ctx;
        isThere = false;
    }
    String user_name;
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        if(type == "login")
        {
            try{
                //String login_url = "http://www.bunte-bloecke.de/htdocsit4c/PHP/login.php";
                String login_url = "http://192.168.1.7/login.php";
                user_name = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
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
        }

        if(type == "signup")
        {
            try{
                //String login_url = "http://www.bunte-bloecke.de/htdocsit4c/PHP/register.php";
                String login_url = "http://192.168.1.7/register.php";
                String firstN = params[1];
                String secondN = params[2];
                String password = params[3];
                String sMail = params[4];
                String picture = params[5];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("firstN","UTF-8")+"="+URLEncoder.encode(firstN,"UTF-8")+"&"
                        +URLEncoder.encode("secondN","UTF-8")+"="+URLEncoder.encode(secondN,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"
                        +URLEncoder.encode("sMail","UTF-8")+"="+URLEncoder.encode(sMail,"UTF-8")+"&"
                        +URLEncoder.encode("Picture","UTF-8")+"="+URLEncoder.encode(picture,"UTF-8");
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
        String a = "Doesn't exists!";
        String b = "Exists";
        String c = "SignUp";
        try{
            if(aVoid.equals(a))
            {
                alertDialog.setMessage("Your username or password aleady exist!");
                alertDialog.show();
            }else {
                if(aVoid.equals(c))
                {
                    Intent i =  new Intent(context,SignIn.class);
                    context.startActivity(i);
                }else {

                         Intent i =  new Intent(context,Welcome.class);
                         i.putExtra("StendenMail",user_name);
                         i.putExtra("Username",aVoid);
                         context.startActivity(i);

                }
            }
        }catch (Exception ex){
            alertDialog.setMessage("You are not connected to the internet! Check your connection and try again.");
            alertDialog.show();
        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
