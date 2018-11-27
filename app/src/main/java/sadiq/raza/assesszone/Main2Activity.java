package sadiq.raza.assesszone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.String.format;
import static  sadiq.raza.assesszone.AvailableTestBt.testBackgroundTask;
import static sadiq.raza.assesszone.AvailableTestBt.time_allowed;

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
import java.util.Locale;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    RadioButton rb1,rb2,rb3,rb4;
    TextView qTime,aTime,totalTime,currTime;
    RadioGroup radioGroup;
    String name;
    private int i=0,length;
    private  MyDataStructure s;
    TextView tv_name,tv_ques;
    Button next,prev,clear;
      Button submit;
      static Context context;
      static  int ans;
    static ArrayList<MyDataStructure> al;
    private ArrayList<String> options;
    private String responseArray[];
    private long startTime,timeInMilliseconds,updatedTime;
    private Handler customHandler= new Handler();
    int attempt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        context=Main2Activity.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        rb1=findViewById(R.id.opt1);
        rb2=findViewById(R.id.opt2);
        rb3=findViewById(R.id.opt3);
        rb4=findViewById(R.id.opt4);

        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);
        submit = findViewById(R.id.submit);
        prev.setEnabled(false);
        clear = findViewById(R.id.clear);

        next.setOnClickListener(this);
        prev.setOnClickListener(this);
        clear.setOnClickListener(this);
        submit.setOnClickListener(this);

        qTime=findViewById(R.id.qTime);
        aTime=findViewById(R.id.aTime);
        totalTime=findViewById(R.id.totalTime);
        currTime=findViewById(R.id.currTime);
        //currTime.setText("45:00");

        radioGroup=findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
        name =HomePage.s_name;
        tv_name = findViewById(R.id.nameTest);
        tv_ques = findViewById(R.id.qText);
        tv_name.setText(name);


        al = testBackgroundTask.getQuestion();
        Log.e("sssss","as"+al.size()+"  "+al);
        length=testBackgroundTask.getLength();
        qTime.setText("Questions 1/"+length);
        responseArray=new String [length];
        time_allowed="2";

        totalTime.setText(time_allowed+" : 00");



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int index) {

                if (rb1.isChecked()){
                    responseArray[i]=rb1.getText().toString();
                }else if (rb2.isChecked()){
                    responseArray[i]=rb2.getText().toString();

                }else if (rb3.isChecked()){
                    responseArray[i]=rb3.getText().toString();

                }else if (rb4.isChecked()){
                    responseArray[i]=rb4.getText().toString();

                }

            }
        });



        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Main2Activity.this);

        // set title
        alertDialogBuilder.setTitle("Confirm");

        // set dialog message
        alertDialogBuilder
                .setMessage("Do not move from this activity otherwise test will be closed\n\n\t\tStart Test")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        startTime = SystemClock.uptimeMillis();
                        customHandler.removeCallbacks(updateTimerThread);
                        customHandler.postDelayed(updateTimerThread, 0);
                        s = al.get(0);

                        tv_ques.setText(Html.fromHtml(s.getQues()));
                        tv_ques.setIncludeFontPadding(false);
                        options=s.getOption();
                        setOption(options);
                        qTime.setText(getString(R.string.quesByTotal)+(i+1)+"/"+length);
                        aTime.setText(String.format(Locale.US,"Attempted %d", questionAttempted()));
                        Log.e("s", "" + al.size() + "  " + al);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        s = new MyDataStructure();
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case  R.id.next:
            {

                prev.setEnabled(true);
                 if(i<length-1)
                    {
                        next.setText(R.string.next);
                        i++;

                        s=al.get(i);

                        tv_ques.setText(Html.fromHtml((s.getQues())));
                        options=s.getOption();

                        setOption(options);
                        qTime.setText("Questions "+(i+1)+"/"+length);
                        aTime.setText(format("Attempted %d", questionAttempted()));
                        radioGroup.clearCheck();
                        if(responseArray != null && responseArray[i]!=null)
                        {
                            if(responseArray[i].equals(rb1.getText().toString()))
                                rb1.setChecked(true);
                            else if(responseArray[i].equals(rb2.getText().toString()))
                                rb2.setChecked(true);
                            else if(responseArray[i].equals(rb3.getText().toString()))
                                rb3.setChecked(true);
                            else if(responseArray[i].equals(rb4.getText().toString()))
                                rb4.setChecked(true);
                        }

                        if(i>=length-1)
                        {
                            next.setEnabled(false);
                        }
                    Log.e("AAAAAAAAAAAAA", "01 i l "+i+"  "+length);

                }
                break;
            }
            case R.id.prev:
            {

                next.setEnabled(true);
                if(i==length-1)
                    next.setText(R.string.next);
                if(i<1)
                {
                    prev.setEnabled(false);

                }
                else
                {
                   // prev.setEnabled(true);
                    i--;
                    s=al.get(i);
                    tv_ques.setText(Html.fromHtml(s.getQues()));
                    options=s.getOption();
                    setOption(options);
                    qTime.setText(getString(R.string.Ques) + (i + 1) + "/" +
                            length);
                    aTime.setText(format("Attempted %d", questionAttempted()));
                    radioGroup.clearCheck();
                    if(responseArray != null && responseArray[i]!=null)
                    {
                        if(responseArray[i].equals(rb1.getText().toString()))
                            rb1.setChecked(true);
                        else if(responseArray[i].equals(rb2.getText().toString()))
                            rb2.setChecked(true);
                        else if(responseArray[i].equals(rb3.getText().toString()))
                            rb3.setChecked(true);
                        else if(responseArray[i].equals(rb4.getText().toString()))
                            rb4.setChecked(true);
                    }
                }
                break;
            }
            case R.id.clear:
            {
                responseArray[i]=null;
                radioGroup.clearCheck();
                StringBuilder sb = new StringBuilder("");
                for(int j=0;j<length;j++)
                    sb.append(" ").append(responseArray[j]);
                Log.e("response: ",sb.toString());
                break;
            }
            case R.id.submit:
            {
                AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(
                        Main2Activity.this);

                alertDialogBuilder2.setTitle("Submit Test");
                alertDialogBuilder2.setMessage("Are you sure you want to submit");
                alertDialogBuilder2.setCancelable(false);
                alertDialogBuilder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                         ans=findScore();
                        new ScoreBackgroundTask2().execute();
                        dialog.cancel();
                    }

                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog2=alertDialogBuilder2.create();
                alertDialog2.show();
                break;
            }
        }

    }
    public void setOption(ArrayList<String> al)
    {
        if(al.size()>=4)
        {
            rb1.setText(al.get(0));
            rb2.setText(al.get(1));
            rb3.setText(al.get(2));
            rb4.setText(al.get(3));
        }
        else
            Toast.makeText(this, "Size of Question list is less", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Not Allowed to go back", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        //customHandler.removeCallbacks(updateTimerThread);

        super.onPause();
    }

    @Override
    protected void onRestart() {
        //customHandler.removeCallbacks(updateTimerThread);
        if(attempt>2)
            new ScoreBackgroundTask2().execute();
        else
            Toast.makeText(context, "Not Allowed", Toast.LENGTH_SHORT).show();
        attempt++;
        super.onRestart();
    }
    private  int questionAttempted()
    {
        int count=0;
        for(int i=0;i<length;i++)
        {
            if(responseArray[i]!=null)
                count++;
        }
        return count;
    }
    private int findScore()
    {
        String arr[]=getAnswer();
        int ans=0;
        for(int i=0;i<length;i++)
        {
            Log.e("Res : arr[i] ",responseArray[i]+" : "+arr[i]);
            if(responseArray[i]!=null && responseArray[i].equals(arr[i]))
                ans++;
        }
        return ans;
    }
    private String[] getAnswer()
    {
        String arr[]=new String[length];
        for(int i=0;i<length;i++)
        {
            arr[i]=al.get(i).getAns();
        }
        return arr;
    }
        boolean flag=true;
    private Runnable updateTimerThread = new Runnable() {
        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime =timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            if(flag && mins==Integer.valueOf(time_allowed)-1)
            {
                Toast.makeText(Main2Activity.this, "Your test is about to end !", Toast.LENGTH_SHORT).show();
                flag=false;
            }
            secs = secs % 60;
            currTime.setText("" + mins + " : "
                    + String.format("%02d", secs));
            if(mins==Integer.valueOf(time_allowed))
            {
                customHandler.removeCallbacks(updateTimerThread);
                new ScoreBackgroundTask2().execute();
            }
            else
            customHandler.postDelayed(this, 0);
        }

    };

}

