package sadiq.raza.assesszone;

import android.content.DialogInterface;
import android.os.Bundle;
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

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    RadioButton rb1,rb2,rb3,rb4;
    RadioGroup radioGroup;
    String name;
    private int i=0,length;
    private  MyDataStructure s;
    private TestBackgroundTask testBackgroundTask;
    private int quesNo;
    TextView tv_name,tv_ques;
    Button next,prev,clear;
    private String question;
    private ArrayList<MyDataStructure> al;
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
        radioGroup=findViewById(R.id.radioGroup);
        name = getIntent().getExtras().getString("nameValue");
        tv_name = findViewById(R.id.nameTest);
        tv_ques = findViewById(R.id.qText);
        tv_name.setText(name);

        testBackgroundTask = new TestBackgroundTask(Main2Activity.this);
        testBackgroundTask.execute();
        int j=0;
        boolean flag=testBackgroundTask.isComplete();
        while (!flag&& j<5000)
        {
            Log.e("j ",""+j++);
            flag=testBackgroundTask.isComplete();

        }
        al = testBackgroundTask.getQuestion();
        responseArray=new int[100];

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int index) {
                switch (index)
                {
                    case R.id.opt1:
                    {
                        responseArray[i]=1;
                        break;
                    }
                    case R.id.opt2:
                    {
                        responseArray[i]=2;
                        break;
                    }
                    case R.id.opt3:
                    {
                        responseArray[i]=3;
                        break;
                    }
                    case R.id.opt4:
                    {
                        responseArray[i]=4;
                        break;
                    }

                }
            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Main2Activity.this);

        // set title
        alertDialogBuilder.setTitle("Confirm");

        // set dialog message
        alertDialogBuilder
                .setMessage("Start Test")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        length = testBackgroundTask.quesList.size();
                        s = al.get(0);

                        tv_ques.setText(Html.fromHtml(s.getQues().toString()));
                        options=s.getOption();
                        setOption(options);
                        Log.e("s", "" + al.size() + "  " + al);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

        //question=testBackgroundTask.question;

        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);
        clear = findViewById(R.id.clear);
        s = new MyDataStructure();

        next.setOnClickListener(this);
        prev.setOnClickListener(this);
        clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case  R.id.next:
            {
                prev.setEnabled(true);
                if(i>=length-1)
                {
                    Log.e("response : ",""+responseArray[0]+" "+responseArray[1]+" "
                            +responseArray[2]+" "+responseArray[3]);
                    next.setEnabled(false);

                }
                else
                    {
                        //next.setEnabled(true);
                        //Log.e("AAAAAAAAAAAAA", "01 i l "+i+"  "+length);
                        i++;
                        s=al.get(i);

                        tv_ques.setText(Html.fromHtml((s.getQues()).toString()));
                        options=s.getOption();
                        setOption(options);
                    Log.e("AAAAAAAAAAAAA", "01 i l "+i+"  "+length);

                }
                break;
            }
            case R.id.prev:
            {
                next.setEnabled(true);
                if(i<1)
                {
                    prev.setEnabled(false);

                }
                else
                {
                   // prev.setEnabled(true);
                    i--;
                    s=al.get(i);
                    tv_ques.setText(Html.fromHtml(s.getQues().toString()));
                    options=s.getOption();
                    setOption(options);
                }
            }
            case R.id.clear:
            {
                responseArray[i]=0;
                rb1.setChecked(false);
                rb2.setChecked(false);
                rb3.setChecked(false);
                rb4.setChecked(false);
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
            Toast.makeText(this, "Size of ArrayList is less", Toast.LENGTH_SHORT).show();
    }
}
