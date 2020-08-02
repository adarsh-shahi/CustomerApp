package com.example.gotraser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class FirstScreen extends AppCompatActivity {

    private Button continuePhone;
    private ImageView ivGoogle,ivFb;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN=1;
    private AccessTokenTracker accessTokenTracker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        mAuth=FirebaseAuth.getInstance();
        continuePhone = findViewById(R.id.phoneLogin);
        ivGoogle = findViewById(R.id.googleSignIn);

     //   ivFb=findViewById(R.id.fbSignIn);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(FirstScreen.this,gso);

        mCallbackManager = CallbackManager.Factory.create();

//        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                handleFacebookToken(loginResult.getAccessToken());
//                startActivity(new Intent(FirstScreen.this,PhoneLogin.class));
//                finish();
//
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        });














        continuePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstScreen.this,PhoneLogin.class));
            }
        });

        ivGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();


            }
        });

    }




    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(FirstScreen.this,"Google Signed In",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(FirstScreen.this,PhoneLogin.class));

            FirebaseGoogleAuth(acc);
        } catch (ApiException e) {
            e.printStackTrace();
            Toast.makeText(FirstScreen.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }


    }

    private void FirebaseGoogleAuth(GoogleSignInAccount account) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(FirstScreen.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(FirstScreen.this, "Successful", Toast.LENGTH_SHORT).show();
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    updateUI(firebaseUser);
                }
                else {
                    Toast.makeText(FirstScreen.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }


    private void updateUI(FirebaseUser fUser) {
        GoogleSignInAccount account =GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account!=null){
            String mail = account.getEmail();
            String displayName = account.getDisplayName();
            String givenName = account.getGivenName();
            String peronId = account.getId();
        }
    }


}