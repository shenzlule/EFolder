package com.tonni.efolder;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signUP extends AppCompatActivity {

    private ImageView login_or;
    TextView loginNow;
    EditText email;
    EditText password ,password2;
    ProgressBar progressBarReg;

    FirebaseAuth mAuth;

    /////////////////////////////////////////////////////////////////////////////////

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

    ///////////////////////////////////////////////////////////////////////////////////



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        progressBarReg=findViewById(R.id.prog_reg);
        password2=findViewById(R.id.repassword);

        loginNow=findViewById(R.id.loginNow);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        mAuth=FirebaseAuth.getInstance();

        MaterialButton regbtn = (MaterialButton) findViewById(R.id.signupbtn);


        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),login__.class);
                startActivity(intent);
                finish();
            }
        });


        //TODO declared  click
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarReg.setVisibility(View.VISIBLE);


                String email_ ,password_;
                email_=String.valueOf(email.getText());
                password_=String.valueOf(password.getText());

                if(TextUtils.isEmpty(email_)){
                    Toast.makeText(signUP.this, "email is empty", Toast.LENGTH_SHORT).show();
                    progressBarReg.setVisibility(View.GONE);
                return;
                }

                if(TextUtils.isEmpty(password_)){

                    Toast.makeText(signUP.this, "password is empty", Toast.LENGTH_SHORT).show();
                    progressBarReg.setVisibility(View.GONE);
                return;
                }



                if(!Patterns.EMAIL_ADDRESS.matcher(email_).matches()) {
                    Toast.makeText(signUP.this, "invalid email", Toast.LENGTH_SHORT).show();
                    progressBarReg.setVisibility(View.GONE);
                    return;
                }


                mAuth.createUserWithEmailAndPassword(email_, password_)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressBarReg.setVisibility(View.GONE);
                                    // Sign in success, update UI with the signed-in user's information


                                    Toast.makeText(getApplicationContext(), "Register successfull",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
                                    startActivity(intent);

                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(getApplicationContext(), "Authentication failed. try again",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });

    }


}
