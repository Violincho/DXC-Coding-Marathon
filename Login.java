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
import com.example.pp_app.MainActivity;
import com.example.pp_app.R;
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
    boolean valid = true;

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


            }
        });





        butlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(),register.class));
                //                Intent intent = new Intent(login.this, register.class);
//                startActivity(intent);

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

                                    Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                            });
                }



            }
        });




    }

    private void checkUserAccessLevel(String uid) {
        DocumentReference df = fStore.collection("Users").document(uid);
        //izvlichane na danni ot bazata danni
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG" , "onSuccess " + documentSnapshot.getData());
                //identificirame kakyv e user-a (admin ili obiknoven)

                if(documentSnapshot.getString("isAdmin") != null){

                    //user-a e admin
                    startActivity(new Intent(getApplicationContext(), AdminActivity.class));  // otvarq se aktiviti na admin
                    finish();
                }

                if(documentSnapshot.getString("isUser") != null)
                {
                    //user-a e obiknoven
                    startActivity(new Intent(getApplicationContext(), MainActivity.class)); //otvarq se aktiviti na obiknoven user!
                    finish();
                }
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

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }
}
