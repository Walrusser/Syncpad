package com.nordstar.afehr.syncpad;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class registerActivity extends Activity {

    private FirebaseAuth auth;

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_activity);

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        confirmPasswordEditText = (EditText) findViewById(R.id.confirmPasswordEditText);
        registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())) && (passwordEditText.getText().toString().length() > 6)){
                    String email = emailEditText.getText().toString();
                    String password = passwordEditText.getText().toString();

                    auth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(registerActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful()){
                                        Toast.makeText(registerActivity.this, "Failed to create user, error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    } else{
                                        startActivity(new Intent(registerActivity.this, loginActivity.class));
                                    }
                                }
                            });
                } else{
                    if(passwordEditText.getText().toString().length() > 6){
                        Toast.makeText(registerActivity.this, "The password is not long enough, please use atleast 6 characters", Toast.LENGTH_LONG).show();
                    } else{
                        Toast.makeText(registerActivity.this, "The passwords do not match", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

}
