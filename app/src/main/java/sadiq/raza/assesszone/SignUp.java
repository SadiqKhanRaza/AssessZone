package sadiq.raza.assesszone;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

     EditText et_name,et_email,et_password,et_confirmPassword,et_college,et_regNo,et_cEmail;
     Button signUpUser;
     TextView linkLogin;
    static ProgressDialog progressDialog ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this);
        setContentView(R.layout.activity_sign_up);
        et_name=findViewById(R.id.name);
        et_email=findViewById(R.id.email);
        et_password=findViewById(R.id.password);
        et_confirmPassword=findViewById(R.id.confirm_password);
        et_college=findViewById(R.id.college);
        et_regNo=findViewById(R.id.reg_no);
        et_cEmail=findViewById(R.id.confirmEmail);
        signUpUser=findViewById(R.id.btn_signup);
        linkLogin=findViewById(R.id.link_login);


        signUpUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"hdskjdhsjkdk",Toast.LENGTH_SHORT).show();
                progressDialog.setMessage("Creating your account...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                String name = et_name.getText().toString();
                String regNo=et_regNo.getText().toString();
                String college=et_college.getText().toString();
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                if(signup())
                {
                    progressDialog.show();
                    String type="register";
                    BackgroundTask2 backgroundTask2 = new BackgroundTask2(SignUp.this);
                    backgroundTask2.execute(type,name,regNo,college,email,password);
                }
            }
        });

        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public boolean signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return false;
        }

        signUpUser.setEnabled(false);

        return true;
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();

        signUpUser.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String name = et_name.getText().toString();
        String regNo=et_regNo.getText().toString();
        String college=et_college.getText().toString();
        String email = et_email.getText().toString();
        String confirmEmail = et_cEmail.getText().toString();
        String password = et_password.getText().toString();
        String confirmPassword = et_confirmPassword.getText().toString();

        if (regNo.isEmpty() || regNo.length() < 3 || regNo.matches("0-9")) {
            et_regNo.setError("at least 3 characters");
            valid = false;
        }

        if (name.isEmpty() || name.length() < 3 ) {
            et_name.setError("at least 3 characters");
            valid = false;
        }
        else if(!name.matches("[a-zA-Z ]*"))
        {
            et_name.setError("Special character or number not allowed");
            valid=false;
        }

        else {
           et_name.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_email.setError("enter a valid email address");
            valid = false;
        } else {
            et_email.setError(null);
        }
        if(!confirmEmail.equals(email))
        {
            et_cEmail.setError("Email did not match");
            valid =false;
        }
        else {
            et_cEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            et_password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        }
        else if(!password.matches("[a-zA-Z0-9@#$]*"))
        {
            et_password.setError("This password not allowed");
            valid=false;
        }
        else {
            et_password.setError(null);
        }
        if(!confirmPassword.equals(password))
        {
            et_confirmPassword.setError("Password did not match");
            valid =false;
        }
        else {
            et_confirmPassword.setError(null);
        }

        if(!college.matches("[a-zA-Z ]*"))
        {
            et_college.setError("Please Enter valid college name");
            valid=false;
        }
        else
            et_college.setError(null);

        return valid;
    }


}