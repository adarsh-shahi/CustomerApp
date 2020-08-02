package com.example.gotraser;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

//        if(isServices()){
//            init();
//        }


        if (firebaseUser != null) {
            startActivity(new Intent(MainActivity.this, MapActivity.class));
            finish();
        } else {
            startActivity(new Intent(MainActivity.this, FirstScreen.class));
            finish();


        }

//        private void init(){
//            Button button = findViewById(R.id.btnMap);
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(MainActivity.this,MapActivity.class);
//                    startActivity(intent);
//                }
//            });
//
//
//        }


//        public boolean isServices(){
//
//            Log.d(TAG,"isServicesOk: checking google play services version");
//
//            int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
//            if(available== ConnectionResult.SUCCESS){
//                Log.d(TAG,"isServicesOk: checking google play services is working");
//                return  true;
//            }
//            else  if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
//                Log.d(TAG,"isServicesOk: an error occurred but we can fix it");
//                Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,available,ERROR_DIALOG_REQUEST);
//                dialog.show();
//            }
//            else {
//                Toast.makeText(this, "We have to do this any how", Toast.LENGTH_LONG).show();
//            }
//            return  false;
//
//
//        }


    }
}
