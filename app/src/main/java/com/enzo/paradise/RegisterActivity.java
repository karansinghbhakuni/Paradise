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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText eid,epass,ename,ephn;
    private Button bregister;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private TextView account,text_skip;
    String name="",number="";
    String MobilePattern = "[0-9]{10}";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        eid = findViewById(R.id.editText_email);
        epass = findViewById(R.id.editText_password);
        ename = findViewById(R.id.editText_name);
        ephn =findViewById(R.id.editText_phoneno);
        bregister = findViewById(R.id.button_register);
        text_skip = findViewById(R.id.text_skipregister);
        mAuth = FirebaseAuth.getInstance();
        account = findViewById(R.id.textView_register);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");

        text_skip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("logindata", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("loginCounter", true);
                editor.putString("userEmail", "Guest");
                editor.apply();
                startActivity(new Intent(RegisterActivity.this,DashboardActivity.class));
                finish();
            }
        });

        bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = eid.getText().toString().trim();
                String pass = epass.getText().toString().trim();
                name=ename.getText().toString().trim();
                number=ephn.getText().toString().trim();
                if (name.length() > 20) {

                    Toast.makeText(getApplicationContext(), "please enter less the 20 character in user name", Toast.LENGTH_SHORT).show();

                } else if (name.length() == 0 || email.length() ==0)
                {
                    if(name.length()==0)
                    {
                        ename.setError("Enter Your Name");
                        ename.setFocusable(true);
                    }
                    if(email.length()==0)
                    {
                        eid.setError("Enter Your Email");
                        eid.setFocusable(true);
                    }
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    eid.setError("Invalid Email");
                    eid.setFocusable(true);
                }else if(pass.length()<8)
                {
                    epass.setError("Password Length Atleast 8 Character");
                    epass.setFocusable(true);
                }
                else if(number.length()==0)
                {
                    ephn.setError("Enter your phone no.");
                    ephn.setFocusable(true);
                }
                else if(!number.matches(MobilePattern)){
                    ephn.setError("Enter a valid phone no.");
                    ephn.setFocusable(true);
                }
                else {
                    registeruser(email, pass); //registering meathod
                }
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
    private void registeruser (String email, String pass){
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            String email = user.getEmail();
                            String uid = user.getUid();
                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("uid", uid);
                            hashMap.put("name", name);
                            hashMap.put("phoneno",number);
                            hashMap.put("email", email);
                            hashMap.put("image", "");
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("users");
                            reference.child(uid).setValue(hashMap);
                  //        user = mAuth.getCurrentUser();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    //...
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, e.getMessage() + "",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}