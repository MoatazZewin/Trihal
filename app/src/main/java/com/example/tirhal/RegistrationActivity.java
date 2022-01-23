package com.example.tirhal;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegistrationActivity extends AppCompatActivity {
    Button btn_register;
    EditText ed_name, ed_email, ed_pass, ed_phone;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        changeStatusBarColor();
        initComponents();
        mAuth = FirebaseAuth.getInstance();
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });



    }

    //to create a new user account with email and password
    private void createUser()
    {
        String email = ed_email.getText().toString();
        String pass = ed_pass.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            ed_email.setError("email must be written");
            ed_email.requestFocus();
        }else if(TextUtils.isEmpty(pass))
        {
            ed_pass.setError("pass must be written");
            ed_pass.requestFocus();
        }else {
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(RegistrationActivity.this, "user Registered successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistrationActivity.this,LoginActivity2.class));
                    }else
                    {
                        Toast.makeText(RegistrationActivity.this, "Registration Error"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }//end createUser

    // method to init the components
    public void initComponents()
    {
        btn_register = findViewById(R.id.btn_login);
        ed_name = findViewById(R.id.editTextNameRegister);
        ed_email = findViewById(R.id.edTxtEmailrregist);
        ed_pass = findViewById(R.id.edTxtPasswordregi);
        ed_phone = findViewById(R.id.editTextMobile);
    }// end initComponents

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity2.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);

    }



}
