package com.example.project1_mehulkri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_page);
        getSupportActionBar().setTitle("CS 310                                Mehul Krishna");
        Intent intent = getIntent();
        // Set time
        int elpasedTime = intent.getIntExtra("elapsedTime", 0);
        TextView time = (TextView) findViewById(R.id.timeElapsed);
        String timeMessage = "Used " + String.valueOf(elpasedTime) + " seconds.";
        time.setText(timeMessage);
        // Set winning status
        boolean won = intent.getBooleanExtra("win", false);
        TextView outcome = (TextView) findViewById(R.id.outcome);
        TextView extraInfo = (TextView) findViewById(R.id.comment);
        if(won) {
            outcome.setText("You won.");
            extraInfo.setText("Good job!!");
        } else {
            outcome.setText("You lost.");
            extraInfo.setText("Try again!!");
        }
        Button play = (Button) findViewById(R.id.repeat);
        play.setOnClickListener(this::onClickButton);

    }

    public void onClickButton(View view) {
        Intent intent = new Intent(ResultsPage.this, MainActivity.class);
        startActivity(intent);
    }
}