package com.example.pp_app.login_reg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pp_app.MainActivity;
import com.example.pp_app.R;
import com.example.pp_app.company_activity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText name1,regMail1, regPass1, regPhone1;
    Button regBut1;
    Drawable dr1;
    boolean valid1 ;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        name1 = findViewById(R.id.fullName2);
        regMail1 = findViewById(R.id.regEmail);
        regPass1 = findViewById(R.id.regPass2);
        regPhone1 = findViewById(R.id.phoneNumber2);
        dr1 = getDrawable(R.drawable.ic_err);

        regBut1 = findViewById(R.id.butreg2);


        regBut1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                checkIfEmpty(name1);
                checkIfEmpty(regMail1);
                checkIfEmpty(regPass1);
                checkIfEmpty(regPhone1);

                if(valid1){
                    //starting register procedure

                    fAuth.createUserWithEmailAndPassword(regMail1.getText().toString(),
                            regPass1.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = fAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "Account Created!", Toast.LENGTH_SHORT).show();
                            DocumentReference df = fStore.collection("Users").document(user.getUid());
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("FullName" , name1.getText().toString());
                            userInfo.put("PhoneNum" , regPhone1.getText().toString());
                            userInfo.put("UserEmail" , regMail1.getText().toString());

                            //check if user is admin
                            userInfo.put("isOrdinaryUser" , "1");

                            df.set(userInfo);

                            startActivity(new Intent(getApplicationContext(), company_activity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "Failed to create an account...", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

    }

    public boolean checkIfEmpty(EditText textField)
    {
        if(textField.toString().isEmpty())
        {
            textField.setError("Field is empty",dr1);
            return valid1 = false;
        }else
            return valid1 = true;
    }
}