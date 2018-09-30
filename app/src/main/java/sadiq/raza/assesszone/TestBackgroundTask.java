package sadiq.raza.assesszone;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
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
import java.util.ArrayList;

/**
     * Created by Sadiq on 3/11/2018.
     */
    public class TestBackgroundTask extends AsyncTask<String,Void,String> {
        Context context;
        AlertDialog alertDialog;
        ArrayList<MyDataStructure> quesList = new ArrayList<>();
        public String question;

        public TestBackgroundTask(Context context) {
            this.context=context;
        }

        @Override
        protected String doInBackground(String... param) {

            //String type =param[0];
            String question_url="https://assesszone.000webhostapp.com/client/getQuestions.php";
            /*if(type.equals("login"))*/ try {
                //String testId=param[1];

                URL url = new URL(question_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("test_id","UTF-8")+"="+ URLEncoder.encode("27","UTF-8");
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
            question=result;
            //Toast.makeText(context, ""+question, Toast.LENGTH_SHORT).show();
            try {

                if(result!=null)
                {
                    load(result);
                    Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();

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
           // Log.e("AAAAAAAAAA,", json);
            JSONArray arr = new JSONArray(json);
            for (int i=0; i<arr.length(); i++)
            {
                JSONArray curr = arr.getJSONArray(i);
                Log.e("AAkAAAAA "+arr.length()+" " +i, " "+arr.getJSONArray(i)+" he");
                    String question = curr.getString(0);
                    String optionList = curr.getString(1);
                    String ans = curr.getString(2);
                    //Log.e("BBBBBB "+j, "a is "+a+"\n b is "+b+"\n c is "+c);
                    JSONObject opt = new JSONObject(optionList);
                    String option1= opt.getString("opt1");
                    String option2= opt.getString("opt2");
                    String option3= opt.getString("opt3");
                    String option4= opt.getString("opt4");

                    JSONObject a=new JSONObject(ans);
                    String answer= a.getString("ans1");
                    //Toast.makeText(context, "" +question, Toast.LENGTH_SHORT).show();
                    //Log.e("Dsd",question+"\n"+option1+" "+option2+" "+option3+" "+option4+" " +answer);
                    ArrayList<String > optionAl = new ArrayList<>();
                    optionAl.add(option1);
                    optionAl.add(option2);
                    optionAl.add(option3);
                    optionAl.add(option4);
                    quesList.add(new MyDataStructure(question,optionAl,answer));
                Log.e("size",""+quesList.size()+" "+quesList);

            }
//            Toast.makeText(context, ""+jsonArray, Toast.LENGTH_SHORT).show();

            return  null;

        }
        public boolean isComplete()
        {
            if(quesList.size()<1)
                return false;
            return true;
        }
    public ArrayList<MyDataStructure> getQuestion()
    {
        return quesList;
    }


    }