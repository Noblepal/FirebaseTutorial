package com.sample.firebasetutorial;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputEditText edtEmail, edtPassword;
    private Button btnLogin, btnRegister;
    private ProgressBar progress;
    private TextInputLayout layout_email, layout_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize auth
        mAuth = FirebaseAuth.getInstance();

        initUI();


    }

    //load all UI components
    private void initUI(){
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        progress = findViewById(R.id.progress_authentication);
        layout_email = findViewById(R.id.edt_email_layout);
        layout_pass = findViewById(R.id.edt_pass_layout);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                layout_email.setError(null);
                layout_pass.setError(null);
                 createAccount(Objects.requireNonNull(edtEmail.getText()).toString(),
                        Objects.requireNonNull(edtPassword.getText()).toString());
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                layout_email.setError(null);
                layout_pass.setError(null);
               logIn(Objects.requireNonNull(edtEmail.getText()).toString(),
                        Objects.requireNonNull(edtPassword.getText()).toString());
            }
        });

    }

    //disable items
    private void enableInputs(boolean disabled){
        edtPassword.setEnabled(disabled);
        edtEmail.setEnabled(disabled);
        btnLogin.setEnabled(disabled);
        btnRegister.setEnabled(disabled);
    }

    //remove items from edittexts
    private void removeInputs(){
        edtEmail.setText("");
        edtPassword.setText("");
    }

    //create account
    private void createAccount(String email, String password){
        progress.setVisibility(View.VISIBLE);
        enableInputs(false);
        //create account with email and password
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progress.setVisibility(View.GONE);
                enableInputs(true);
                //display dialog if account choice is successful
                if(task.isSuccessful()) displayDialog(); else {layout_email.setError("This email is already in use.");}
            }
        });
    }

    //log in user
    private void logIn(String email, String password){
        progress.setVisibility(View.VISIBLE);
        enableInputs(false);
        // sign in user
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progress.setVisibility(View.GONE);
                enableInputs(true);
                //display dialog if account choice is successful
                if(task.isSuccessful()) displayDialog(); else {layout_pass.setError("Credentials do not match.");}
            }
        });

    }

    private void displayDialog(){
        String[] options = {"LANDLORD","TENANT"};
        int checkedItem = 1;

        //Use material dialog builder
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setTitle("Which service do you want?")
                .setSingleChoiceItems(options, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeInputs();
                        //start activity with selected inputs
                        startActivity(new Intent(MainActivity.this,Home.class).putExtra("SELECTION",which));
                    }
                });
        builder.setCancelable(false)
                .create()
                .show();


    }


}