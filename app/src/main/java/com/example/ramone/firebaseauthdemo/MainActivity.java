package com.example.ramone.firebaseauthdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static android.R.attr.button;
import static com.example.ramone.firebaseauthdemo.R.id.conditionTextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignin;


    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    //References to the Database
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mDatabase.child("Name");


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //Pointing to the database
        mDatabase = FirebaseDatabase.getInstance().getReference();


        if (firebaseAuth.getCurrentUser() != null){
            //User already logged in
            //Start camera activity here
            finish();
            startActivity(new Intent(getApplicationContext(), Profile_Activity.class));
        }

        //Initializing Views
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);

        progressDialog = new ProgressDialog(this);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        //Attaching listener to button
        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
    }


    private void registerUser(){

        //Getting email and password from edit texts
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //Checking if email and password is empty
        if (TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Pleas enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this, "Pleas enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        //if the email and password are not empty
        //show a progress bar

        progressDialog.setMessage("Registration in process...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //user has successfully registered and logged in
                            //start profile activity here
                            finish();
                            startActivity(new Intent(getApplicationContext(), Profile_Activity.class));
                            Toast.makeText(MainActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this, "Could not complete registration. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //onClick method takes user to next activity after button press
    public void onClick(View view){

        String name =  editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();





        if (view == buttonRegister){
            registerUser();
        }

        if (view == textViewSignin){
            //open login activity
            startActivity(new Intent(this, Login_Activity.class));
        }
    }
}
