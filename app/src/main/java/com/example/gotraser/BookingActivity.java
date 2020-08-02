package com.example.gotraser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BookingActivity extends AppCompatActivity{

    private TextView tv_pickUp,tv_destination,vehiclePayment;
    private Button onlinePayment;
    final int UPI_PAYMENT = 0;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

        tv_pickUp=findViewById(R.id.pickupTv);
        tv_destination=findViewById(R.id.destinationTv);

        String pickup= getIntent().getStringExtra("pickup");
        String destination=getIntent().getStringExtra("destination");

        tv_pickUp.setText(pickup);
        tv_destination.setText(destination);
        onlinePayment=findViewById(R.id.OnlinePayment);
        vehiclePayment=findViewById(R.id.apePayment);
        radioGroup=findViewById(R.id.radioGroup);

        onlinePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                payUsingUpi("adarshshahi1009@oksbi",vehiclePayment.getText().toString().trim());
            }
        });



    }


    public void checkButton(View v){
        int radioId=radioGroup.getCheckedRadioButtonId();
        radioButton=findViewById(radioId);

        Toast.makeText(this, "Selected: "+radioButton.getText().toString()+" method.", Toast.LENGTH_SHORT).show();
    }



    private void payUsingUpi(String s, String trim) {

        Uri uri = Uri.parse("uri://pay").buildUpon()
                .appendQueryParameter("pa",s)
                .appendQueryParameter("am",trim)
                .appendQueryParameter("cu","INR")
                .build();




//        String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
//        int GOOGLE_PAY_REQUEST_CODE = 123;
//
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(uri);
//        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
//startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);



        Intent upiPayPayment=new Intent(Intent.ACTION_VIEW);
        upiPayPayment.setData(uri);
        Intent chooser = Intent.createChooser(upiPayPayment,"Pay with");

        if(null!=chooser.resolveActivity(getPackageManager())){
            startActivityForResult(chooser,UPI_PAYMENT);
        }
        else {
            Toast.makeText(BookingActivity.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }


    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(BookingActivity.this)) {
            String str = data.get(0);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(BookingActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(BookingActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(BookingActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(BookingActivity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }


    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }



}