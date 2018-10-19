package sadiq.raza.assesszone;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.Map;

/**
 * Created by Sadiq on 3/11/2018.
 */

public class AvailableTestBt extends AsyncTask<String,Void,String> {
    Context context;
    ProgressDialog pd ;
    String test_id,test_name;

    public AvailableTestBt(Context context) {
        this.context=context;
    }

    @Override
    protected String doInBackground(String... param) {


        String test_list="https://assesszone.000webhostapp.com/client/getTest.php";
         try {
            String reg_id= "11505615";
            URL url = new URL(test_list);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream=httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String post_data= URLEncoder.encode("reg_id","UTF-8")+"="+ URLEncoder.encode(reg_id,"UTF-8");
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
        pd=new ProgressDialog(context);
        pd.setTitle("Loading test details");
        pd.setCancelable(false);

        pd.show();

    }

    @Override
    protected void onPostExecute(String result) {
        Log.e("Result ", result);
        try
        {
            if(result!=null)
                loadTestDetails(result);
            else
                Log.e("ss","null");

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        pd.dismiss();
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
    private void loadTestDetails(String json)throws  JSONException
    {
        JSONObject jsonObject = new JSONArray(json).getJSONObject(0);//for only one test at zero index
         test_id=jsonObject.getString("test_id");
         test_name=jsonObject.getString("test_name");
        String start_date_time=jsonObject.getString("start_date_time");
        String time_allowed=jsonObject.getString("time_allowed");
        Log.e("tes : ",test_id+" "+test_name);
    }


}
