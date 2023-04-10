package com.example.pp_app;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {
    EditText loginEmail , loginPpassword;
    Button butlog;
    Drawable dr;
    boolean valid = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginEmail = findViewById(R.id.loginEmail);
        loginPpassword = findViewById(R.id.loginPassword);
        butlog = findViewById(R.id.buttonLogin);

        dr = getDrawable(R.drawable.ic_err);  //drawable object

        checkIfEmpty(loginEmail);
        checkIfEmpty(loginPpassword);




        butlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(),register.class));
                Intent intent = new Intent(login.this, register.class);
                startActivity(intent);
            }
        });




    }



    public boolean checkIfEmpty(EditText textField)
    {
        if(textField.toString().isEmpty())
        {
            textField.setError("Field is empty",dr);
            return valid = false;
        }else
            return valid = true;
    }
}
