package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager Sensormanger;
    private Sensor Accel;
    TextView text;
    Vibrator v; // 진동 오브젝트 생성
    int check = 0; // 직전의 스마트폰 화면의 방향을 확인하기 위한 변수 -> 토스트 메시지 또는 진동을 한 번만 출력하기 위함

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Sensormanger = (SensorManager)getSystemService(Context.SENSOR_SERVICE); // 센서를 사용하기 위한 센서 매니저 객체 생성
        Accel = Sensormanger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // 가속도 센서를 사용하기 위한 센서 객체 생성
        text = findViewById(R.id.text); // 가속도 값 출력을 위한 텍스트 뷰 생성. 메인 액티비티의 텍스트뷰에 연결
        v = (Vibrator) getSystemService(VIBRATOR_SERVICE); // 진동 시스템 객체 생성
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 가속도 센서 이벤트 리스너 등록
        Sensormanger.registerListener(this, Accel, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 가속도 센서 이벤트 리스너 해제
        Sensormanger.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        // 측정한 가속도 값을 메인 액티비티에 출력
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // 차례대로 x축. y축, z축의 가속도 값 출력
            text.setText("Accel: x: " + event.values[0] + "\n"
                    + " y: " + event.values[1] + "\n" + " z: " + event.values[2]);
        }

        // 화면이 땅을 향해 있을 때 토스트 메시지를 출력하고 진동을 울림
        // z축의 가속도 값이 -9보다 낮고 check값이 1이 아닐 때(이전에 동일한 출력을 실행하지 않았을 때)
        if(event.values[2] <-9 && check != 1) {
            Toast.makeText(getApplicationContext(), "화면이 땅을 향해 있습니다.", Toast.LENGTH_SHORT).show(); // 토스트 메시지 출력
            v.vibrate(1000); // 진동을 1초간 출력
            check = 1; // 땅을 향해 있을 때 메시지 출력 및 진동 실행을 체크함
        }
        // 화면이 하늘을 향해 있을 때 토스트 메시지를 출력
        // z축의 가속도 값이 9보다 높고 check값이 2가 아닐 때(이전에 동일한 출력을 실행하지 않았을 때)
        else if(event.values[2] > 9 && check !=2) {
            Toast.makeText(this.getApplicationContext(), "화면이 하늘을 향해 있습니다.", Toast.LENGTH_SHORT).show();
            check = 2; // 하늘을 향해 있을 때 메시지 출력을 체크함
        }
        // 스마트폰이 수직 상태에 근접하면 체크 초기화
        else if(Math.abs(event.values[2]) < 1){
            check = 0;
        }
    }

}