class ScoreBackgroundTask2 extends AsyncTask<String,Void,String> {
    android.app.AlertDialog alertDialog;
//
//    public String name;
//    public String email;
//    public String jsonString;
    String test_id;
    //String scores;
     ProgressDialog progressDialog;
    @Override
    protected String doInBackground(String... param) {
        test_id=testBackgroundTask.getTestId();


        String login_url="https://assesszone.000webhostapp.com/client/testScores.php";
        try {
            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setReadTimeout(9000);
            httpURLConnection.setConnectTimeout(9000);
            OutputStream outputStream=httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            Log.e("sent",MainActivity.reg_no+" "+test_id+" "+Main2Activity.ans);
            String post_data= URLEncoder.encode("reg_id","UTF-8")+"="+ URLEncoder.encode(MainActivity.reg_no,"UTF-8")+"&"+
                    URLEncoder.encode("test_id","UTF-8")+"="+ URLEncoder.encode(test_id,"UTF-8")+"&"+
                    URLEncoder.encode("scores","UTF-8")+"="+ URLEncoder.encode(String.valueOf(Main2Activity.ans),"UTF-8");
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
        progressDialog=new ProgressDialog(Main2Activity.context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Submitting scores");
        progressDialog.show();

    }

    @Override
    protected void onPostExecute(String result) {
        progressDialog.dismiss();
        Log.e("result","s"+result);
        if(result!=null&&result.equals("1")&& AvailableTestBt.practice)
            Toast.makeText(Main2Activity.context, "Your Score is "+ Main2Activity.ans, Toast.LENGTH_LONG).show();
        else if(result!=null&&result.equals("1"))
            Toast.makeText(Main2Activity.context, "Submitted Successfully", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(Main2Activity.context, "Error Occured in Submission", Toast.LENGTH_SHORT).show();
        ((Activity)Main2Activity.context).finish();

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(String aVoid) {


        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }super.onCancelled(aVoid);
    }

    @Override
    protected void onCancelled() {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }super.onCancelled();
    }


}
