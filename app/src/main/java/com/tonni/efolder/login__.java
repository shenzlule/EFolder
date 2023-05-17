package com.tonni.efolder;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login__ extends AppCompatActivity {

    private ImageView sign_or;
    TextView SignNow;
    EditText emailLogin;
    EditText passwordLogin;
    ProgressBar progressBarSign;
    Button loginBtn;

    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
            startActivity(intent);
            finish();

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);



        loginBtn =findViewById(R.id.loginbtn);
        progressBarSign=findViewById(R.id.prog_login);


        SignNow=findViewById(R.id.RegisterNow);

        emailLogin = (EditText) findViewById(R.id.email_login);
        passwordLogin = (EditText) findViewById(R.id.password_login);
        mAuth=FirebaseAuth.getInstance();


        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);

        SignNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),signUP.class);
                startActivity(intent);
                finish();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email_ ,password_;
                email_=String.valueOf(emailLogin.getText());
                password_=String.valueOf(passwordLogin.getText());

                if(TextUtils.isEmpty(email_)){
                    Toast.makeText(getApplicationContext(), "email is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password_)){

                    Toast.makeText(getApplicationContext(), "password is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email_, password_)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBarSign.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(login__.this, "Sign in Sucessfull", Toast.LENGTH_SHORT).show();

                                    Intent intent= new Intent(getApplicationContext(),MenuActivity.class);
                                    finish();
                                } else {

                                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });







    }
}
