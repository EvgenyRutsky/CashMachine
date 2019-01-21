package com.example.dbryuzgin.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Button bRegisterR;
    EditText etFirstName, etLastName, etEmailR, etPasswordR, etPasswordRRepeat;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        etEmailR = (EditText) findViewById(R.id.etEmailR);
        etPasswordR = (EditText) findViewById(R.id.etPasswordR);
        bRegisterR = (Button) findViewById(R.id.bRegisterR);

        findViewById(R.id.bRegisterR).setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        switch(v.getId()){
            case R.id.bRegisterR:
                Registration(etEmailR.getText().toString(), etPasswordR.getText().toString());
                User user = new User(etEmailR.getText().toString(), etPasswordR.getText().toString());
                myRef = FirebaseDatabase.getInstance().getReference().child("Users");
                myRef.push().setValue(user);
                break;
        }
    }

    public void Registration (String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Регистрация не пройдена", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}
