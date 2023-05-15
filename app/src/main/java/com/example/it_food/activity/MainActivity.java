package com.example.it_food.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.it_food.R;

public class MainActivity extends AppCompatActivity {

    public TextView textProduct;
    public Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

}
        /*button.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Get error", Toast.LENGTH_SHORT).show();
                //clickGetPro();
            }
        });


    */
}