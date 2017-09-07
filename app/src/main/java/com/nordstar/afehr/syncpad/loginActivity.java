package com.nordstar.afehr.syncpad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends Activity {

    private FirebaseAuth auth;

    private Button loginButton;
    private TextView registerTextView;
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null){
            startActivity(new Intent(loginActivity.this, mainActivity.class));
        }

        //TODO Add remember me function

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_activity);

        //Setup Views
        loginButton = (Button) findViewById(R.id.registerButton);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        registerTextView = (TextView) findViewById(R.id.registerTextView);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText = emailEditText.getText().toString();
                String passwordText = passwordEditText.getText().toString();

                //TODO Create a loading icon
                auth.signInWithEmailAndPassword(emailText,passwordText)
                        .addOnCompleteListener(loginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(loginActivity.this, "Failed to login, error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                                else {
                                    startActivity(new Intent(loginActivity.this, mainActivity.class));
                                }
                            }
                        });
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginActivity.this, registerActivity.class));
            }
        });


    }
}
