package com.example.it_food.activity.manager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.helper.DateValidator;
import com.example.it_food.model.Result;

import java.text.DecimalFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticalActivity extends AppCompatActivity {

    private EditText edtDateStart;
    private EditText edtDateEnd;
    private Button btnSend;
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private TextView tvTotalOrdersDay, tvTotalRevenue, tvTotalOrdersDaySeries, tvTotalPricesDaySeries;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_statistical);

        AnhXa();

        APIService.apiService.getTotalOrdersDay("6463b70d1b4d877e52f22b94").enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful()) {
                    Result result = response.body();
                    tvTotalOrdersDay.setText(result.getResult());

                }
                else {
                    Toast.makeText(StatisticalActivity.this, "Get total orders failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(StatisticalActivity.this, "Get total orders failed", Toast.LENGTH_SHORT).show();
            }
        });

        APIService.apiService.getTotalPricesDay("6463b70d1b4d877e52f22b94").enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful()) {
                    Result result = response.body();
                    Float number = Float.parseFloat(result.getResult());

                    // Làm tròn đến 2 chữ số thập phân
                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    String roundedNumber = decimalFormat.format(number);

                    tvTotalRevenue.setText("$ " + roundedNumber.toString());

                }
                else {
                    Toast.makeText(StatisticalActivity.this, "Get total prices failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(StatisticalActivity.this, "Get total prices failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AnhXa(){
        edtDateStart = (EditText) findViewById(R.id.edtDateStart);
        edtDateEnd = (EditText) findViewById(R.id.edtDateEnd);
        btnSend = (Button) findViewById(R.id.btnSend);
        tvTotalOrdersDay = (TextView) findViewById(R.id.tvTotalOrdersDay);
        tvTotalRevenue = (TextView) findViewById(R.id.tvTotalRevenue);
        tvTotalOrdersDaySeries = (TextView) findViewById(R.id.tvTotalOrdersDaySeries);
        tvTotalPricesDaySeries = (TextView) findViewById(R.id.tvTotalPricesDaySeries);
    }

    public void btnStartDate_Click(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                edtDateStart.setText(String.format("%d/%d/%d", dayOfMonth, month, year));
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public void btnEndDate_Click(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                edtDateEnd.setText(String.format("%d/%d/%d", dayOfMonth, month, year));
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public void btnSend_Click(View view) {
        final String dateStart = edtDateStart.getText().toString();
        final String endStart = edtDateEnd.getText().toString();

        if (!DateValidator.isValidDate(dateStart)) {
            edtDateStart.setError("Please enter your Date Start!");
            edtDateStart.requestFocus();
            return;
        }

        if (!DateValidator.isValidDate(endStart)) {
            edtDateEnd.setError("Please enter your Date Start!");
            edtDateEnd.requestFocus();
            return;
        }

        APIService.apiService.getTotalOrdersDaySeries("6463b70d1b4d877e52f22b94", dateStart, endStart).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful()) {
                    Result result = response.body();
                    tvTotalOrdersDaySeries.setText(result.getResult());

                }
                else {
                    Toast.makeText(StatisticalActivity.this, "Get total orders day Series failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(StatisticalActivity.this, "Get total orders day Series failed", Toast.LENGTH_SHORT).show();
            }
        });

        APIService.apiService.getTotalPricesDaySeries("6463b70d1b4d877e52f22b94", dateStart, endStart).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful()) {
                    Result result = response.body();
                    Float number = Float.parseFloat(result.getResult());

                    // Làm tròn đến 2 chữ số thập phân
                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    String roundedNumber = decimalFormat.format(number);

                    tvTotalPricesDaySeries.setText("$ " + roundedNumber.toString());

                }
                else {
                    Toast.makeText(StatisticalActivity.this, "Get total prices day Series failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(StatisticalActivity.this, "Get total prices day Series failed", Toast.LENGTH_SHORT).show();
            }
        });



    }
}