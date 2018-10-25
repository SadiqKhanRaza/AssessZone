package sadiq.raza.assesszone;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    RadioButton rb1,rb2,rb3,rb4;
    TextView qTime,aTime,totalTime,currTime;
    RadioGroup radioGroup;
    String name;
    private int i=0,length;
    private  MyDataStructure s;
    TextView tv_name,tv_ques;
    Button next,prev,clear;
    static ArrayList<MyDataStructure> al;
    private ArrayList<String> options;
    private int responseArray[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        rb1=findViewById(R.id.opt1);
        rb2=findViewById(R.id.opt2);
        rb3=findViewById(R.id.opt3);
        rb4=findViewById(R.id.opt4);

        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);
        prev.setEnabled(false);
        clear = findViewById(R.id.clear);

        next.setOnClickListener(this);
        prev.setOnClickListener(this);
        clear.setOnClickListener(this);

        qTime=findViewById(R.id.qTime);
        aTime=findViewById(R.id.aTime);
        totalTime=findViewById(R.id.totalTime);
        currTime=findViewById(R.id.currTime);
        currTime.setText("45:00");

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
        responseArray=new int[length];

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int index) {

                if (rb1.isChecked()){
                    responseArray[i]=1;
                }else if (rb2.isChecked()){
                    responseArray[i]=2;

                }else if (rb3.isChecked()){
                    responseArray[i]=3;

                }else if (rb4.isChecked()){
                    responseArray[i]=4;

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
                        // if this button is clicked, close
                        // current activity
                        //length = al.size();
                        s = al.get(0);

                        tv_ques.setText(Html.fromHtml(s.getQues()));
                        options=s.getOption();
                        setOption(options);
                        qTime.setText("Questions "+(i+1)+"/"+length);
                        aTime.setText(format("Attempted %d", questionAttempted()));
                        Log.e("s", "" + al.size() + "  " + al);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                        finish();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

        //question=testBackgroundTask.question;

        s = new MyDataStructure();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case  R.id.next:
            {

                prev.setEnabled(true);

                if(i==length-1)
                {
                    AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(
                            Main2Activity.this);

                    alertDialogBuilder2.setTitle("Submit Test");

                    // set dialog message
                    alertDialogBuilder2.setMessage("Are you sure you want to submit");
                    alertDialogBuilder2.setCancelable(false);
                    alertDialogBuilder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            /*int ans=findScore();
                            StringBuilder sb = new StringBuilder("");
                            int ansArr[]=getAnswer();
                            for(int j=0;j<length;j++)
                                sb.append(" ").append(ansArr[j]);
                            Log.e("response: ",sb.toString());
                            Toast.makeText(Main2Activity.this, "Submitted Successfully : "+0, Toast.LENGTH_SHORT).show();*/
                            dialog.cancel();
                            finish();
                        }

                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog2=alertDialogBuilder2.create();
                    alertDialog2.show();
                }
                else if(i<length-1)
                    {
                        //next.setEnabled(true);
                        //Log.e("AAAAAAAAAAAAA", "01 i l "+i+"  "+length);
                        next.setText(R.string.next);
                        i++;

                        s=al.get(i);

                        tv_ques.setText(Html.fromHtml((s.getQues())));
                        options=s.getOption();

                        setOption(options);
                        qTime.setText("Questions "+(i+1)+"/"+length);
                        aTime.setText(format("Attempted %d", questionAttempted()));
                        radioGroup.clearCheck();
                        if(responseArray != null && responseArray[i]!=0)
                        {
                            if(responseArray[i]==1)
                                rb1.setChecked(true);
                            else if(responseArray[i]==2)
                                rb2.setChecked(true);
                            else if(responseArray[i]==3)
                                rb3.setChecked(true);
                            else if(responseArray[i]==4)
                                rb4.setChecked(true);
                        }

                        if(i>=length-1)
                        {
                            next.setText(R.string.submit);
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
                    qTime.setText(new StringBuilder().append("Questions ").append(i + 1).append("/").append(length).toString());
                    aTime.setText(format("Attempted %d", questionAttempted()));
                    radioGroup.clearCheck();
                    if(responseArray[i]!=0)
                    {
                        if(responseArray[i]==1)
                            rb1.setChecked(true);
                        else if(responseArray[i]==2)
                            rb2.setChecked(true);
                        else if(responseArray[i]==3)
                            rb3.setChecked(true);
                        else if(responseArray[i]==4)
                            rb4.setChecked(true);
                    }
                }
                break;
            }
            case R.id.clear:
            {
                responseArray[i]=0;
                radioGroup.clearCheck();
                StringBuilder sb = new StringBuilder("");
                for(int j=0;j<length;j++)
                    sb.append(" ").append(responseArray[j]);
                Log.e("response: ",sb.toString());
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
        //HomePage.openTestPb.dismiss();
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }

    @Override
    protected void onRestart() {
        finish();
        super.onRestart();
    }
    private  int questionAttempted()
    {
        int count=0;
        for(int i=0;i<length;i++)
        {
            if(responseArray[i]!=0)
                count++;
        }
        return count;
    }
    /*private int findScore()
    {
        int arr[]=getAnswer();
        int ans=0;
        for(int i=0;i<length;i++)
        {
            if(responseArray[i]==arr[i])
                ans++;
        }
        return ans;
    }
    private int[] getAnswer()
    {
        int arr[]=new int[length];
        for(int i=0;i<length;i++)
        {
            arr[i]=Integer.parseInt(al.get(i).getAns());
        }
        return arr;
    }*/
}
