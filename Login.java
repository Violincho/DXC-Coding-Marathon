package com.example.pp_app.login_reg;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pp_app.AdminActivity;
import com.example.pp_app.R;
import com.example.pp_app.company_activity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    EditText loginEmail , loginPassword;
    Button butlog , button;
    Drawable dr;
    boolean valid;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        butlog = findViewById(R.id.buttonLogin);
        button = findViewById(R.id.button);

        dr = getDrawable(R.drawable.ic_err);  //drawable object



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this , RegisterActivity.class));
                finish();

            }
        });







    }

    private void checkUserAccessLevel(String uid) {
        DocumentReference df = fStore.collection("Users").document(uid);
        //extract data from the database
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG" , "onSuccess " + documentSnapshot.getData());
                //identification if user is admin or ordinary

                if(documentSnapshot.getString("isAdmin") != null){

                    //if user is admin
                    startActivity(new Intent(getApplicationContext(), AdminActivity.class));  // Opens the admin activity
                    finish();
                }

                if(documentSnapshot.getString("isUser") != null)
                {
                    //if user is ordinary(not admin)
                    startActivity(new Intent(getApplicationContext(), company_activity.class)); //opens activities that refer to users
                    finish();
                }
            }
        });
    }


    public boolean checkIfEmpty(@Nullable EditText textField) {
        if (textField.toString().isEmpty()) {
            textField.setError("Field is empty", dr);
            valid = false;
        } else
            {
           valid = true;
            }

        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //check if a user is already logged in
//        if(FirebaseAuth.getInstance().getCurrentUser() != null)
//        {
//            startActivity(new Intent(getApplicationContext(), Login.class));
//            finish();
//        }


        butlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIfEmpty(loginEmail);
                checkIfEmpty(loginPassword);

                if (valid)
                {
                    fAuth.signInWithEmailAndPassword(loginEmail.getText().toString() , loginPassword.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult)
                                {
                                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    checkUserAccessLevel(authResult.getUser().getUid());

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(Login.this, "Login unsuccessful. Wrong password or e-mail", Toast.LENGTH_SHORT).show();
                                }
                            });
                }else {
                    Toast.makeText(Login.this, "Empty fields", Toast.LENGTH_SHORT).show();
                }




            }
        });

    }
}
