package com.example.brainwashing.onlinebookingclinic;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.brainwashing.onlinebookingclinic.Models.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private String email;
    private String password;
    private FirebaseAuth mAuth;
    private static final String TAG = "GoogleSignInActivity";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private Button loginBtn;
    private Button googleBtn;
    private Button creatAccBtn;
    private DatabaseReference mDatabase;
    private static final int RC_SIGN_IN = 9001;
    private EditText inputEmail, inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        googleBtn = (Button) findViewById(R.id.googleBtn);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        creatAccBtn = (Button) findViewById(R.id.createAccountBtn);
        inputEmail = (EditText) findViewById(R.id.usernameTxt);
        inputPassword = (EditText) findViewById(R.id.passTxt);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        loginBtn.setOnClickListener(loginClick);
        googleBtn.setOnClickListener(googleClick);
        creatAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(in);

            }
        });

        if(checkCurrentLogin()){
            Intent in = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(in);
            finish();
        }

    }

    View.OnClickListener loginClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            loginWithEmailPassword();
        }
    };
    View.OnClickListener googleClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            loginWithGoogle();
        }
    };

    public void loginWithEmailPassword() {
        final String email= inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(LoginActivity.this, "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage("Sign in");
        pd.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete( Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("error", "signInWithEmail"+ task.getException().getMessage());
                            pd.dismiss();
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }else {
                            pd.dismiss();
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser fbuser = mAuth.getCurrentUser();
                            String userid = fbuser.getUid();
                            setCurrentLogin(userid,"Email");
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
    }

    private void loginWithGoogle() {
        if(mGoogleApiClient.isConnected()){
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        }
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(LoginActivity.this, "Google login failed.", Toast.LENGTH_SHORT).show();
                Log.i(TAG,"Google Sign In failed");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage("Waiting");
        pd.show();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                if (!task.isSuccessful()) {
                    pd.dismiss();
                    Toast.makeText(LoginActivity.this, "login failed.", Toast.LENGTH_SHORT).show();
                    Log.i(TAG,"task fail");
                } else {
                    Log.i(TAG,"task success");
                    pd.dismiss();
                    FirebaseUser fbuser = mAuth.getCurrentUser();
                    String userid = fbuser.getUid();
                    String str = fbuser.getDisplayName();
                    String[] name = str.split(" ");
                    User user = new User(fbuser.getEmail(),name[0],name[1]);
                    mDatabase.child("users").child(userid).setValue(user);
                    setCurrentLogin(userid,"Google");
                    Intent in = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(in);
                    finish();
                }
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    public void setCurrentLogin(String userid,String provider){
        SharedPreferences sp = getSharedPreferences("PREF_LOGIN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("CurrentID", userid);
        editor.putString("AuthProvider", provider);
        editor.commit();

    }
    public boolean checkCurrentLogin(){
        SharedPreferences sp = getSharedPreferences("PREF_LOGIN", Context.MODE_PRIVATE);
        String user_id = sp.getString("CurrentID","def");
        if(user_id == null || user_id == "def" || TextUtils.isEmpty(user_id)){
            return false;
        }
        return true;
    }

}
