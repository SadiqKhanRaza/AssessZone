package sadiq.raza.assesszone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
        int length;
        AlertDialog alertDialog;
        ArrayList<MyDataStructure> quesList = new ArrayList<>();
        public String question;
        private String testId;
        public TestBackgroundTask(Context context,String testId)
        {
            this.testId=testId;
            this.context=context;
        }

        @Override
        protected String doInBackground(String... param) {

            String question_url="https://assesszone.000webhostapp.com/client/getQuestions.php";
            try {
                URL url = new URL(question_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setReadTimeout(8000);
                httpURLConnection.setConnectTimeout(9000);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("test_id","UTF-8")+"="+ URLEncoder.encode(testId +
                        "","UTF-8");
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
        HomePage.openTestPb.show();
        HomePage.openTestPb.setCancelable(false);
        }


        @Override
        protected void onPostExecute(String result) {

            question=result;
            //Toast.makeText(context, ""+question, Toast.LENGTH_SHORT).show();
            try
            {
                if(result!=null)
                {
                    length=load(result);

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context,"No Questions Available ", Toast.LENGTH_SHORT).show();
            }
                HomePage.openTestPb.dismiss();
            if(quesList.size()>1)
                context.startActivity(new Intent(context,Main2Activity.class));


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
        private int load(String json) throws JSONException {
           // Log.e("AAAAAAAAAA,", json);
            JSONArray arr = new JSONArray(json);
            for (int i=0; i<arr.length(); i++)
            {
                JSONArray curr = arr.getJSONArray(i);
                //Log.e("AAkAAAAA "+arr.length()+" " +i, " "+arr.getJSONArray(i)+" he");
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
                //Log.e("size",""+quesList.size()+" "+quesList);

            }
//            Toast.makeText(context, ""+jsonArray, Toast.LENGTH_SHORT).show();

            return  quesList.size();

        }
        public int getLength()
        {
            return length;
        }
        public String getTestId(){return  testId;}
    public ArrayList<MyDataStructure> getQuestion()
    {
        return quesList;
    }


    }