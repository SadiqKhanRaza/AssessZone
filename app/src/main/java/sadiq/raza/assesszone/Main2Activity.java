package sadiq.raza.assesszone;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    String name;
    private int i=0,length;
    private  MyDataStructure s;
    private TestBackgroundTask testBackgroundTask;
    TextView tv_name,tv_ques;
    Button next,prev,clear;
    private String question;
    private ArrayList<MyDataStructure> al;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        name = getIntent().getExtras().getString("nameValue");
        tv_name = findViewById(R.id.nameTest);
        tv_ques = findViewById(R.id.qText);
        tv_name.setText(name);

        testBackgroundTask = new TestBackgroundTask(Main2Activity.this);
        testBackgroundTask.execute();
        al = testBackgroundTask.getQuestion();

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
                        tv_ques.setText(s.getQues());
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
                    next.setEnabled(false);

                }
                else
                    {
                        //next.setEnabled(true);
                        Log.e("AAAAAAAAAAAAA", "01 i l "+i+"  "+length);
                        i++;
                        s=al.get(i);

                        tv_ques.setText(s.getQues());
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
                    tv_ques.setText(s.getQues());
                }
            }
            case R.id.clear:
            {
                break;
            }
        }

    }
}
