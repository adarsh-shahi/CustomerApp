package com.example.gotraser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;

import java.util.Date;

public class DateAndTimePicker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_and_time_picker);

        new SingleDateAndTimePickerDialog.Builder(this)
                //.bottomSheet()
                //.curved()
                //.stepSizeMinutes(15)
                //.displayHours(false)
                //.displayMinutes(false)
                //.todayText("aujourd'hui")
                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {
                        //retrieve the SingleDateAndTimePicker
                    }
                })

                .title("Simple")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {

                    }
                }).display();

    }



    private void display(String toDisplay) {
        Toast.makeText(this, toDisplay, Toast.LENGTH_SHORT).show();
    }
}
