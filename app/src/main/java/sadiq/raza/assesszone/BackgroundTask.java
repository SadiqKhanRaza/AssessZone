package sadiq.raza.assesszone;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.system.Os;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.conn.ConnectTimeoutException;
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
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Sadiq on 3/11/2018.
 */

public class BackgroundTask extends AsyncTask<String,Void,String> {
    Context context;
    AlertDialog alertDialog;

    public static String name;
    public static String email;
    public String jsonString;
    ProgressDialog progressDialog;
    public BackgroundTask(Context context) {
        this.context=context;
    }

    @Override
    protected String doInBackground(String... param) {

        String type =param[0];
        String login_url="https://assesszone.000webhostapp.com/client/login.php";
       if(type.equals("login")) try {
            String reg_id=param[1];
            String u_password=param[2];
           if (android.os.Build.VERSION.SDK_INT <21) {
               // only for gingerbread and newer versions
               trustEveryone();
               Log.e("Version",""+Build.VERSION.SDK_INT);
           }
            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setReadTimeout(4000);
            httpURLConnection.setConnectTimeout(4000);
            OutputStream outputStream=httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String post_data= URLEncoder.encode("reg_id","UTF-8")+"="+ URLEncoder.encode(reg_id,"UTF-8")+"&"+
                    URLEncoder.encode("password","UTF-8")+"="+ URLEncoder.encode(u_password,"UTF-8");
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


        }
        catch (ConnectTimeoutException e)
        {
            Log.e("Time out ",e.toString());
        }

        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
           Log.e("Time out ",e.toString());
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Authenticating your login..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        alertDialog= new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");
        //alertDialog.setMessage("HI");
        //alertDialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        try {
                progressDialog.dismiss();

            if(result!=null && result.length()<100)
            {
                alertDialog.setMessage(result);
                alertDialog.show();
                load(result);
                Intent intent =new Intent(context,HomePage.class);
                Bundle extras=new Bundle();
                extras.putString("myStr1",name );
                extras.putString("myStr2",email );
                intent.putExtras(extras);
                context.startActivity(intent);
                alertDialog.dismiss();
            }
            else
            {
                Log.e("rs","d"+result);
                Toast.makeText(context, "Wrong Credential", Toast.LENGTH_SHORT).show();
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
        Log.e("time " ,"timeout");
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
    private void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }});
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager(){
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }}}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    context.getSocketFactory());
        } catch (Exception e) { // should never happen
            e.printStackTrace();
        }
    }

}
