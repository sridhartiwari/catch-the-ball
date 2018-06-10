package com.example.sridhartiwari.catchtheball;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    public TextView scoreLabel;
    private TextView startLabel;
    private ImageView box;
    private ImageView orange;
    private ImageView black;
    private ImageView pink;


    //Size
    private int frameHeight;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;

    //Position
    private int boxY;
    private int orangeX;
    private int orangeY;
    private int pinkX;
    private int pinkY;
    private int blackX;
    private int blackY;
    Context ct;

    // Score
    private int score = 0;


    //Initialize class
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    //Status Check
    private boolean action_flg = false;
    private boolean start_flg=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreLabel = (TextView) findViewById(R.id.scorelabel);
        startLabel = (TextView) findViewById(R.id.startlabel);
        box = (ImageView) findViewById(R.id.box);
        orange = (ImageView) findViewById(R.id.orange);
        pink = (ImageView) findViewById(R.id.pink);
        black = (ImageView) findViewById(R.id.black);



        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;



        orange.setX(80);
        orange.setY(80);
        pink.setX(80);
        pink.setY(80);
        black.setX(80);
        black.setY(80);



        scoreLabel.setText("score: 0" );





    }

    public void changepos() {

        hitCheck();





        orangeX -= 6;
        if(orangeX < 0){
            orangeX = screenWidth +20;
            orangeY =(int)Math.floor(Math.random() * (frameHeight - orange.getHeight()));
        }
        orange.setX(orangeX);
        orange.setY(orangeY);



        blackX -= 16;
        if(blackX < 0){
            blackX = screenWidth +10;
            blackY =(int)Math.floor(Math.random() * (frameHeight - black.getHeight()));
        }
        black.setX(blackX);
        black.setY(blackY);


        pinkX -= 20;
        if(pinkX < 0){
            pinkX = screenWidth +5000;
            pinkY =(int)Math.floor(Math.random() * (frameHeight - pink.getHeight()));
        }
        pink.setX(pinkX);
        pink.setY(pinkY);




       if(action_flg==true){

            boxY -=20;
        }else {

            boxY +=20;
        }



        if( boxY < 0) boxY=0;

        if( boxY > frameHeight - boxSize) boxY = frameHeight - boxSize;

        box.setY(boxY);

        scoreLabel.setText("score: " + score);
    }

    public void hitCheck(){


        int orangeCenterX = orangeX + orange.getWidth()/2;
        int orangeCenterY = orangeY + orange.getHeight()/2;




        if(0 <= orangeCenterX && orangeCenterX <= boxSize&&
                boxY <= orangeCenterY && orangeCenterY <= boxY + boxSize){

            score += 10;
            orangeX= -10;
        }


        int pinkCenterX = pinkX + pink.getWidth()/2;
        int pinkCenterY = pinkY + pink.getHeight()/2;

        if(0 <= pinkCenterX && pinkCenterX <= boxSize&&
                boxY <= pinkCenterY && pinkCenterY <= boxY + boxSize) {

            score += 100;
            orangeX = -10;
        }


        int blackCenterX = blackX + black.getWidth()/2;
        int blackCenterY = blackY + black.getHeight()/2;

        if(0 <= blackCenterX && blackCenterX <= boxSize&&
                boxY <= blackCenterY && blackCenterY <= boxY + boxSize) {


            //stop timer

            timer.cancel();
            timer = null;

            //show result
            Intent intent = new Intent(getApplicationContext(),result.class);
            intent.putExtra("SCORE",score);
            startActivity(intent);


        }
    }







    public boolean onTouchEvent(MotionEvent me) {

        if(start_flg==false){

            start_flg= true;


            //why get frame size height and box height here?
            //because the ui has not been set on the screen in OnCreate()

            FrameLayout frame=(FrameLayout)findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            boxY = (int)box.getY();



            boxSize = box.getHeight();


            startLabel.setVisibility(View.GONE);


            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changepos();

                        }
                    });
                }
            }, 0, 20);
        }else{
            if (me.getAction() == MotionEvent.ACTION_DOWN) {
               action_flg = true;
                boxY -=20;
            } else if (me.getAction() == MotionEvent.ACTION_UP) {
                action_flg = false;

            }
        }



        return true;


    }
}