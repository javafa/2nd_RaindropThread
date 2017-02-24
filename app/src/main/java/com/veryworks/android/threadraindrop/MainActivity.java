package com.veryworks.android.threadraindrop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    FrameLayout layout;
    Stage stage;
    Button btnStart,btnPause,btnStop;

    int deviceWidth,deviceHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 화면 가로세로 사이즈 가져오기
        DisplayMetrics matrix = getResources().getDisplayMetrics();
        deviceWidth = matrix.widthPixels;
        deviceHeight = matrix.heightPixels;

        // 위젯 세팅
        layout = (FrameLayout) findViewById(R.id.stage);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnPause = (Button) findViewById(R.id.btnPause);
        btnStop = (Button) findViewById(R.id.btnStop);

        btnStart.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnStop.setOnClickListener(this);

        // 커스텀뷰를 레이아웃에 add
        stage = new Stage(this);
        layout.addView(stage);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnStart:
                MakeRain rain = new MakeRain();
                rain.start();
                break;
            case R.id.btnPause:
                break;
            case R.id.btnStop:
                break;
        }
    }

    class MakeRain extends Thread{
        boolean flag = true;
        @Override
        public void run(){
            while(flag){
                Raindrop raindrop = new Raindrop();
                stage.addRaindrop(raindrop);
                raindrop.start();
                try {
                    Thread.sleep(50);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    class Raindrop extends Thread{
        int x;
        int y;
        int radius;
        int speed;
        int direction;

        boolean stopflag = true;
        boolean pauseflag = false;


        @Override
        public void run(){
            while(stopflag && y < deviceHeight + radius ){
                if(!pauseflag) {
                    y = y + speed;
                    stage.postInvalidate();
                    try {
                        Thread.sleep(10);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }

        public Raindrop(){
            Random random = new Random();
            x = random.nextInt(deviceWidth);
            y = 0;
            radius = random.nextInt(30)+5;
            speed = random.nextInt(10)+1;
        }
    }

    class Stage extends View {

        Paint rainColor;
        List<Raindrop> raindrops;

        public Stage(Context context) {
            super(context);
            raindrops = new ArrayList<>();
            rainColor = new Paint();
            rainColor.setColor(Color.BLUE);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            for(int i=0 ; i<raindrops.size() ; i++) {
                Raindrop raindrop = raindrops.get(i);
                canvas.drawCircle(raindrop.x, raindrop.y, raindrop.radius, rainColor);
            }
        }

        public void addRaindrop(Raindrop raindrop){
            raindrops.add(raindrop);
        }
    }
}












