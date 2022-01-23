package com.example.tirhal;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity2 extends AppCompatActivity {
    EditText ed_email_login, ed_pass_login;
    Button btn_log;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for changing status bar icon colors
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login2);
        ed_email_login = findViewById(R.id.edTxtEmailrregist);
        ed_pass_login = findViewById(R.id.edTxtPasswordregi);
        btn_log = findViewById(R.id.btn_login);
        mAuth = FirebaseAuth.getInstance();
        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });


    }//end onCreate method

    private void loginUser() {
        String email = ed_email_login.getText().toString();
        String pass = ed_pass_login.getText().toString();
        if (TextUtils.isEmpty(email)) {
            ed_email_login.setError("email must be written");
            ed_email_login.requestFocus();
        } else if (TextUtils.isEmpty(pass)) {
            ed_pass_login.setError("pass must be written");
            ed_pass_login.requestFocus();

        }else
        {
            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(LoginActivity2.this, "user Login successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity2.this,HomeActivity.class));
                    }else
                    {
                        Toast.makeText(LoginActivity2.this, "login Error"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void onLoginClick(View View) {
        startActivity(new Intent(this, RegistrationActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);

    }

/* <item name="colorPrimary">@color/pu</item>
        <item name="colorPrimaryVariant">@color/purple_700</item>
        <item name="colorOnPrimary">@color/white</item>
        <!-- Secondary brand color. -->
        <item name="colorSecondary">@color/teal_200</item>
        <item name="colorSecondaryVariant">@color/teal_700</item>*/

}