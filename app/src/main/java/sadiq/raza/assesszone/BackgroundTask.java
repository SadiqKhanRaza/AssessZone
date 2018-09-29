package sadiq.raza.assesszone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class BackgroundTask extends AsyncTask<String,Void,String> {
    Context context;
    AlertDialog alertDialog;

    public String name;
    public String email;
    public String jsonString;

    public BackgroundTask(Context context) {
        this.context=context;
    }

    @Override
    protected String doInBackground(String... param) {

        String type =param[0];
        String login_url="https://sadiqkhanraza.000webhostapp.com/login.php";
       if(type.equals("login")) try {
            String reg_id=param[1];
            String u_password=param[2];
            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream=httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String post_data= URLEncoder.encode("reg_id","UTF-8")+"="+ URLEncoder.encode(reg_id,"UTF-8")+"&"+
                    URLEncoder.encode("u_password","UTF-8")+"="+ URLEncoder.encode(u_password,"UTF-8");
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

        alertDialog= new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");
        //alertDialog.setMessage("HI");
        //alertDialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            MainActivity.mProgressBar.dismiss();


            alertDialog.setMessage(result);
           alertDialog.show();
            if(result!=null)
            {
                load(result);
                Intent intent =new Intent(context,HomePage.class);
                Bundle extras=new Bundle();
                extras.putString("myStr1",name );
                extras.putString("myStr2",email );
                intent.putExtras(extras);
                context.startActivity(intent);
                alertDialog.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(context,"Excep in fetching ", Toast.LENGTH_SHORT).show();
        }

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
    private String load(String json) throws JSONException {

        JSONObject jsonArray = new JSONObject(json);
         name=jsonArray.getString("name");
         email=jsonArray.getString("email");

        Toast.makeText(context,"Hi There "+name+"\nemail :  "+email, Toast.LENGTH_SHORT).show();

return  null;

    }


}
