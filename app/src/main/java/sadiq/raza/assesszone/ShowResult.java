package sadiq.raza.assesszone;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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

/**
 * Created by Sadiq on 3/11/2018.
 */

public class ShowResult extends AsyncTask<String,Void,String> {
    Context context;
    AlertDialog alertDialog;

    public String name;
    public String email;
    public String jsonString;
    //String reg_id ="11505615";
    String test_id="17";
    ProgressDialog pdd;

    public ShowResult(Context context) {
        this.context=context;
    }

    @Override
    protected String doInBackground(String... param) {

        String login_url="https://assesszone.000webhostapp.com/client/showResult.php";
        try {
            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setReadTimeout(7000);
            httpURLConnection.setConnectTimeout(7000);
            OutputStream outputStream=httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String post_data= URLEncoder.encode("reg_id","UTF-8")+"="+ URLEncoder.encode(MainActivity.reg_no,"UTF-8")+"&"+
                    URLEncoder.encode("test_id","UTF-8")+"="+ URLEncoder.encode(test_id,"UTF-8");
            bw.write(post_data);
            bw.flush();
            bw.close();
            outputStream.close();

            InputStream inputStream=httpURLConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
            String result="";
            String line="";
            while((line=br.readLine())!=null)
            {
                result+=line;
            }
            br.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        pdd=new ProgressDialog(context);
        pdd.setCancelable(false);
        pdd.setMessage("Fetching scores");
        pdd.show();

    }

    @Override
    protected void onPostExecute(String result) {

        Log.e("Your Score is ","dg"+result);
        pdd.dismiss();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(String aVoid) {

        super.onCancelled(aVoid);
    }

    @Override
    protected void onCancelled() {

        super.onCancelled();
    }


}
