package sadiq.raza.assesszone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    CardView logIn;
    TextView reg,forgotPass;
    EditText lreg_no,lpassword;
   static ProgressDialog mProgressBar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        reg=findViewById(R.id.reg);
        logIn=findViewById(R.id.login);
        lreg_no=findViewById(R.id.uname);
        lpassword=findViewById(R.id.upass);
        mProgressBar= new ProgressDialog(this);
        forgotPass=findViewById(R.id.fPass);
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Service Not Available yet", Toast.LENGTH_SHORT).show();
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // mProgressBar.setCancelable(false);

                mProgressBar.setMessage("Authenticating your login..");
                mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressBar.setCancelable(false);


                String reg_no=lreg_no.getText().toString();
                String password =lpassword.getText().toString();
                String type="login";
                if(reg_no.length()<3||password.length()<3)
                    Toast.makeText(MainActivity.this, "Please Enter valid Details", Toast.LENGTH_SHORT).show();
                else
                {
                    BackgroundTask backgroundTask = new BackgroundTask(MainActivity.this);
                    backgroundTask.execute(type,reg_no,password);
                    mProgressBar.show();
                }

            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,SignUp.class);
                startActivity(i);
            }
        });




    }



}

