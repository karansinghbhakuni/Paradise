package com.enzo.paradise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    TextView text_help,text_register,text_skip;
    EditText text_email,text_password;
    Button btn_login;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        text_help=findViewById(R.id.textView_help);
        text_register=findViewById(R.id.textView_register);
        text_email=findViewById(R.id.editText_email);
        text_password=findViewById(R.id.editText_password);
        btn_login=findViewById(R.id.button_login);
        text_skip=findViewById(R.id.text_skip);

        text_skip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("logindata", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("loginCounter", true);
                editor.putString("userEmail", "Guest");
                editor.apply();
                startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
                finish();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("...");
        text_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });
        text_help.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
              //  startActivity(new Intent(LoginActivity.this,RecoverActivity.class));
                finish();
            }

        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=text_email.getText().toString().trim();
                String pass=text_password.getText().toString().trim();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    text_email.setError("Invalid Email");
                    text_email.setFocusable(true);
                }
                if(pass.length()==0)
                {
                    text_password.setError("Password cannot be empty");
                    text_password.setFocusable(true);
                }
                else{
                    loginuser(email,pass);
                }
            }
        });
    }
    private void loginuser(final String email, String pass) {
        progressDialog.setMessage("Loggin in ...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            saveData(email);
                            finish();
                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Incorrect Id or Password.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Authentication Failed.",
                        Toast.LENGTH_SHORT).show();
             //   Toast.makeText(LoginActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    void saveData(final String email) {
        SharedPreferences sharedPreferences = getSharedPreferences("logindata", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("loginCounter", true);
        editor.putString("userEmail", email);
        editor.apply();
        Intent intent= new Intent(LoginActivity.this,DashboardActivity.class);
        startActivity(intent);
    }
}