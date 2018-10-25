package sadiq.raza.assesszone;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
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

/**
 * Created by Sadiq on 3/11/2018.
 */

public class BackgroundTask2 extends AsyncTask<String,Void,String> {
    Context context;


    public BackgroundTask2(Context context) {
        this.context=context;
    }

    @Override
    protected String doInBackground(String... param) {

        String type =param[0];
        String register_url="https://assesszone.000webhostapp.com/client/register.php";


//REGISTER
        if(type.equals("register")) try {
            String name=param[1];
            String reg=param[2];
            String college=param[3];
            String email=param[4];
            String password=param[5];

            URL url = new URL(register_url);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setReadTimeout(6000);
            httpURLConnection.setConnectTimeout(7000);
            OutputStream outputStream=httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String post_data= URLEncoder.encode("name","UTF-8")+"="+ URLEncoder.encode(name,"UTF-8")+"&"+
                    URLEncoder.encode("reg_id","UTF-8")+"="+ URLEncoder.encode(reg,"UTF-8")+"&"+
                    URLEncoder.encode("college","UTF-8")+"="+ URLEncoder.encode(college,"UTF-8")+"&"+
                    URLEncoder.encode("email","UTF-8")+"="+ URLEncoder.encode(email,"UTF-8")+"&"+
                    URLEncoder.encode("password","UTF-8")+"="+ URLEncoder.encode(password,"UTF-8");
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

    }

    @Override
    protected void onPostExecute(String result) {
        Log.e("res","j"+result);
            SignUp.progressDialog.dismiss();
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setAction("CLOSE_SIGNUP");
                context.sendBroadcast(intent);
            }
        }, 1500);


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
