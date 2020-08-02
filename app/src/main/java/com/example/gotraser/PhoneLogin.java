package com.example.gotraser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PhoneLogin extends AppCompatActivity {

    private EditText etNumber, enterOtp;
    private Button nextBtn, verifyBtn;
    private String phoneNumber, verificationId;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private ProgressBar progressBar;
    private LinearLayout linear1, linear2;
    private FirebaseFirestore db;
    private String mail, displayName, givenName, personId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        updateUI(firebaseUser);
        db=FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);
        etNumber = findViewById(R.id.enterNumber);
        nextBtn = findViewById(R.id.nextButton);
        linear1 = findViewById(R.id.linear1);
        linear2 = findViewById(R.id.linear2);
        enterOtp = findViewById(R.id.enterOtp);
        verifyBtn = findViewById(R.id.button);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String current = etNumber.getText().toString();
                if ((current.length() == 10)) {
                    phoneNumber = "+91" + etNumber.getText().toString().trim();
                    Toast.makeText(PhoneLogin.this, phoneNumber, Toast.LENGTH_SHORT).show();
                    sendVerificationCode(phoneNumber);
                    linear1.setVisibility(View.GONE);
                    linear2.setVisibility(View.VISIBLE);

                    verifyBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String code = enterOtp.getText().toString().trim();
                            if (code.isEmpty() || code.length() < 6) {
                                enterOtp.setError("Enter 6 digit code");
                                enterOtp.requestFocus();
                                return;
                            }

                            verifyCode(code);

                        }
                    });


                } else {
                    Toast.makeText(PhoneLogin.this, "Please enter 10 digit mobile number", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    saveToDB();
                    Intent intent = new Intent(PhoneLogin.this, MapActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else {
                    Toast.makeText(PhoneLogin.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    private void sendVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number, 120, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                enterOtp.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(PhoneLogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };


    private void updateUI(FirebaseUser fUser) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null) {
            mail = account.getEmail();
            displayName = account.getDisplayName();
            givenName = account.getGivenName();
            personId = account.getId();
            Toast.makeText(PhoneLogin.this, mail + " " + displayName + " " + givenName + "\n" + personId, Toast.LENGTH_LONG).show();

        }
    }


        private void saveToDB () {
            Map<String, Object> note = new HashMap<>();
            note.put("mail", mail);

            db.collection("Customers").document(phoneNumber).set(note)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(PhoneLogin.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
}