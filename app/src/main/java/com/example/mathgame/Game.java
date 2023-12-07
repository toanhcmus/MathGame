package com.example.mathgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

public class Game extends AppCompatActivity {

    TextView txtScore, txtLife, txtTime;
    TextView txtQuestion;
    EditText answer;

    Button btnOk, btnNext;

    Random random = new Random();
    int number1, number2;
    int userAnswer;
    int realAnswer;
    int userScore = 0;
    int userLife = 3;
    CountDownTimer timer;
    private static final long START_TIMER_IN_MILIS = 60000;
    Boolean timer_running;
    long time_left_in_milis = START_TIMER_IN_MILIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        txtScore = findViewById(R.id.txtScore);
        txtLife = findViewById(R.id.txtLife);
        txtTime = findViewById(R.id.txtTime);

        txtQuestion = findViewById(R.id.txtQuestion);
        answer = findViewById(R.id.editTxtAnswer);

        btnNext = findViewById(R.id.btnNext);
        btnOk = findViewById(R.id.btnOk);

        gameContinue();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAnswer = Integer.valueOf(answer.getText().toString());

                pauseTimer();

                if (userAnswer == realAnswer)
                {
                    userScore = userScore + 10;
                    txtQuestion.setText("Congratulations, Your answer is true!");
                    txtScore.setText(""+userScore);
                }
                else
                {
                    userLife = userLife - 1;
                    txtLife.setText(""+userLife);
                    txtQuestion.setText("Sorry, Your answer is wrong!");
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                answer.setText("");
                resetTimer();

                if (userLife <= 0)
                {
                    Toast.makeText(Game.this, "Game over!!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Game.this, Result.class);
                    intent.putExtra("Score", userScore);
                    startActivity(intent);
                    finish();
                }
                else
                {

                    gameContinue();

                }
            }
        });

    }

    public void gameContinue() {
        number1 = random.nextInt(100);
        number2 = random.nextInt(100);
        realAnswer = number1 + number2;
        txtQuestion.setText(number1 + " + " + number2);
        startTimer();
    }

    public void startTimer()
    {
        timer = new CountDownTimer(time_left_in_milis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time_left_in_milis = millisUntilFinished;
                updateText();
            }

            @Override
            public void onFinish() {
                timer_running = false;
                txtQuestion.setText("Sorry! Time is up!");
                pauseTimer();
                resetTimer();
                updateText();
                userLife = userLife - 1;
                txtLife.setText(""+userLife);
            }
        }.start();
        timer_running = true;
    }

    public void resetTimer() {
        time_left_in_milis = START_TIMER_IN_MILIS;
        updateText();
    }

    public void pauseTimer() {
        timer.cancel();
        timer_running = false;
    }

    public void updateText()
    {
        int second = (int) ((time_left_in_milis / 1000) % 60);
        String time_left = String.format(Locale.getDefault(), "%02d", second);
        txtTime.setText(time_left);
    }

}