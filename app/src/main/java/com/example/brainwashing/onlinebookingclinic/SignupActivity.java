package com.example.brainwashing.onlinebookingclinic;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.brainwashing.onlinebookingclinic.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity  {
    FirebaseAuth mAuth;
    Button signupBtn;
    private DatabaseReference mDatabase;
    private static final String TAG = "signupActivity";
    private EditText inputEmail, inputPassword, inputFname, inputLname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signupBtn = (Button) findViewById(R.id.signupBtn);
        inputEmail = (EditText) findViewById(R.id.emailTxt);
        inputPassword = (EditText) findViewById(R.id.passTxt);
        inputFname = (EditText) findViewById(R.id.fnameTxt);
        inputLname = (EditText) findViewById(R.id.lnameTxt);

        signupBtn.setOnClickListener(signup);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    View.OnClickListener signup = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String email= inputEmail.getText().toString();
            final String password = inputPassword.getText().toString();
            final String fname = inputFname.getText().toString();
            final String lname = inputLname.getText().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(SignupActivity.this, "Enter email address!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(SignupActivity.this, "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(fname)) {
                Toast.makeText(SignupActivity.this, "Enter firstname!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(lname)) {
                Toast.makeText(SignupActivity.this, "Enter lastname!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(SignupActivity.this, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                return;
            }
            final ProgressDialog pd = new ProgressDialog(SignupActivity.this);
            pd.setMessage("Waiting");
            pd.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser fbuser = mAuth.getCurrentUser();
                                String userid = fbuser.getUid();
                                User user = new User(email,fname,lname);
                                mDatabase.child("users").child(userid).setValue(user);
                                setCurrentLogin(userid);
                                pd.dismiss();
                                Log.d(TAG, "createUserWithEmail:success");
                                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                pd.dismiss();
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignupActivity.this, "Sign up failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    };

    public void setCurrentLogin(String userid){
        SharedPreferences sp = getSharedPreferences("PREF_LOGIN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("CurrentID", userid);
        editor.putString("AuthProvider", "Email");
        editor.commit();

    }
}
