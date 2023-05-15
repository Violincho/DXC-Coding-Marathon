package com.example.pp_app;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText name,regMail, regPass, regPhone;
    Button regBut;
    Drawable dr;
    boolean valid = true;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        name = findViewById(R.id.fullName);
        regMail = findViewById(R.id.RegEmail);
        regPass = findViewById(R.id.regPass);
        regPhone = findViewById(R.id.phoneNumber);


        dr = getDrawable(R.drawable.ic_err);

        regBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfEmpty(name);
                checkIfEmpty(regMail);
                checkIfEmpty(regPass);
                checkIfEmpty(regPhone);

                if(valid){
                    //startira se procesa za registraciq

                    fAuth.createUserWithEmailAndPassword(regMail.getText().toString(),
                            regPass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = fAuth.getCurrentUser();
                            Toast.makeText(Register.this, "Account Created!", Toast.LENGTH_SHORT).show();
                            DocumentReference df = fStore.collection("Propagators").document(user.getUid());
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("FullName" , name.getText().toString());
                            userInfo.put("PhoneNum" , regPhone.getText().toString());
                            userInfo.put("UserEmail" , regMail.getText().toString());

                            //check if user is ordinary(not admin)
                            userInfo.put("isOrdinaryUser" , "1");

                            df.set(userInfo);

                            startActivity(new Intent(getApplicationContext(), propagator_activity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Register.this, "Failed to create an account...", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

    }



    public boolean checkIfEmpty(@NonNull EditText textField)
    {
        if(textField.toString().isEmpty())
        {
            textField.setError("Field is empty",dr);
            return valid = false;
        }else
            return valid = true;
    }
}
