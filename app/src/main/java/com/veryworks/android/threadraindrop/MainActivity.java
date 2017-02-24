package com.veryworks.android.threadraindrop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    FrameLayout layout;
    Stage stage;
    Button btnStart,btnPause,btnStop;

    int deviceWidth,deviceHeight;

    boolean running = true;

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
                DrawStage drawStage = new DrawStage(stage);
                drawStage.start();
                MakeRain rain = new MakeRain(stage);
                rain.start();
                break;
            case R.id.btnPause:
                break;
            case R.id.btnStop:
                break;
        }
    }

    class DrawStage extends Thread{

        Stage stage;
        public DrawStage(Stage stage){
            this.stage = stage;
        }

        public void run(){
            while(running){
                stage.postInvalidate();
//                try {
//                    Thread.sleep(10);
//                }catch(Exception e){
//                    e.printStackTrace();
//                }
            }
        }
    }

    class MakeRain extends Thread{
        boolean flag = true;
        Stage stage;
        public MakeRain(Stage stage){
            this.stage = stage;
        }

        @Override
        public void run(){

            while(flag){
                new Raindrop(stage);
                try {
                    Thread.sleep(10);
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

        boolean stopflag = false;
        boolean pauseflag = false;

        Stage stage;

        public Raindrop(Stage stage){
            Random random = new Random();
            x = random.nextInt(deviceWidth);
            y = 0;
            radius = random.nextInt(30)+5;
            speed = random.nextInt(10)+1;

            this.stage = stage;
            stage.addRaindrop(this);
        }

        @Override
        public void run(){
            while(!stopflag){
                if(!pauseflag) {
                    y = y + speed;
                    try {
                        Thread.sleep(10);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                if(y > deviceHeight)
                    stopflag = true;
            }
            stage.removeRaindrop(this);
        }
    }

    class Stage extends View {

        Paint rainColor;
        List<Raindrop> raindrops;

        public Stage(Context context) {
            super(context);
            raindrops = new CopyOnWriteArrayList<>();
            rainColor = new Paint();
            rainColor.setColor(Color.BLUE);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Log.i("Rain Size","==================================" + raindrops.size());
            for(Raindrop raindrop : raindrops) {
                canvas.drawCircle(raindrop.x, raindrop.y, raindrop.radius, rainColor);
            }
        }

        public void addRaindrop(Raindrop raindrop){
            raindrops.add(raindrop);
            raindrop.start();
        }

        public void removeRaindrop(Raindrop raindrop){
            raindrops.remove(raindrop);
            raindrop.interrupt();
        }
    }
}












