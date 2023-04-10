package com.example.pp_app;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class register extends AppCompatActivity {

    EditText name,regMail, regPass, regPhone;
    Button regBut;
    Drawable dr;
    boolean valid = true;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        name = findViewById(R.id.fullName);
        regMail = findViewById(R.id.RegEmail);
        regPass = findViewById(R.id.regPass);
        regPhone = findViewById(R.id.phoneNumber);

        dr = getDrawable(R.drawable.ic_err);

        checkIfEmpty(name);
        checkIfEmpty(regMail);
        checkIfEmpty(regPass);
        checkIfEmpty(regPhone);

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
