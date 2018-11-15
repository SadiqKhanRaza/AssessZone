package sadiq.raza.assesszone;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    static String reg_no;
    ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reg=findViewById(R.id.reg);
        logIn=findViewById(R.id.login);
        lreg_no=findViewById(R.id.uname);
        lpassword=findViewById(R.id.upass);
        forgotPass=findViewById(R.id.fPass);
        connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Service Not Available yet", Toast.LENGTH_SHORT).show();
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isConnected())
                {
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }


                 reg_no=lreg_no.getText().toString();
                String password =lpassword.getText().toString();
                String type="login";
                if(reg_no.length()<3||password.length()<3|| !reg_no.matches("[0-9]*"))
                    Toast.makeText(MainActivity.this, "Please Enter valid Details", Toast.LENGTH_SHORT).show();
                else
                {
                    BackgroundTask backgroundTask = new BackgroundTask(MainActivity.this);
                    backgroundTask.execute(type,reg_no,password);
                }

            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isConnected())
                {
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(MainActivity.this,SignUp.class);
                startActivity(i);
            }
        });




    }
    public boolean isConnected()
    {
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected())
        {
            return networkInfo.getType() == ConnectivityManager.TYPE_WIFI ||
                    networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        }
        return  false;
    }

}

